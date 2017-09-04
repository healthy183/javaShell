package com.kang.shell.top;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @Title 类名
 * @Description 描述
 * @Date 2017/9/5.
 * @Author Healthy
 * @Version
 */
@Slf4j
public class TopShellTest extends BaseTest {

    @Test
    public void splitToFile(){
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
