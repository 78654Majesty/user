package com.security.demo.webapi;

import com.security.demo.entity.ProcessParamsContext;
import com.security.demo.service.WebApiTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author fanglingxiao
 * @desc webapi 多个平台统一入口
 * 自定义在request请求头中增加platform参数表明哪个平台比如抖音(DY)、拼多多(PDD)
 * @date 2019/12/11
 */
@RestController()
@RequestMapping("/api/v1")
public class WebApiTestController {

    private static final Logger logger = LoggerFactory.getLogger(WebApiTestController.class);

    @Autowired
    private WebApiTestService webApiTestService;

    @PostMapping("subscribe")
    public String subscribe(HttpServletRequest request, HttpServletResponse response, @RequestBody String requestBody) {
        logger.info("webapi params =" + requestBody);

        System.out.println(request.getRemoteAddr());

        // 获取request中参数
//        Enumeration<String> headerNames = request.getHeaderNames();
//        Map<String,String> paramMap = Maps.newHashMap();
//        while (headerNames.hasMoreElements()){
//            String paramName = headerNames.nextElement();
//            String[] parameterValues = request.getParameterValues(paramName);
//            if (parameterValues.length == 1){
//                if (parameterValues[0].length() != 0){
//                    paramMap.put(paramName,parameterValues[0]);
//                }
//            }
//        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        String result = webApiTestService.process(new ProcessParamsContext(request,response,requestBody));
        return "";
    }
}
