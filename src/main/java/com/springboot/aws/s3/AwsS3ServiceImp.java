package com.springboot.aws.s3;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.springboot.utils.ObjectUtils;

@Service
public class AwsS3ServiceImp implements AwsS3Service {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AmazonS3 amazonS3;

	@Override
	public String uploadFile(String key, File file) {
		log.debug("uploadFile()");
		
		PutObjectResult result = amazonS3.putObject(new PutObjectRequest(AwsS3Bucket.DEV_RESOURCE_100, key, file).withCannedAcl(CannedAccessControlList.PublicRead));
		
		log.info("PutObjectResult={}",ObjectUtils.toJson(result));
		
		return String.valueOf(amazonS3.getUrl(AwsS3Bucket.DEV_RESOURCE_100, key));
	}

}
