package com.example.quay_so.utils;

import com.example.quay_so.configuration.MinIoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.security.cert.X509Certificate;
import java.io.File;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;

@Component
public class MinIOUploader {
    private final Logger LOGGER = LoggerFactory.getLogger(MinIOUploader.class);
    private RestTemplate restTemplate;
    private MinIoConfig minIoConfig;

    public MinIOUploader(RestTemplateBuilder builder, MinIoConfig minIoConfig) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30)).build();
        this.minIoConfig = minIoConfig;
    }

    public String getMinioUrl(MultipartFile file,String bookId) {
        String uploadDir = minIoConfig.getUploadFolder();
        String fileName = file.getOriginalFilename();
        try {
            LOGGER.warn("Start uploading file: " + fileName);
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDir + File.separator + fileName);
            Files.write(path, bytes);
            String imageUrl = uploadBinaryFileToMinIO(path.toString(),fileName,bookId);
            LOGGER.warn("End uploading imageUrl: " + imageUrl);
            return imageUrl;
        } catch (Exception e) {
            LOGGER.error("Error while uploading file: ", e);
            return "";
        } finally {
            File fileToDelete = new File(uploadDir + File.separator + fileName);
            if(fileToDelete.exists()){
                LOGGER.warn("Delete file: " + fileName);
                fileToDelete.delete();
            }
        }
    }

    private String uploadBinaryFileToMinIO(String filePath,String fileName,String bookId) {
        try {
            File file = new File(filePath);
            byte[] fileBytes = Files.readAllBytes(file.toPath());

            String minIoEndpoint = minIoConfig.getEndpoint() + "/" + bookId + "-" + fileName;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.add("Authorization", "Bearer " + minIoConfig.getAccessKey() + ":" + minIoConfig.getSecretKey());
            HttpEntity<byte[]> requestEntity = new HttpEntity<>(fileBytes, headers);

           disableSslVerification();
            ResponseEntity<String> response = restTemplate.exchange(minIoEndpoint, HttpMethod.PUT, requestEntity, String.class);
            LOGGER.warn("Upload MinIO response:{}",response);
            // Check the response
            if (response.getStatusCode().is2xxSuccessful()) {
                return minIoEndpoint;
            }
            return "";
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("Error while uploading file: ", e);
            return "";
        } catch (ResourceAccessException | ConnectException e) {
            LOGGER.error( "Error while uploading file: ", e);
            return "";
        } catch (Exception e) {
            LOGGER.error("Error while uploading file: ", e);
            return "";
        }

    }


    private static void disableSslVerification() throws NoSuchAlgorithmException, KeyManagementException {
        // Create a trust manager that accepts all certificates
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[]{
                new javax.net.ssl.X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        // Create a hostname verifier that accepts all hostnames
        HostnameVerifier allHostsValid = (hostname, session) -> true;

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}

