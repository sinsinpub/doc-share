package com.jfinal.ext.plugin.hikaricp;

import java.util.Properties;

import javax.sql.DataSource;

import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 支持<a href="https://github.com/brettwooldridge/HikariCP">HikariCP</a>的JFinal插件.
 * 
 * @author sin_sin
 * @version $Date: Mar 10, 2016 $
 */
public class HikariCpPlugin implements IPlugin, IDataSourceProvider {

    private final HikariConfig config;
    private HikariDataSource dataSource;

    public HikariCpPlugin() {
        config = new HikariConfig();
    }

    public HikariCpPlugin(String jdbcUrl, String username, String password) {
        this();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
    }

    public HikariCpPlugin(Properties props) {
        config = new HikariConfig(props);
    }

    public HikariCpPlugin setPoolName(String poolName) {
        config.setPoolName(poolName);
        return this;
    }

    public HikariCpPlugin setJdbcUrl(String jdbcUrl) {
        config.setJdbcUrl(jdbcUrl);
        return this;
    }

    public HikariCpPlugin setDriverClassName(String driverClassName) {
        config.setDriverClassName(driverClassName);
        return this;
    }

    public HikariCpPlugin setDataSourceClassName(String className) {
        config.setDataSourceClassName(className);
        return this;
    }

    public HikariCpPlugin setConnectionTimeout(long connectionTimeoutMs) {
        config.setConnectionTimeout(connectionTimeoutMs);
        return this;
    }

    public HikariCpPlugin setIdleTimeout(long idleTimeoutMs) {
        config.setIdleTimeout(idleTimeoutMs);
        return this;
    }

    public HikariCpPlugin setMaxLifetime(long maxLifetimeMs) {
        config.setMaxLifetime(maxLifetimeMs);
        return this;
    }

    public HikariCpPlugin setMinimumIdle(int minIdle) {
        config.setMinimumIdle(minIdle);
        return this;
    }

    public HikariCpPlugin setMaximumPoolSize(int maxPoolSize) {
        config.setMaximumPoolSize(maxPoolSize);
        return this;
    }

    public HikariCpPlugin setConnectionTestQuery(String connectionTestQuery) {
        config.setConnectionTestQuery(connectionTestQuery);
        return this;
    }

    public HikariCpPlugin setAutoCommit(boolean isAutoCommit) {
        config.setAutoCommit(isAutoCommit);
        return this;
    }

    public HikariCpPlugin setRegisterMbeans(boolean register) {
        config.setRegisterMbeans(register);
        return this;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public boolean start() {
        dataSource = new HikariDataSource(config);
        return true;
    }

    @Override
    public boolean stop() {
        if (dataSource != null)
            dataSource.close();
        return true;
    }

}
