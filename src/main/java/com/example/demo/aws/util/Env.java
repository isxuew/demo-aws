package com.example.demo.aws.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Env {

    public static DateTime getRefreshTime() {
        return refreshTime;
    }

    public static void setRefreshTime() {
        log.info("重置计时");
        Env.refreshTime = DateUtil.date();
    }

    private static DateTime refreshTime = DateUtil.date();

}
