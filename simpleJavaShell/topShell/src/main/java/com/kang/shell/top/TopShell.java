package com.kang.shell.top;

import com.google.common.base.Throwables;
import com.kang.shell.common.AppendUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
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
public class TopShell {

    private  static final  String CPU_MAX_STR = "今天是%s,cpu在%s最高占比%s%%";
    private  static final  String MEM_MAX_STR = "今天是%s,内存在%s最高占比%s%%";
    private  static final  String CPU_MIN_STR = "今天是%s,cpu在%s最低占比%s%%";
    private  static final  String MEM_MIN_STR = "今天是%s,内存在%s最低占比%s%%";

    public void splitToFile(List<TopVO> topVOList, String fileUrl){
        File file = new  File(fileUrl);
        if(!file.isFile() || !file.exists()){
            log.error("file {} invalid or do not exits!",fileUrl);
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
            log.info(Throwables.getStackTraceAsString(e));
        }
    }

    /**
     *cpu排序
     * @param topVOList
     */
    public void sortCPU(List<TopVO> topVOList){
        Collections.sort(topVOList, new Comparator<TopVO>() {
            public int compare(TopVO o1, TopVO o2) {
                return o2.getCpuProportion().compareTo(o1.getCpuProportion());
            }
        });
    }


    public void reportCPU(List<TopVO> topVOList, AppendUtils appendUtils){
        if(!org.springframework.util.CollectionUtils.isEmpty(topVOList)){
            String todayStr = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
            TopVO max = topVOList.get(0);
            String maxStr = String.format(CPU_MAX_STR,todayStr,max.getLogDate(),max.getCpuProportion());
            log.info(maxStr);
            appendUtils.append(maxStr);
            TopVO min = topVOList.get(topVOList.size()-1);
            String minStr = String.format(CPU_MIN_STR,todayStr,min.getLogDate(),min.getCpuProportion());
            appendUtils.append(minStr);
            log.info(minStr);
        }
    }
    /**
     * 内存排序
     * @param topVOList
     */
    public void sortMEM(List<TopVO> topVOList){
        Collections.sort(topVOList, new Comparator<TopVO>() {
            public int compare(TopVO o1, TopVO o2) {
                return o2.getMenProportion().compareTo(o1.getMenProportion());
            }
        });
    }

    public void reportMEN(List<TopVO> topVOList, AppendUtils appendUtils){
        if(!org.springframework.util.CollectionUtils.isEmpty(topVOList)){
            String todayStr = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
            TopVO max = topVOList.get(0);
            String maxStr = String.format(MEM_MAX_STR,todayStr,max.getLogDate(),max.getMenProportion());
            appendUtils.append(maxStr);
            log.info(maxStr);
            TopVO min = topVOList.get(topVOList.size()-1);
            String minStr = String.format(MEM_MIN_STR,todayStr,min.getLogDate(),min.getMenProportion());
            log.info(minStr);
            appendUtils.append(minStr);
        }
    }
}
