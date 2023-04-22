package com.lizhi.once.importuser;

import com.lizhi.mapper.UserMapper;
import com.lizhi.model.domain.User;
import com.lizhi.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @author 荔枝
 */
@Component
public class InsertUsers {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;


    /**
     * 批量插入用户
     * Scheduled 任务调度
     *
     */
    public void doInsertUsers() {
       // @Scheduled(fixedDelay = Long.MAX_VALUE)  间隔非常多时间 执行一次  可以理解成只执行一次
        // stopWatch计时
        StopWatch stopWatch = new StopWatch();
        ArrayList userArrayList = new ArrayList<User>();
        stopWatch.start();
        final int INSERT_NUM = 1000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("机器人"+i+"号");
            user.setUserAccount("lizhi123"+i);
            user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("123");
            user.setEmail("123@qq.com");
            user.setTags("[]");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("11111111");
            // 单次插入
            userMapper.insert(user);
            //批量插入
//            userArrayList.add(user);

        }
        // 批量插入 (经测试批量插入的速度更加快)
//        userService.saveBatch(userArrayList);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
