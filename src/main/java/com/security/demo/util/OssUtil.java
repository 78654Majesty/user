package com.security.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.security.demo.config.OssConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @author fanglingxiao
 * @date 2019/4/2
 */
@Component
public class OssUtil {

    @Autowired
    private OssConfigProperties ossConfigProperties;

    private static final Logger logger = LoggerFactory.getLogger(OssUtil.class);
    /**
     * 上传字符串
     * @param content content
     */
    public void uploadStr(String content){
        OSSClient ossClient = new OSSClient(ossConfigProperties.getEndPoint(), ossConfigProperties.getAccessKeyId(), ossConfigProperties.getAccessKeySecret());
        PutObjectResult putObjectResult = ossClient.putObject(ossConfigProperties.getBucketName(), ossConfigProperties.getFirstKey(), new ByteArrayInputStream(content.getBytes()));
        System.out.println(JSONObject.toJSONString(putObjectResult));
        ossClient.shutdown();
    }

    /**
     * 上传字节数组
     * @param content content
     */
    public void uploadByte(byte[] content){
        OSSClient ossClient = new OSSClient(ossConfigProperties.getEndPoint(), ossConfigProperties.getAccessKeyId(), ossConfigProperties.getAccessKeySecret());
        ossClient.putObject(ossConfigProperties.getBucketName(),ossConfigProperties.getFirstKey(), new ByteArrayInputStream(content));
        ossClient.shutdown();
    }

    /**
     * 上传网络流
     */
    public void uploadInp(String url){
        OSSClient ossClient = new OSSClient(ossConfigProperties.getEndPoint(),ossConfigProperties.getAccessKeyId(), ossConfigProperties.getAccessKeySecret());
        try {
            InputStream inputStream = new URL(url).openStream();
            PutObjectResult putObjectResult = ossClient.putObject(ossConfigProperties.getBucketName(), ossConfigProperties.getFirstKey(), inputStream);
            System.out.println(JSONObject.toJSONString(putObjectResult));
        } catch (IOException e) {
            logger.info("oss error {}",e);
        }finally {
            ossClient.shutdown();
        }
    }

    /**
     * 上传文件
     * @param url 本地文件
     * @return string 文件名
     */
    public void uploadFile(String url){
        OSSClient ossClient = new OSSClient(ossConfigProperties.getEndPoint(), ossConfigProperties.getAccessKeyId(), ossConfigProperties.getAccessKeySecret());
        ossClient.putObject(ossConfigProperties.getBucketName(),ossConfigProperties.getFirstKey(),new File(url));
        ossClient.shutdown();
    }

    public String uploadFile(MultipartFile multipartFile){
        OSSClient ossClient = new OSSClient(ossConfigProperties.getEndPoint(), ossConfigProperties.getAccessKeyId(), ossConfigProperties.getAccessKeySecret());
        String fileName = multipartFile.getName();
        String objectKey = ossConfigProperties.getFirstKey()+"/"+fileName;
        if (0!=multipartFile.getSize() && !StringUtils.isEmpty(fileName)){
            try {
                ossClient.putObject(ossConfigProperties.getBucketName(),objectKey,new ByteArrayInputStream(multipartFile.getBytes()));
            } catch (IOException e) {
                logger.info("upload file error {}",e);
            }
        }
        // 设置这个文件地址的有效时间
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        String url = ossClient.generatePresignedUrl(ossConfigProperties.getBucketName(), objectKey, expiration).toString();
        ossClient.shutdown();
        return url;
    }

    /**
     * 下载文件
     * @param url 文件路径 objectName 文件名
     */
    public void downloadFile(String url,String objectName){
        OSSClient ossClient = new OSSClient(ossConfigProperties.getEndPoint(), ossConfigProperties.getAccessKeyId(), ossConfigProperties.getAccessKeySecret());
        ossClient.getObject(new GetObjectRequest(ossConfigProperties.getBucketName(),objectName),new File(url));
        ossClient.shutdown();
    }
}
