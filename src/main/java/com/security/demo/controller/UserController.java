package com.security.demo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.security.demo.aop.Action;
import com.security.demo.auth.Login;
import com.security.demo.entity.CurrentUser;
import com.security.demo.entity.ExcelDataVO;
import com.security.demo.entity.ExcelError;
import com.security.demo.entity.User;
import com.security.demo.service.impl.UserServiceImpl;
import com.security.demo.util.OssUtil;
import com.security.demo.util.ResultApi;
import org.apache.commons.compress.utils.Lists;
import org.apache.xmlbeans.XmlError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author fanglingxiao
 * @date 2019/3/26
 */
@RestController
@RequestMapping("**/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${fang.export.excel}")
    private String exportUrl;

    @Autowired
    private OssUtil ossUtil;

    @PostMapping("register")
    public ResultApi register(@Valid @RequestBody CurrentUser model){

        if (StringUtils.isEmpty(model.getPassword()) || StringUtils.isEmpty(model.getUserName())
        || StringUtils.isEmpty(model.getRealName())){
            return new ResultApi.Builder<String>().setResCode(-1).setResMsg("注册有误！请输入有效参数").build();
        }
        userService.register(model);
        return new ResultApi.Builder<String>().setResCode(200).setResMsg("注册成功!").build();
    }

    @PostMapping("login")
    public ResultApi login(@Valid @RequestBody CurrentUser model){
        logger.info("login params {}",model);

        String token = userService.login(model);

        ResultApi.Builder<String> builder = new ResultApi.Builder<>();
        if (StringUtils.isEmpty(token)){
            return builder.setResCode(-1).setResMsg("用户不存在！请重新登陆").build();
        }
        return builder.setResCode(200).setResMsg("登陆成功!").setDate(token).build();
    }

    @Login
    @Action
    @PostMapping("queryUserList")
    public ResultApi queryUserList(HttpServletRequest request){
        List<User> users = userService.queryUserList();
        Map<String,Object> currentUser = (Map<String, Object>) request.getAttribute("currentUser");
        String userName = (String) currentUser.get("userName");
        logger.info("userName is {}",userName);
        return new ResultApi.Builder<List<User>>().setDate(users).build();
    }

    @GetMapping("load")
    public ResultApi uploadStr(@RequestParam String str){
        ossUtil.uploadStr(str);
        return new ResultApi.Builder<String>().build();
    }

    @PostMapping("uploadFile")
    public ResultApi uploadFile(@RequestParam MultipartFile multipartFile){
        String url = ossUtil.uploadFile(multipartFile);
        return new ResultApi.Builder<String>().setDate(url).build();
    }

    @PostMapping("downloadFile")
    public ResultApi downloadFile(@RequestParam String url){
        ossUtil.downloadFile(url,"");
        return new ResultApi.Builder<String>().setDate(url).build();
    }

    /**
     * 解析excel
     * @param url
     * @return
     */
    @PostMapping("readExcel")
    public ResultApi readExcel(@RequestParam String url){
        List<ExcelDataVO> list = (List<ExcelDataVO>) ossUtil.readExcel(url, ExcelDataVO.class, ExcelDataVO.DTO);
        if (CollectionUtils.isEmpty(list)){
            return new ResultApi.Builder<String>().build();
        }
        ArrayList<ExcelError> errorList = Lists.newArrayList();
        checkListContent(list,errorList);
        logger.info("readExcel list is {}",JSONObject.toJSONString(list));
        return new ResultApi.Builder<String>().setDate(url).build();
    }

    private void checkListContent(List<ExcelDataVO> list, ArrayList<ExcelError> errorList) {
        //收集excel是否内容重复
        Map<String,String> map = Maps.newHashMap();
        list.forEach(model->{
            //Validator注解错误信息
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<ExcelDataVO>> violations = validator.validate(model);
            if (violations.isEmpty()) {
                return;
            }
            violations.forEach(t -> {
                ExcelError error = new ExcelError();
                error.setReason(t.getMessage().split("_")[1]);
                error.setErrorLine("");
                error.setFieldName(t.getMessage().split("_")[0]);
                errorList.add(error);
            });
        });
    }

    @GetMapping("exportExcel1")
    public ResultApi exportExcel1(@RequestParam int userId){
        ExcelDataVO vo = new ExcelDataVO();
        vo.setName("张三");
        vo.setAge(23);
        vo.setLocation("上海");
        vo.setJob("程序员");

        ExcelWriter writer = ExcelUtil.getWriter(exportUrl+"a.xlsx");
        writer.addHeaderAlias("name","姓名");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("location","居住地");
        writer.addHeaderAlias("job","职业");

        List<ExcelDataVO> list = CollUtil.newArrayList(vo);
        writer.merge(3,"员工信息");
        writer.write(list);
//        writer.flush();
        writer.close();

        return new ResultApi.Builder<String>().build();
    }

    @GetMapping("exportExcel2")
    public ResultApi exportExcel2(@RequestParam int userId, HttpServletResponse response){
        ExcelDataVO vo = new ExcelDataVO();
        vo.setName("张三");
        vo.setAge(23);
        vo.setLocation("上海");
        vo.setJob("程序员");

        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("name","姓名");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("location","居住地");
        writer.addHeaderAlias("job","职业");

        List<ExcelDataVO> list = CollUtil.newArrayList(vo);
        writer.merge(3,"员工信息");
        writer.write(list);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=test.xls");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            writer.flush(outputStream);
        } catch (IOException e) {
            logger.info("error",e);
        }
        writer.close();

        return new ResultApi.Builder<String>().build();
    }

    /**
     * 用于生成带四位数字验证码的图片
     */
//    @GetMapping(value = "captcha")
//    public String imagecode(@RequestParam String captchaId, HttpServletResponse response) throws Exception {
//        response.setDateHeader("Expires", 0);
//        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
//        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
//        response.setHeader("Pragma", "no-cache");
//        response.setContentType("image/jpeg");
//
//        String s = getTrippleDes().decrypt(captchaId);
//        String strEnsure = s.substring(0, 4);
//
//        OutputStream os = response.getOutputStream();
//        //返回验证码和图片的map
//        Map<String, Object> map = CaptchaUtil.getImageCode(86, 37, os, strEnsure);
//        try {
//            ImageIO.write((BufferedImage) map.get("image"), "jpg", os);
//        } catch (IOException e) {
//            return "";
//        } finally {
//            if (os != null) {
//                os.flush();
//                os.close();
//            }
//        }
//        return null;
//    }

}
