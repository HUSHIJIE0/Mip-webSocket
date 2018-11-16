package com.example.demo.service;

import com.example.demo.domain.HpbPushHistory;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Michael
 * @Date: Created in 18:24 2018/6/14
 * @Desciption:
 */
@Service
public class HpbPushHistoryService {
    @Autowired
    private UserRepository userRepository;

    public List<HpbPushHistory> getHistory(String orgId){
        return userRepository.findByPushTo(orgId);
    }

    public HpbPushHistory save(HpbPushHistory hpbPushHistory){
        return userRepository.save(hpbPushHistory);
    }

    public List<HpbPushHistory> findData(Long tenBefore,String orgId){
        return userRepository.findData(tenBefore,orgId);
    }

}
