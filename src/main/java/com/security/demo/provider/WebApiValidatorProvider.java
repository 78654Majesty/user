package com.security.demo.provider;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.security.demo.entity.CurrentUser;
import com.security.demo.entity.ProcessParamsContext;
import com.security.demo.util.MD5Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

/**
 * @author fanglingxiao
 * @desc 验证器
 * @date 2019/12/11
 */
@Component
public class WebApiValidatorProvider {

    @Value("#{${webapi.platform.secret}}")
    private Map<String, String> platformSecretMap;

    public Function<ProcessParamsContext, Boolean> validator(ProcessParamsContext context) {
        String platform = context.getRequest().getParameter("platform");
        switch (platform) {
            case "DY":
                return this::dyValidator;
            case "PDD":
                return this::pddValidator;
            default:
                throw new UnsupportedOperationException("no support platform");
        }
    }

    /**
     * 假设抖音的签名等系统参数是拼接在url上
     */
    private boolean dyValidator(ProcessParamsContext context) {

        String platform = context.getRequest().getParameter("platform");
        String digest = context.getRequest().getParameter("digest");
        String secret = platformSecretMap.get(platform);
        String requestBody = context.getRequestBody();
        // guava工具
        Preconditions.checkArgument(StrUtil.isNotBlank(digest), "digest is empty");
        Preconditions.checkArgument(StrUtil.isNotBlank(secret), "secret is empty");
        Preconditions.checkArgument(StrUtil.isNotBlank(requestBody), "requestBody is empty");
        return digest.equals(MD5Util.generateDigestWithMd5AndBase64(platform + requestBody + secret));
    }

    /**
     * 假设拼多多的签名等参数是在body中
     */
    private boolean pddValidator(ProcessParamsContext context) {
        // 根据body的参数定义对象转换
        CurrentUser currentUser = JSONObject.parseObject(context.getRequestBody(), CurrentUser.class);

        return false;
    }
}
