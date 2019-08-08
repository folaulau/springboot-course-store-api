package com.springboot.aws;


import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.springboot.credentials.DbSecret;

@Profile(value = { "dev"})
@Component
public class AWSSecretsManagerUtils {
	private Logger log = LoggerFactory.getLogger(AWSSecretsManagerUtils.class);

	@Value("${datasource.secret.name}")
	private String dataSourceSecretName;

	@Autowired
	private AWSSecretsManager awsSecretsManager;

	public DbSecret getDbSecret() {
		return DbSecret.fromJson(getCredentials(dataSourceSecretName));
	}
	
	private String getCredentials(String secretId) {
		
		GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest();
		getSecretValueRequest.setSecretId(secretId);

		GetSecretValueResult getSecretValueResponse = null;
		
		try {
			
			getSecretValueResponse = awsSecretsManager.getSecretValue(getSecretValueRequest);
			
		} catch (Exception e) {
			log.error("Exception, msg: ", e.getMessage());
		}

		if (getSecretValueResponse == null) {
			log.error("secret value response is null");
			return null;
		}

		ByteBuffer binarySecretData;
		String secret;
		// Decrypted secret using the associated KMS CMK
		// Depending on whether the secret was a string or binary, one of these fields
		// will be populated
		if (getSecretValueResponse.getSecretString() != null) {
			log.info("secret string");
			secret = getSecretValueResponse.getSecretString();
		} else {
			log.info("secret binary secret data");
			binarySecretData = getSecretValueResponse.getSecretBinary();
			secret = binarySecretData.toString();
		}
		
		return secret;
	}
}
