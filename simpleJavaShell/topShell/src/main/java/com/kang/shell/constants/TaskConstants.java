package com.kang.shell.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Title 类名
 * @Description 描述
 * @Date 2017/9/5.
 * @Author Healthy
 * @Version
 */
@Component
@Data
@PropertySource(value = "classpath:properties/application.properties")
public class TaskConstants {

    @Value("${runReport}")
    private String runReport;
    @Value("${filePath}")
    private String filePath;
}
