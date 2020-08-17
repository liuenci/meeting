package com.stylefeng.guns.core.util;

/**
 * @program: meeting
 * @description: 令牌桶
 * @author: liuenci
 * @create: 2020-08-17 19:38
 **/
public class TokenBucket {
    private int bucketNums = 100; // 令牌桶容量
    private int rate = 1; // 流入速度
    private int nowTokens; // 当前令牌数量
    private long timestamp = getNowTime(); // 时间戳

    private long getNowTime() {
        return System.currentTimeMillis();
    }

    private int min(int tokens) {
        if (bucketNums > tokens) {
            return tokens;
        } else {
            return bucketNums;
        }
    }

    public boolean getToken() {
        // 记录来拿令牌的时间
        long nowTime = getNowTime();
        // 添加令牌【判断该有多少个令牌】
        nowTokens = nowTokens + (int) ((nowTime - timestamp) * rate);
        // 判断添加以后的令牌数量与桶的容量哪个更小
        nowTokens = min(nowTokens);
        // 修改拿令牌的时间
        timestamp = nowTime;
        if (nowTokens < 1) {
            return false;
        } else {
            nowTokens -= 1;
            return true;
        }
    }
}
