package com.security.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fanglingxiao
 * @date 2019/4/2
 */
@Component
@ConfigurationProperties(prefix = "ossconfig.fang")
public class OssConfigProperties {
    private String endPoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String firstKey;

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getFirstKey() {
        return firstKey;
    }

    public void setFirstKey(String firstKey) {
        this.firstKey = firstKey;
    }
}
