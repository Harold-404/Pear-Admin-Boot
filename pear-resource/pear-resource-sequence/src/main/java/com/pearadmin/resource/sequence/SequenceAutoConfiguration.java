package com.pearadmin.resource.sequence;

import com.pearadmin.resource.sequence.pool.SequencePool;
import com.pearadmin.resource.sequence.pool.SequencePoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 分 布 式 Id 自 动 配 置 --[就眠仪式]
 */
@Slf4j
@Configuration
@ConditionalOnClass(SequencePool.class)
@EnableConfigurationProperties(SequenceAutoProperties.class)
public class SequenceAutoConfiguration {

    /**
     * 引 用 映 射 配 置 属 性
     */
    @Resource
    private SequenceAutoProperties sequenceAutoProperties;

    /**
     * 初 始 化 分 布 式 Id 工 厂,并 交 由 Spring IOC 托 管
     */
    @Bean
    public SequencePool sequencePool(SequencePoolConfig sequencePoolConfig){
        try {
            SequencePool sequencePool = new SequencePool(sequencePoolConfig);

            sequencePool.init();

            log.info("剩 余 可 用 : " + sequencePool.getCount() + " Sequence ID");

            return sequencePool;

        }catch (Exception e){

            e.printStackTrace();
        }
        return null;
    }

    /**
     * 配 置 文 件 初 始 化
     * */
    @Bean
    public SequencePoolConfig sequencePoolConfig(){

        SequencePoolConfig config = new SequencePoolConfig();

        config.setCenterId(sequenceAutoProperties.getCenterId());
        config.setWorkerId(sequenceAutoProperties.getWorkerId());
        config.setMinIdle(sequenceAutoProperties.getMinIdle());
        config.setInitSize(sequenceAutoProperties.getInitSize());

        log.info("Read sequence configuration information");
        log.info("组 件 名 称 : 分布式 ID 工厂");
        log.info("初 始 容 量 : " + sequenceAutoProperties.getInitSize());
        log.info("最 小 闲 置 : " + sequenceAutoProperties.getMinIdle());
        log.info("机 器 编 号 : " + sequenceAutoProperties.getWorkerId());
        log.info("数 据 中 心 : " + sequenceAutoProperties.getCenterId());
        return config;
    }

}
