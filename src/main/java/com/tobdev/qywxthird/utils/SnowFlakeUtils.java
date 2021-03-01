package com.tobdev.qywxthird.utils;

public class SnowFlakeUtils {
    //起始的时间戳
    private final static long START_STAMP = 1480166465631L;
    //每一部分占用的位数
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5; //机器标识占用的位数
    private final static long DATA_CENTER_BIT = 5;//数据中心占用的位数
    //每一部分的最大值
    private final static long MAX_DATA_CENTER_NUM = -1L ^ (-1L << DATA_CENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);
    //每一部分向左的位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;
    private long dataCenterId; //数据中心
    private long machineId; //机器标识
    private long sequence = 0L; //序列号
    private long lastStamp = -1L;//上一次时间戳

    public SnowFlakeUtils(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("dataCenterId can't be greaterthan MAX_DATA_CENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater thanMAX_MACHINE_NUM or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    /**
     * 生成订单前缀，26个字母随机俩位
     * @return
     */
    private String createPrefix() {
        String randomPrefix = "";
        for (int i = 0; i < 2; i++) {
            char c = (char) (Math.random() * 26 + 'A');
            randomPrefix += c;
        }
        System.out.println("randomPrefix======" + randomPrefix);
        return randomPrefix;
    }

    //产生订单号
    public synchronized String createOrderNo() {
        long currStamp = getNewStamp();
        if (currStamp < lastStamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }
        if (currStamp == lastStamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }
        lastStamp = currStamp;
        return "" + ((currStamp - START_STAMP) << TIMESTAMP_LEFT //时间戳部分
                | dataCenterId << DATA_CENTER_LEFT //数据中心部分
                | machineId << MACHINE_LEFT //机器标识部分
                | sequence); //序列号部分

    /*return createPrefix() + ((currStamp - START_STAMP) << TIMESTAMP_LEFT //时间戳部分
            | dataCenterId << DATA_CENTER_LEFT //数据中心部分
            | machineId << MACHINE_LEFT //机器标识部分
            | sequence);*/
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    /**
     * 生成新的时间戳
     * @return
     */
    private long getNewStamp() {
        return System.currentTimeMillis();
    }

}



