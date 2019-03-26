package com.security.demo.auth;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Maps;
import com.security.demo.util.JwtUtil;
import com.security.demo.util.ResultApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 登陆拦截器
 * ctrl + o 接口重写快捷键
 * @author fanglingxiao
 * @date 2019/3/26
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(Login.class)){
            return true;
        }
        Login login = method.getAnnotation(Login.class);
        if (!login.required()){
            return false;
        }
        if (!checkToken(token)){
            //token有误
            ResultApi res = new ResultApi();
            res.setResCode(-1);
            res.setResMsg("请重新登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().append(JSONUtils.toJSONString(res));
            return false;
        }
        String userName = JwtUtil.getUserName(token);
        String realName = JwtUtil.getRealName(token);

        Map<String,Object> currentUser = Maps.newHashMap();
        currentUser.put("userName",userName);
        currentUser.put("realName",realName);
        request.setAttribute("currentUser",currentUser);
        return true;
    }

    private boolean checkToken(String token){
        // 执行认证
        if (null == token) {
            throw new RuntimeException("无token，请重新登录");
        }
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("token格式有误，请重新登录");
        }
        String userName = JwtUtil.getUserName(token);
        if (StringUtils.isEmpty(userName)){
            return false;
        }
        String tokenInRedis = redisTemplate.opsForValue().get(userName);
        if (StringUtils.isEmpty(tokenInRedis)){
            return true;
        }
        String realName = JwtUtil.getRealName(token);
        long expireIn = JwtUtil.getExpireIn(token);

        if (StringUtils.isEmpty(realName)){
            return false;
        }
        String userNameInRedis = JwtUtil.getUserName(tokenInRedis);
        String realNameInRedis = JwtUtil.getRealName(tokenInRedis);
        long expireInRedis = JwtUtil.getExpireIn(tokenInRedis);

        if (StringUtils.isEmpty(userNameInRedis) || StringUtils.isEmpty(realNameInRedis)){
            return false;
        }
        //判断token是否过期
        if (expireIn != expireInRedis){
            return false;
        }
        return expireIn >= System.currentTimeMillis();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
