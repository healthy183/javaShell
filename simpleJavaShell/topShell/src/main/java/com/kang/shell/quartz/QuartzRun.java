package com.kang.shell.quartz;

import com.google.common.base.Throwables;
import com.kang.shell.common.AppendUtils;
import com.kang.shell.constants.TaskConstants;
import com.kang.shell.top.TopShell;
import com.kang.shell.top.TopVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Title 类名
 * @Description 描述
 * @Date 2017/9/5.
 * @Author Healthy
 * @Version
 */
@Component
@Slf4j
public class QuartzRun {
    @Autowired
    private TaskConstants taskConstants;
    @Autowired
    private TopShell topShell;

    public void runReport(){
        List<TopVO> topVOList = new ArrayList<TopVO>();
        Date date = new Date();
        String todayStr = DateFormatUtils.ISO_DATE_FORMAT.format(date);
        String dateTime =  FastDateFormat.getInstance("yyyy-MM-dd_HH_mm_ss").format(date);
        topShell.splitToFile(topVOList,taskConstants.getFilePath()+todayStr+".log");
        topShell.sortCPU(topVOList);
        AppendUtils appendUtils = AppendUtils.getInstance();
        topShell.reportCPU(topVOList,appendUtils);
        topShell.sortMEM(topVOList);
        topShell.reportMEN(topVOList,appendUtils);
        log.info(appendUtils.toString());
        try {
            org.apache.commons.io.FileUtils.writeStringToFile
                    (new File(taskConstants.getFilePath()+dateTime+"_report.txt"),
                            appendUtils.toString(), "utf-8");
        } catch (IOException e) {
            log.info(Throwables.getStackTraceAsString(e));
        }
    }
}
