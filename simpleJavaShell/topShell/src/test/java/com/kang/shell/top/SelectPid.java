package com.kang.shell.top;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @Title 类名
 * @Description 描述
 * @Date 2017/9/6.
 * @Author Healthy
 * @Version
 */
public class SelectPid {

    public static void main(String[] args) {
            Process process;
            String key = "trade-web";
            String pid = "";
            try {
                //process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","ps -ef | grep "+key+" | grep java  | awk '{print $2}'"});
                process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","ps -ef | grep "+key+" | grep java"});
                try (InputStreamReader ir = new InputStreamReader(process.getInputStream())) {
                    try (LineNumberReader input = new LineNumberReader(ir)) {
                        String line;
                        while ((line = input.readLine()) != null) {
                            System.out.println("pid result:"+line);
                            pid = line;
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
}
