package com.springboot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Reference -
 * https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby <br>
 * Referecen - https://github.com/brettwooldridge/HikariCP/wiki/About-Pool-Sizing <br>
 * 
 * @author folaukaveinga
 *
 */
@Profile("local")
@Configuration
public class LocalConfig {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@Bean
	public AWSCredentialsProvider amazonAWSCredentialsProvider() {
		return new ProfileCredentialsProvider("folauk+100");
	}
	
	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder
		  .standard()
		  .withCredentials(amazonAWSCredentialsProvider())
		  .withRegion(Regions.US_WEST_2)
		  .build();
	}

	// ================================== DB =============================

	@Bean
	public HikariDataSource dataSource() {
		log.info("Configuring local datasource...");

		String username = "root";
		String password = "";
		String dbName = "springboot-course-tshirt";
		String url = "jdbc:mysql://localhost:3306/" + dbName + "?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC";

		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		
		HikariDataSource hds = new HikariDataSource(config);
		hds.setMaximumPoolSize(30);
		hds.setMinimumIdle(20);
		hds.setMaxLifetime(1800000);
		hds.setConnectionTimeout(30000);
		hds.setIdleTimeout(600000);
		
		return hds;
	}
	
}
