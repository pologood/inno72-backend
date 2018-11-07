package com.inno72.common;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

@SuppressWarnings("rawtypes")
@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }), })
public class DataAutherInterceptor implements Interceptor {

	private static final List SIMPLE_TYPE_NAMES = CollectionUtils
			.arrayToList(new String[] { "char", "byte", "short", "int", "long", "float", "double", "boolean" });

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	static private ThreadLocal<String> userIdThreadLocal = new ThreadLocal<String>();
	static private ThreadLocal<List<String>> forbiddenColumn = new ThreadLocal<List<String>>();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		Object[] args = invocation.getArgs();
		MappedStatement stm = (MappedStatement) args[0];

		String userId = userIdThreadLocal.get();
		forbiddenColumn.set(null);

		Object parameter = args[1];
		RowBounds rowBounds = (RowBounds) args[2];
		ResultHandler resultHandler = (ResultHandler) args[3];
		Executor executor = (Executor) invocation.getTarget();
		BoundSql boundSql;
		CacheKey cacheKey;
		if (args.length == 4) {
			boundSql = stm.getBoundSql(parameter);
			cacheKey = executor.createCacheKey(stm, parameter, rowBounds, boundSql);
		} else {
			cacheKey = (CacheKey) args[4];
			boundSql = (BoundSql) args[5];
		}

		List resList = null;

		if (userId == null) {
			resList = executor.query(stm, parameter, rowBounds, resultHandler, cacheKey, boundSql);
		} else {
			DataSource ds = stm.getConfiguration().getEnvironment().getDataSource();
			userIdThreadLocal.set(null);
			Connection conn = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			Map<String, List<String>> conditions = new HashMap<String, List<String>>();
			try {
				conn = ds.getConnection();

				statement = conn.prepareStatement(
						"select vo_name, vo_column from inno72_user_function_data where user_id=? order by vo_name");

				statement.setString(1, userId);

				rs = statement.executeQuery();

				String curName = "";
				List<String> voColumns = null;
				while (rs.next()) {
					String voName = rs.getString(1);
					if (!curName.equals(voName)) {
						if (voColumns != null) {
							conditions.put(curName, voColumns);
						}
						voColumns = new ArrayList<String>();
						curName = voName;
					}
					if (voColumns != null) {
						voColumns.add(rs.getString(2));
					}
				}

				if (StringUtils.isNotEmpty(curName)) {
					conditions.put(curName, voColumns);
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			}

			resList = executor.query(stm, parameter, rowBounds, resultHandler, cacheKey, boundSql);

			if (conditions != null && !conditions.isEmpty()) {
				filtVoPropertyList(resList, conditions);
			}

		}

		return resList;
	}

	private List filtVoPropertyList(List result, Map<String, List<String>> conditions) {

		if (conditions != null) {
			boolean isFirst = true;
			for (Object vo : result) {
				List<String> forbiddenItems = filtVoPropertyItem(vo, conditions);
				if (isFirst) {
					isFirst = false;
					if (forbiddenItems != null && !forbiddenItems.isEmpty()) {
						forbiddenColumn.set(forbiddenItems);
					}
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private List<String> filtVoPropertyItem(Object vo, Map<String, List<String>> conditions) {

		List<String> properties = conditions.get(vo.getClass().getName());
		List<String> forbiddenItems = new ArrayList<String>();

		if (properties != null) {

			for (String property : properties) {

				String[] levels = StringUtils.split(property, '.');

				if (levels != null) {

					Object curVo = vo;

					for (int i = 0; i < levels.length; i++) {
						if (curVo == null)
							break;
						if (isMapType(curVo.getClass())) {
							Object item = ((Map) curVo).get(levels[i]);
							if (item == null) {
								break;
							} else {
								if (i == (levels.length - 1)) {
									((Map) curVo).put(levels[i], null);
									forbiddenItems.add(property);
								} else
									curVo = item;
							}
						} else {

							Field field = this.getSupperDeclaredField(curVo.getClass(), levels[i]);

							if (field == null) {
								logger.warn(
										"No field named [" + levels[i] + "] in class " + curVo.getClass().getName());
								break;
							} else {
								if (i == (levels.length - 1)) {
									field.setAccessible(true);
									try {
										if (isSimpleType(field)) {
											field.set(curVo, getSimpleTypeNullVale(field));
										} else {
											field.set(curVo, null);
										}
										forbiddenItems.add(property);
									} catch (Exception e) {
										logger.error(e.getMessage(), e);
									} finally {
										field.setAccessible(false);
									}
								} else {
									try {
										curVo = field.get(curVo);
									} catch (Exception e) {
										logger.error(e.getMessage(), e);
										curVo = null;
									}
								}
							}

						}

					}
				}

			}

		}

		return forbiddenItems;
	}

	private boolean isMapType(Class clazz) {
		return Map.class.isAssignableFrom(clazz);
	}

	static public void setUserId(String userId) {
		userIdThreadLocal.set(userId);
	}

	static public List<String> forbiddenColumn() {
		return forbiddenColumn.get();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

	private Object getSimpleTypeNullVale(Field f) {
		switch (f.getType().getName()) {
		case "char":
			return new Character((char) 0x00);
		case "byte":
			return new Byte((byte) 0x00);
		case "short":
			return new Short((short) 0);
		case "int":
			return new Integer(0);
		case "long":
			return new Long(0);
		case "float":
			return new Float(0.0);
		case "double":
			return new Double(0.0);
		case "boolean":
			return new Boolean(false);
		default:
			return null;
		}
	}

	private boolean isSimpleType(Field f) {
		String simpleName = f.getType().getName();
		return SIMPLE_TYPE_NAMES.contains(simpleName);
	}

	private Field getSupperDeclaredField(Class clazz, String fieldName) {

		Class tempClass = clazz;
		while (tempClass != null) {
			try {
				Field field = tempClass.getDeclaredField(fieldName);
				return field;
			} catch (NoSuchFieldException e) {
				tempClass = tempClass.getSuperclass();
				if (tempClass != null && "java.lang.Object".equals(tempClass.getName())) {
					tempClass = null;
				}
			} catch (SecurityException e) {
				tempClass = tempClass.getSuperclass();
				if (tempClass != null && "java.lang.Object".equals(tempClass.getName())) {
					tempClass = null;
				}
			}
		}
		return null;
	}

}
