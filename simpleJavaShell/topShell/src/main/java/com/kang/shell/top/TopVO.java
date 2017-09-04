package com.kang.shell.top;

import lombok.Data;
import lombok.ToString;

/**
 * @Title 类名
 * @Description 描述
 * @Date 2017/9/5.
 * @Author Healthy
 * @Version
 */
@Data
@ToString
public class TopVO implements java.io.Serializable {

    private Integer pid;

    private String userName;

    private Double cpuProportion;

    private Double menProportion;

    private String courseType;

    private String logDate;
}
