package com.inno72.common;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.github.miemiedev.mybatis.paginator.OffsetLimitInterceptor.BoundSqlSqlSource;
import com.inno72.system.model.Inno72UserFunctionArea;
import com.inno72.utils.page.ReflectHelper;

@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }), })

public class AuthPlugin implements Interceptor {

	@SuppressWarnings("unused")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		Object[] args = invocation.getArgs();
		MappedStatement stm = (MappedStatement) args[0];
		Object parameter = args[1];
		RowBounds rowBounds = (RowBounds) args[2];
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
		String id = stm.getId();
		boolean matches = id.contains("withAuth");
		if (matches) { // 拦截需要分页的SQL
			Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
			if (parameterObject == null) {
				parameterObject = "";
			}

			String sql = boundSql.getSql();
			SessionData session = CommonConstants.SESSION_DATA;
			List<Inno72UserFunctionArea> functionArea = Optional.ofNullable(session).map(SessionData::getFunctionArea)
					.orElse(null);
			System.out.println(sql);

			if (sql.contains("2=2")) {

				sql = sql.replace("2=2", "3=3");
			}

			ReflectHelper.setValueByFieldName(boundSql, "sql", sql);
			args[0] = copyFromNewSql(stm, boundSql, sql);
		}
		return invocation.proceed();
	}

	private MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql, String sql) {
		BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);
		return copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
	}

	private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
			StringBuffer keyProperties = new StringBuffer();
			for (String keyProperty : ms.getKeyProperties()) {
				keyProperties.append(keyProperty).append(",");
			}
			keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
			builder.keyProperty(keyProperties.toString());
		}

		// setStatementTimeout()
		builder.timeout(ms.getTimeout());

		// setStatementResultMap()
		builder.parameterMap(ms.getParameterMap());

		// setStatementResultMap()
		builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());

		// setStatementCache()
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());

		return builder.build();
	}

	private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(),
				boundSql.getParameterObject());
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
			String prop = mapping.getProperty();
			if (boundSql.hasAdditionalParameter(prop)) {
				newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
			}
		}
		return newBoundSql;
	}

	@Override
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	@Override
	public void setProperties(Properties p) {
	}

}
