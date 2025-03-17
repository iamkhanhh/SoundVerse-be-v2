package com.TLU.SoundVerse.service;

import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class S3Service {
  @Value("${aws.accessKeyId}")
  private String accessKeyId;

  @Value("${aws.secretAccessKey}")
  private String secretAccessKey;

  @Value("${aws.region}")
  private String region;

  @Value("${aws.bucketName}")
  private String bucketName;

  private S3Presigner getPresigner() {
    return S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
        .build();
  }

  public String createPresignedUrl(String fileName, Integer user_id) {
    try (S3Presigner presigner = getPresigner()) {
      String objectKey = user_id + "/" + fileName; 

      PutObjectRequest objectRequest = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(objectKey)
          .contentType("audio/mpeg")
          .build();

      PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
          .signatureDuration(Duration.ofMinutes(10))
          .putObjectRequest(objectRequest)
          .build();

      PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
      return presignedRequest.url().toString();
    }
  }

  public String createPresignedUrlForThumbnail(String fileName, Integer user_id) {
    try (S3Presigner presigner = getPresigner()) {
      String objectKey = user_id + "/thumbnails/" + fileName; 
      String contentType = getContentType(fileName);
      System.out.println(contentType);

      PutObjectRequest objectRequest = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(objectKey)
          .contentType(contentType)
          .build();

      PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
          .signatureDuration(Duration.ofMinutes(10))
          .putObjectRequest(objectRequest)
          .build();

      PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
      return presignedRequest.url().toString();
    }
  }

  public String getS3Url(String fileName) {
    System.out.println("Checking S3 key: " + fileName);
    System.out.println("Bucket Name: " + bucketName);
    try (S3Presigner presigner = getPresigner()) {
      GetObjectRequest objectRequest = GetObjectRequest.builder()
          .bucket(bucketName)
          .key(fileName)
          .build();

      GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofHours(12))
          .getObjectRequest(objectRequest)
          .build();

      PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
      return presignedRequest.url().toString();
    }
  }

  public String generateFileName(String name) {
    name = name.replaceAll("\\s+", "_").trim();

    Matcher matcher = Pattern.compile("(\\.[^.]+)$").matcher(name);
    String extension = matcher.find() ? matcher.group(1) : "";

    String baseName = extension.isEmpty() ? name : name.replace(extension, "");

    String timeStamp = String.valueOf(System.currentTimeMillis());
    String uniqueID = UUID.randomUUID().toString();

    return baseName + "-" + timeStamp + "-" + uniqueID + extension;
  }

  private String getContentType(String fileName) {
    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
        return "image/jpeg";
    } else if (fileName.endsWith(".png")) {
        return "image/png";
    } else if (fileName.endsWith(".gif")) {
        return "image/gif";
    } else if (fileName.endsWith(".webp")) {
        return "image/webp";
    }
    return "application/octet-stream";
  }
}
