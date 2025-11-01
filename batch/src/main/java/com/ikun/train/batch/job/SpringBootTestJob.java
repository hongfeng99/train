//package com.ikun.train.batch.job;
//
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
///*
//    适合单体应用，不适合集群
//    不能实时更改定时任务和策略
// */
//@Component
//@EnableScheduling
//public class SpringBootTestJob {
//
//    @Scheduled(cron = "0/5 * * * * *")
//    private void test(){
//        // 增加分布式锁
//        LocalDateTime now = LocalDateTime.now();
//        System.out.println(now);
//    }
//}
