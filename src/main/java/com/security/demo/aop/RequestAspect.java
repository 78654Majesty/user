package com.security.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * description
 *
 * @author fanglingxiao
 * @date 2019/4/8
 */
@Aspect
@Component
public class RequestAspect {

    private final static Logger logger = LoggerFactory.getLogger(RequestAspect.class);

    @Pointcut(value = "@annotation(com.security.demo.aop.Action)")
    public void reqAspect(){}

//    @Before(value = "@annotation(com.security.demo.aop.Action)")
    @Before(value = "reqAspect()")
    public void doBefore(JoinPoint joinPoint){
        //获取到请求的属性
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取到请求对象
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        //URL：根据请求对象拿到访问的地址
        logger.info("url=" + request.getRequestURL());
        //获取请求的方法，是Get还是Post请求
        logger.info("method=" + request.getMethod());
        //ip：获取到访问
        logger.info("ip=" + request.getRemoteAddr());
        //获取被拦截的类名和方法名
        logger.info("class=" + joinPoint.getSignature().getDeclaringTypeName() +
                "and method name=" + joinPoint.getSignature().getName());
        //参数
        logger.info("参数=" + Arrays.toString(joinPoint.getArgs()));
    }

//    @Around(value = "reqAspect()")
//    public Object doAround(ProceedingJoinPoint point){
//        MethodSignature signature = (MethodSignature) point.getSignature();
//        Method method = signature.getMethod();
//        return point.getArgs();
//    }
//
//    @After(value = "reqAspect()")
//    public void doAfter(){
//
//    }
}
