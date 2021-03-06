package com.kang.shell.top;

import com.google.common.base.Throwables;
import com.kang.shell.common.AppendUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title 类名
 * @Description 描述
 * @Date 2017/9/5.
 * @Author Healthy
 * @Version
 */
@Slf4j
public class TopShellTest extends BaseTest {

    @Autowired
    private TopShell topShell;

    @Test
    public void splitToFile(){
        String fileUrl  = "D:\\jixin\\cpu.txt";
        List<TopVO> topVOList = new ArrayList<TopVO>();
        topShell.splitToFile(topVOList,fileUrl);
        topShell.sortCPU(topVOList);
        AppendUtils appendUtils = AppendUtils.getInstance();
        topShell.reportCPU(topVOList,appendUtils);
        topShell.sortMEM(topVOList);
        topShell.reportMEN(topVOList,appendUtils);
        log.info(appendUtils.toString());
        try {
            org.apache.commons.io.FileUtils.writeStringToFile
                    (new File("D:\\jixin\\cpu_report.txt"),
                    appendUtils.toString(), "utf-8");
        } catch (IOException e) {
            log.info(Throwables.getStackTraceAsString(e));
        }
    }

    @Test
    public void testSplit(){
        String str = "12490 root 20 0 8771m 1.1g 11m S 0.0 6.7 119:39.87 java 2017-09-05_00:18:36";
        String[] arry = str.split("\\s+");
        for(int i =0;i<arry.length;i++){
            System.out.println(i +":"+ arry[i]);
        }
    }

}
