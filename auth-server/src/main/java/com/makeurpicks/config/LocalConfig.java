package com.makeurpicks.config;

import java.beans.PropertyVetoException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@Profile("local")
public class LocalConfig {

	

 
    @Bean
    public ComboPooledDataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        
        dataSource.setMaxPoolSize(10);
        dataSource.setMinPoolSize(2);
        dataSource.setAcquireIncrement(2);
        dataSource.setIdleConnectionTestPeriod(3000);
        dataSource.setMaxStatements(10);
        dataSource.setMaxIdleTime(5000);
        dataSource.setJdbcUrl("jdbc:mysql://localhost/player");
        dataSource.setPassword("rage311");
        dataSource.setUser("root");
        dataSource.setDriverClass("org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider");
        return dataSource;
    }
    
//			    @Bean
//				public String ui()
//				{
//					return "http://localhost:8080/";
//				}
//				
//				@Bean
//				public String admin()
//				{
//					return "http://localhost:9000/admin";
//				}
	
}