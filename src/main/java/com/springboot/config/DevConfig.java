package com.springboot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.springboot.aws.AWSSecretsManagerUtils;
import com.springboot.credentials.DbSecret;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Profile(value = {"dev"})
@Configuration
public class DevConfig {
    private Logger                 log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private AWSSecretsManagerUtils awsSecretsManagerUtils;
    
	@Bean
	public AWSCredentialsProvider amazonAWSCredentialsProvider() {
		
		return DefaultAWSCredentialsProviderChain.getInstance();
	}
	
	@Bean
	public AWSSecretsManager awsSecretsManager() {
		AWSSecretsManager awsSecretsManager = AWSSecretsManagerClientBuilder
				.standard()
				.withCredentials(amazonAWSCredentialsProvider())
				.withRegion(Regions.US_WEST_2)
				.build();
		
		return awsSecretsManager;
	}

	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder
		  .standard()
		  .withCredentials(amazonAWSCredentialsProvider())
		  .withRegion(Regions.US_WEST_2)
		  .build();
	}
    
    @Bean
    public HikariDataSource dataSource() {
        //log.debug("DB SECRET: {}", dbSecret.toJson());
    	
    	DbSecret dbSecret = awsSecretsManagerUtils.getDbSecret();

        log.info("Configuring dev datasource...");
        Integer port = 3306;
        String host = dbSecret.getHost();
        String username = dbSecret.getUsername();
        String password = dbSecret.getPassword();
        String dbName = "springboot-course-tshirt";
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=utf8&useSSL=false";
        
        HikariConfig config = new HikariConfig();
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);

		HikariDataSource hds = new HikariDataSource(config);
		hds.setMaximumPoolSize(30);
		hds.setMinimumIdle(20);
		hds.setMaxLifetime(1800000);
		hds.setConnectionTimeout(30000);
		hds.setIdleTimeout(600000);
		
		return hds;
    }
   
}
