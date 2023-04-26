package com.lizhi.job;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lizhi.model.domain.User;
import com.lizhi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热任务
 *
 * @author <a href="https://github.com/lizhe-0423">荔枝</a>
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;


    private List<Long> mainUserList=new ArrayList<>();

    /**
     *    每天执行，预热推荐用户
     */
    @Scheduled(cron = "0 31 0 * * *")
    public void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock("find-friend:precachejob:docache:lock");
        try {
            // 只有一个线程能获取到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock: " + Thread.currentThread().getId());
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                Wrapper<User> gt = queryWrapper.gt(User::getUserRole, 0);
                List<User> userList = userService.getBaseMapper().selectList(gt);
                for(User user:userList){
                    mainUserList.add(user.getId());
                }
                for (Long userId : mainUserList) {
                    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
                    //不推荐自己
                    wrapper.ne(User::getId,userId);
                    Page<User> userPage = userService.page(new Page<>(1, 20),wrapper);
                    String redisKey = String.format("find-friend:user:recommend:%s", userId);
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    // 写缓存
                    try {
                        valueOperations.set(redisKey, userPage, 1, TimeUnit.MINUTES);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doCacheRecommendUser error", e);
        } finally {
            // 只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }

}
