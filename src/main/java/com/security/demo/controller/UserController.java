package com.security.demo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.security.demo.auth.Login;
import com.security.demo.entity.CurrentUser;
import com.security.demo.entity.ExcelDataVO;
import com.security.demo.entity.User;
import com.security.demo.service.impl.UserServiceImpl;
import com.security.demo.util.OssUtil;
import com.security.demo.util.ResultApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public ResultApi<String> register(@Valid @RequestBody CurrentUser model){
        ResultApi<String> res = new ResultApi<>();

        if (StringUtils.isEmpty(model.getPassword()) || StringUtils.isEmpty(model.getUserName())
        || StringUtils.isEmpty(model.getRealName())){
            res.setResCode(-1);
            res.setResMsg("注册有误！请输入有效参数");
        }
        userService.register(model);
        res.setResCode(200);
        res.setResMsg("注册成功！");
        return res;
    }

    @PostMapping("login")
    public ResultApi<String> login(@Valid @RequestBody CurrentUser model){
        logger.info("login params {}",model);

        String token = userService.login(model);

        ResultApi<String> res = new ResultApi<>();
        if (StringUtils.isEmpty(token)){
            res.setResCode(-1);
            res.setResMsg("用户不存在！请重新登陆");
            return res;
        }
        res.setResCode(200);
        res.setResMsg("登陆成功！");
        res.setDate(token);
        return res;
    }

    @PostMapping("queryUserList")
    @Login
    public ResultApi<List<User>> queryUserList(HttpServletRequest request){
        List<User> users = userService.queryUserList();
        ResultApi<List<User>> res = new ResultApi<>();
        res.setDate(users);
        Map<String,Object> currentUser = (Map<String, Object>) request.getAttribute("currentUser");
        String userName = (String) currentUser.get("userName");
        return res;
    }

    @GetMapping("load")
    public ResultApi<String> uploadStr(@RequestParam String str){
        ossUtil.uploadStr(str);
        ResultApi<String> res = new ResultApi<>();
        res.setResCode(200);
        res.setResMsg("登陆成功！");
        return res;
    }

    @PostMapping("uploadFile")
    public ResultApi<String> uploadFile(@RequestParam MultipartFile multipartFile){
        String url = ossUtil.uploadFile(multipartFile);
        ResultApi<String> res = new ResultApi<>();
        res.setResCode(200);
        res.setResMsg("登陆成功！");
        res.setDate(url);
        return res;
    }

    @PostMapping("downloadFile")
    public ResultApi<String> downloadFile(@RequestParam String url){
        ossUtil.downloadFile(url,"");
        ResultApi<String> res = new ResultApi<>();
        res.setResCode(200);
        res.setResMsg("登陆成功！");
        res.setDate(url);
        return res;
    }

    @PostMapping("readExcel")
    public ResultApi<String> readExcel(@RequestParam String url){
        List<ExcelDataVO> list = (List<ExcelDataVO>) ossUtil.readExcel(url, ExcelDataVO.class, ExcelDataVO.DTO);
        System.out.println(JSONObject.toJSONString(list));
        ResultApi<String> res = new ResultApi<>();
        res.setResCode(200);
        res.setResMsg("登陆成功！");
        res.setDate(url);
        return res;
    }

    @GetMapping("exportExcel1")
    public ResultApi<String> exportExcel1(@RequestParam int userId){
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

        ResultApi<String> res = new ResultApi<>();
        res.setResCode(200);
        res.setResMsg("登陆成功！");
        return res;
    }

    @GetMapping("exportExcel2")
    public ResultApi<String> exportExcel2(@RequestParam int userId, HttpServletResponse response){
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

        ResultApi<String> res = new ResultApi<>();
        res.setResCode(200);
        res.setResMsg("登陆成功！");
        return res;
    }
}
