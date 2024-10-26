package org.cresplanex.core.common.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class CoreSpringJdbcStatementExecutor implements CoreJdbcStatementExecutor {

    private final JdbcTemplate jdbcTemplate;

    public CoreSpringJdbcStatementExecutor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public long insertAndReturnGeneratedId(String sql, String idColumn, Object... params) {
        if (logger.isDebugEnabled()) {
            logger.debug("insertAndReturnGeneratedId {} {} {}", sql, idColumn, Arrays.asList(params));
        }
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                for (int i = 1; i <= params.length; i++) {
                    preparedStatement.setObject(i, params[i - 1]);
                }

                return preparedStatement;
            }, holder);

            Map<String, Object> keys = holder.getKeys();

            if (keys == null) {
                throw new IllegalStateException("Generated keys are null");
            }

            if (keys.size() > 1) {
                // necessary for postgres. For postgres holder returns all columns.
                // return (Long) holder.getKeys().get(idColumn);
                if (keys.containsKey(idColumn)) {
                    return (Long) keys.get(idColumn);
                } else {
                    throw new IllegalStateException("Generated keys are null or do not contain the specified id column");
                }
            } else {
                Number key = holder.getKey();
                if (key != null) {
                    return key.longValue();
                } else {
                    throw new IllegalStateException("Generated key is null");
                }
            }
        } catch (DuplicateKeyException e) {
            throw new CoreDuplicateKeyException(e);
        }
    }

    @Override
    public int update(String sql, Object... params) {
        try {
            return jdbcTemplate.update(sql, params);
        } catch (DuplicateKeyException e) {
            throw new CoreDuplicateKeyException(e);
        }
    }

    @Override
    public <T> List<T> query(String sql, CoreRowMapper<T> coreRowMapper, Object... params) {
        return jdbcTemplate.query(sql, coreRowMapper::mapRow, params);
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Object... parameters) {
        return jdbcTemplate.queryForList(sql, parameters);
    }
}
