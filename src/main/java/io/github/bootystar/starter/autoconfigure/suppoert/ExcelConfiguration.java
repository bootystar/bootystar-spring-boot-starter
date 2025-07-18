package io.github.bootystar.starter.autoconfigure.suppoert;

import io.github.bootystar.starter.excel.easyexcel.EasyExcelConverterRegister;
import io.github.bootystar.starter.excel.fastexcel.FastExcelConverterRegister;
import io.github.bootystar.starter.prop.BootystarProperties;
import io.github.bootystar.starter.prop.support.ExcelProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * excel配置
 * @author bootystar
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "bootystar.excel", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ExcelConfiguration implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BootystarProperties properties = applicationContext.getBean(BootystarProperties.class);
        ExcelProperties excel = properties.getExcel();
        if (excel.isInitFastExcelConverter()) {
            try {
                Class.forName("cn.idev.excel.FastExcel");
                FastExcelConverterRegister.registerConverters(properties);
                log.debug("FastExcelConverterRegister init success");
            } catch (ClassNotFoundException e) {
                log.debug("not class found , FastExcelConverterRegister won't work");
            } catch (Exception e) {
                log.debug("FastExcelConverterRegister init failed", e);
            }
        }
        if (excel.isInitEasyExcelConverter()) {
            try {
                Class.forName("com.alibaba.excel.EasyExcel");
                EasyExcelConverterRegister.registerConverters(properties);
                log.debug("EasyExcelConverterRegister init success");
            } catch (ClassNotFoundException e) {
                log.debug("not class found , EasyExcelConverterRegister won't work");
            } catch (Exception e) {
                log.debug("EasyExcelConverterRegister init failed", e);
            }
        }

    }

}
