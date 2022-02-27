package edu.cuit.lushan.config;

import edu.cuit.lushan.config.PerformanceInterceptor;
import edu.cuit.lushan.handler.BaseDataLog;
import edu.cuit.lushan.handler.DataUpdateInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * Mybatis-Plus配置
 * </p>
 */
@Configuration
@EnableTransactionManagement
@MapperScan("edu.cuit.lushan.mapper")
public class MybatisPlusConfig {

    /**
     * <p>
     * SQL执行效率插件  设置 dev test 环境开启
     * </p>
     */
    @Bean
    @Profile({"dev", "test"})
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * <p>
     * 数据更新操作处理
     * </p>
     *
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(BaseDataLog.class)
    public DataUpdateInterceptor dataUpdateInterceptor() {
        return new DataUpdateInterceptor();
    }
}
