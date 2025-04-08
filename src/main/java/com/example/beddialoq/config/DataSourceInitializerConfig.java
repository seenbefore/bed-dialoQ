package com.example.beddialoq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * 数据源初始化配置类
 */
@Configuration
public class DataSourceInitializerConfig {

    @Value("classpath:db/schema.sql")
    private Resource schemaScript;

    @Value("classpath:db/data.sql")
    private Resource dataScript;

    /**
     * 配置数据源初始化器
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    /**
     * 配置数据库填充器
     */
    private DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        populator.addScript(dataScript);
        populator.setSeparator(";"); // SQL语句分隔符
        populator.setContinueOnError(true); // 忽略错误继续执行
        return populator;
    }
} 