package com.kang.shell.top;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @Title 类名
 * @Description 描述
 * @Date 2017/9/5.
 * @Author Healthy
 * @Version
 */
//@Slf4j
public class ServerData {

    public static void main(String[] args) {
        ServerData serverData = new ServerData();
        String pid = serverData.selectPid("trade-web");
        System.out.println("pid is"+pid);
        if(StringUtils.isNotBlank(pid)){
            String topInfo = serverData.selectPidIntop(pid);
            System.out.println(topInfo);
        }
    }

    public String selectPid(String key){
        Process process;
        String pid = "";
        try {
            process = Runtime.getRuntime().exec("$(ps -ef | grep "+key+" | grep java  | awk '{print $2}')");
            try (InputStreamReader ir = new InputStreamReader(process.getInputStream())) {
                try (LineNumberReader input = new LineNumberReader(ir)) {
                    String line;
                    while ((line = input.readLine()) != null) {
                        pid = line;
                    }
                }
            }
        } catch (IOException e) {
            //log.info(Throwables.getStackTraceAsString(e));
            System.out.println(e);
        }
        return pid;
    }

    public String selectPidIntop(String pid){
        Process process;
        String info = "";
        try {
            process = Runtime.getRuntime().exec("$(top -d 2 -n 1 -b | grep "+pid+")");
            try (InputStreamReader ir = new InputStreamReader(process.getInputStream())) {
                try (LineNumberReader input = new LineNumberReader(ir)) {
                    String line;
                    while ((line = input.readLine()) != null) {
                        info = line;
                    }
                }
            }
        } catch (IOException e) {
            //log.info(Throwables.getStackTraceAsString(e));
            System.out.println(e);
        }
        return info;
    }

    @Deprecated
    public String getServerTopCommandContent() {
        Process process;
        String allcontent = "";
        try {
            process = Runtime.getRuntime().exec("top -b -n 1");
            try (InputStreamReader ir = new InputStreamReader(process.getInputStream())) {
                try (LineNumberReader input = new LineNumberReader(ir)) {
                    String line;
                    while ((line = input.readLine()) != null) {
                        //System.out.println(line);
                        line = line + "<br/>";
                        allcontent += line;
                    }
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println(e);
        }
        return null;
    }

}
