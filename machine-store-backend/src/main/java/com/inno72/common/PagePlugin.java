package com.inno72.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.PropertyException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.inno72.utils.page.Pagination;
import com.inno72.utils.page.ReflectHelper;

@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class PagePlugin implements Interceptor {

	private static String dialect = ""; // 数据库方言
	private static String pageSqlId = ""; // mapper.xml中需要拦截的ID(正则匹配
	private static String levelpageSqlId = "";

	@Override
	public Object intercept(Invocation ivk) throws Throwable {
		if (ivk.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler,
					"delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate,
					"mappedStatement");

			String id = mappedStatement.getId();
			boolean matches = id.matches(".*" + pageSqlId);
			boolean matchesLevel = id.matches(".*" + levelpageSqlId);
			if (matchesLevel) {
				BoundSql boundSql = delegate.getBoundSql();
				Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
				if (parameterObject == null) {
					parameterObject = "";
				}
				Connection connection = (Connection) ivk.getArgs()[0];
				String sql = boundSql.getSql();
				String upperCaseSql = sql.toUpperCase();
				String tableName = getFirstTableName(upperCaseSql);
				int indexOfFrom = upperCaseSql.indexOf("FROM");
				String fromCase = "select distinct " + tableName + ".id " + upperCaseSql.substring(indexOfFrom);
				PreparedStatement countStmt = connection.prepareStatement(fromCase);
				BoundSql countBS = mappedStatement.getBoundSql(parameterObject);
				setParameters(countStmt, mappedStatement, countBS, parameterObject);
				ResultSet rs = countStmt.executeQuery();
				int count = 0;
				rs.last();
				count = rs.getRow();
				rs.close();
				countStmt.close();
				Pagination page = Pagination.threadLocal.get();
				if (page == null) { // 没有设置分页page
					page = new Pagination();
					Pagination.threadLocal.set(page);
				}
				page.setTotalCount(count); // 设置总条数
				String pageSql = generatePageSql(sql, page);
				ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); // 将分页sql语句反射回BoundSql.
			} else {
				if (matches) { // 拦截需要分页的SQL
					BoundSql boundSql = delegate.getBoundSql();
					Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
					if (parameterObject == null) {
						parameterObject = "";
					}
					Connection connection = (Connection) ivk.getArgs()[0];
					String sql = boundSql.getSql();
					String upperCaseSql = sql.toUpperCase();
					PreparedStatement countStmt = connection.prepareStatement(upperCaseSql);
					BoundSql countBS = mappedStatement.getBoundSql(parameterObject);
					setParameters(countStmt, mappedStatement, countBS, parameterObject);
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					rs.last();
					count = rs.getRow();
					rs.close();
					countStmt.close();
					Pagination page = Pagination.threadLocal.get();
					if (page == null) { // 没有设置分页page
						page = new Pagination();
						Pagination.threadLocal.set(page);
					}
					page.setTotalCount(count); // 设置总条数
					String pageSql = generatePageSql(sql, page);
					ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); // 将分页sql语句反射回BoundSql.
				}
			}
		}
		return ivk.proceed();
	}

	/**
	 * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.
	 * DefaultParameterHandler
	 * 
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
							&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value)
									.getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName
								+ " of statement " + mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}

	/**
	 * 根据数据库方言，生成特定的分页sql
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	private String generatePageSql(String sql, Pagination page) {
		if (page != null && StringUtils.isNotEmpty(dialect)) {
			StringBuffer pageSql = new StringBuffer();
			if ("mysql".equals(dialect)) {
				pageSql.append(sql);
				pageSql.append(" limit " + page.getCurrentResult() + "," + page.getPageSize());
			} else if ("oracle".equals(dialect)) {
				pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
				pageSql.append(sql);
				pageSql.append(") as tmp_tb where ROWNUM<=");
				pageSql.append(page.getCurrentResult() + page.getPageSize());
				pageSql.append(") where row_id>");
				pageSql.append(page.getCurrentResult());
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}

	@Override
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	@Override
	public void setProperties(Properties p) {
		dialect = p.getProperty("dialect");
		if (StringUtils.isEmpty(dialect)) {
			try {
				throw new PropertyException("dialect property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
		pageSqlId = p.getProperty("pageSqlId");
		if (StringUtils.isEmpty(pageSqlId)) {
			try {
				throw new PropertyException("pageSqlId property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
		levelpageSqlId = p.getProperty("levelpageSqlId");
		if (StringUtils.isEmpty(levelpageSqlId)) {
			try {
				throw new PropertyException("levelpageSqlId property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}

	}

	private String getFirstTableName(String sql) {
		String a = sql.substring(sql.indexOf("FROM") + 5, sql.length());
		String aa = a.substring(a.indexOf(" "), a.length()).trim();
		String aaa = aa.substring(0, aa.indexOf(" "));
		return aaa.replace("\n", "");
	}

	public static void main(String[] args) {
		String sql = "a from b\n bb from cc c from dd d".toUpperCase();
		String a = sql.substring(sql.indexOf("FROM") + 5, sql.length());
		System.out.println(a);

		String aa = a.substring(a.indexOf(" "), a.length()).trim();
		String aaa = aa.substring(0, aa.indexOf(" "));

		System.out.println(aaa);
		System.out.println("+++" + sql);
		System.out.println("+++" + sql.replace("\n", ""));

	}
}
