package com.security.demo.service.impl;

import com.security.demo.dao.UserDao;
import com.security.demo.entity.CurrentUser;
import com.security.demo.entity.User;
import com.security.demo.service.UserService;
import com.security.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author fanglingxiao
 * @date 2019/3/26
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public String login(CurrentUser model) {
        String userName = model.getUserName();
        String password = model.getPassword();
        User user = userDao.selectUserByName(userName);
        if (null == user){
            return "";
        }
        if (!StringUtils.isEmpty(password) && password.equals(user.getPassword())){
            //create token
            CurrentUser c = new CurrentUser();
            c.setUserName(user.getUserName());
            c.setRealName(user.getRealName());
            String jwtToken = JwtUtil.createJWT("1", c, "{\"uId\":\"123\"}", System.currentTimeMillis() + 1000*60*60*24);
            redisTemplate.opsForValue().set(user.getUserName(),"Bearer "+jwtToken,1, TimeUnit.DAYS);
            return jwtToken;
        }
        return "";
    }

    public void register(CurrentUser model) {
        String password = model.getPassword();
        String realName = model.getRealName();
        String userName = model.getUserName();
        User user = new User();
        user.setPassword(password);
        user.setUserName(userName);
        user.setRealName(realName);

        userDao.insert(user);
    }

    public List<User> queryUserList() {
        return userDao.selectAll();
    }
}
