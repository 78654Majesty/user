package com.security.demo.controller;

import com.security.demo.auth.Login;
import com.security.demo.entity.CurrentUser;
import com.security.demo.entity.User;
import com.security.demo.service.impl.UserServiceImpl;
import com.security.demo.util.ResultApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

}
