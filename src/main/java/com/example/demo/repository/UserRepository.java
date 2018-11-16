package com.example.demo.repository;

import com.example.demo.domain.HpbPushHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *@Author: Michael
 *@Date: Created in 16:14 2018/6/14
 *@Desciption: 
*/
public interface UserRepository extends JpaRepository<HpbPushHistory, Long> {
        List<HpbPushHistory> findByPushTo(String pushTo);

        @Query("SELECT O FROM HpbPushHistory O WHERE O.pushTo = :pushTo and O.createDate > :tenBefore ")
        List<HpbPushHistory> findData(@Param("tenBefore") Long tenBefore,@Param("pushTo") String pushTo);
}
