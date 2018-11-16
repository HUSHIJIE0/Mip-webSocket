package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.SocketUser;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.IUserManager;
import com.example.demo.service.impl.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Michael
 * @Date: Created in 15:31 2018/6/11
 * @Desciption:
 */
@RestController
@RequestMapping(value="imController")
public class ImController {
    private static final Logger logger = LoggerFactory.getLogger(ImController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserRepository userRepository;

    private IUserManager userManager = UserManager.getInstance();

    @RequestMapping(value = "helloWorld")
    public String HelloWorld() {
        logger.info("{}", userRepository);
        logger.info("查询size：" + userRepository.findByPushTo("1").size());
        return "Hello World!";
    }

    @RequestMapping(value = "getUserCounts")
    public String getUserCounts() {
        return userManager.getOnlineCount()+"";
    }

    @RequestMapping(value = "getUsers")
    public JSONObject getUsers() {
        Map<String, Map<String, SocketUser>> userGroup = userManager.getAllByGroup();
        Map<String,List<String>> userIdGroup = new HashMap<String, List<String>>();
        for (String orgCode :userGroup.keySet() ){
            List temp = new ArrayList();
            for(String userId :userGroup.get(orgCode).keySet()){
                temp.add(userId);
            }
            userIdGroup.put(orgCode,temp);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("users",userIdGroup);
        return jsonObject;
    }





}
