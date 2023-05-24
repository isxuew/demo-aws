package com.example.demo.aws.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.amazonaws.services.lightsail.AmazonLightsail;
import com.amazonaws.services.lightsail.AmazonLightsailClient;
import com.amazonaws.services.lightsail.model.*;
import com.example.demo.aws.util.Env;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.function.BooleanSupplier;

@Slf4j
@Component
public class CheckIpScheduler {

    @Scheduled(fixedDelay = 1000L * 60)
    public void check() {

        DateTime date = DateUtil.date();
        if (DateUtil.between(date, Env.getRefreshTime(), DateUnit.MINUTE) < 5L) {
            return;
        }

        log.info("更新IP");

        String instanceName = "sg.2bigs.com";
        String staticIpName = DateUtil.format(date, "yyyyMMddHHmmss");

        AmazonLightsail amazonLightsail = AmazonLightsailClient.builder().withRegion("ap-southeast-1").build();

        GetStaticIpsResult getStaticIpsResult = amazonLightsail.getStaticIps(new GetStaticIpsRequest());
        if (CollUtil.isNotEmpty(getStaticIpsResult.getStaticIps())) {
            for (StaticIp staticIp : getStaticIpsResult.getStaticIps()) {
                if (StrUtil.equals(instanceName, staticIp.getAttachedTo())) {
                    amazonLightsail.releaseStaticIp(new ReleaseStaticIpRequest().withStaticIpName(staticIp.getName()));
                }
            }
        }

        GetInstanceRequest getInstanceRequest = new GetInstanceRequest().withInstanceName(instanceName);
        BooleanSupplier supplier = () -> {
            GetInstanceResult getInstanceResult = amazonLightsail.getInstance(getInstanceRequest);
            return getInstanceResult.getInstance().isStaticIp();
        };
        while (supplier.getAsBoolean()) {
            ThreadUtil.sleep(1000);
        }

        AllocateStaticIpRequest allocateStaticIpRequest = new AllocateStaticIpRequest().withStaticIpName(staticIpName);
        amazonLightsail.allocateStaticIp(allocateStaticIpRequest);

        AttachStaticIpRequest attachStaticIpRequest =
                new AttachStaticIpRequest().withStaticIpName(staticIpName).withInstanceName(instanceName);
        amazonLightsail.attachStaticIp(attachStaticIpRequest);

        Env.setRefreshTime();

        log.info("更新完成");
    }

}
