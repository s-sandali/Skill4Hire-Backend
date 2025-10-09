package com.se.skill4hire.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("migration")
public class MigrationConfig {

    @Value("${spring.datasource.url:}")
    private String url;

    @Value("${spring.datasource.username:}")
    private String username;

    @Value("${spring.datasource.password:}")
    private String password;

    // Only create DataSource if a JDBC URL is provided
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.url")
    public DataSource legacyDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        // If the URL indicates H2 and H2 driver is on the classpath, set driver explicitly
        if (url != null && url.startsWith("jdbc:h2")) {
            ds.setDriverClassName("org.h2.Driver");
        }
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    public JdbcTemplate jdbcTemplate(DataSource legacyDataSource) {
        return new JdbcTemplate(legacyDataSource);
    }
}
