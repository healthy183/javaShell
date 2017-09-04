package com.kang.shell.top;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
public class TopShell {

    public void splitToFile(List<TopVO> topVOList, String fileUrl){
        File file = new  File(fileUrl);
        if(!file.isFile() || !file.exists()){
            log.error("file {} invalid or do not exits!");
            return;
        }
        String lineTxt = null;
        try{
            try(InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8")){
                try(BufferedReader bufferedReader = new BufferedReader(read)){
                    while ((lineTxt = bufferedReader.readLine()) != null) {
                        if(StringUtils.isBlank(lineTxt)){
                            break;
                        }
                        String[] arry = lineTxt.split("\\s+");
                        if(arry.length != 13 || arry.length<13){
                            log.info("lineTxt {} invalided!",lineTxt);
                            continue;
                        }
                        TopVO topVO = new TopVO();
                        topVO.setPid(Integer.valueOf(arry[0]));
                        topVO.setUserName(arry[1]);
                        topVO.setCpuProportion(Double.valueOf(arry[8]));
                        topVO.setMenProportion(Double.valueOf(arry[9]));
                        topVO.setCourseType(arry[11]);
                        topVO.setLogDate(arry[12]);
                        topVOList.add(topVO);
                    }
                }
            }
        }catch(Exception e){

        }






    }


}
