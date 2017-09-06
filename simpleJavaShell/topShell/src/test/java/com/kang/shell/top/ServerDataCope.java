package com.kang.shell.top;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDataCope {

    private  static final  String CPU_MAX_STR = "今天是%s,cpu在%s最高占比%s%%";
    private  static final  String MEM_MAX_STR = "今天是%s,内存在%s最高占比%s%%";
    private  static final  String CPU_MIN_STR = "今天是%s,cpu在%s最低占比%s%%";
    private  static final  String MEM_MIN_STR = "今天是%s,内存在%s最低占比%s%%";
    private static final SimpleDateFormat DATE_FORMAT =  new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETTIME_FORMAT =  new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
    private static final  ExecutorService  executorService =  Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        ServerDataCope serverData = new ServerDataCope();
        if(!(args != null && args.length== 2)){
            System.out.println("pidkey或者fileUrl为空!");
            return;
        }
        String pidKey = args[0];
        if(!serverData.isNotBlank(pidKey)){
            System.out.println("pidkey为空!");
            return;
        }
        String fileUrl = args[1];
        if(!serverData.isNotBlank(fileUrl)){
            System.out.println("fileUrl为空!");
            return;
        }
        serverData.writeReport(pidKey,fileUrl);
        serverData.readLogFile(fileUrl);
    }

    private void readLogFile(final String fileUrl){
        Runnable writeReportThread = new Runnable(){
            @Override
            public void run() {
                while (true) {
                    String todayStr = DATE_FORMAT.format(new Date());
                    String fileName = fileUrl + todayStr + ".log";
                    List<TopVO> topVOList = new ArrayList<TopVO>();
                    splitToFile(topVOList, fileName);
                    sortCPU(topVOList);
                    AppendUtils appendUtils = new AppendUtils();
                    reportCPU(topVOList, appendUtils, todayStr);
                    sortMEM(topVOList);
                    reportMEN(topVOList, appendUtils, todayStr);
                    System.out.println(appendUtils.toString());
                    String fileNameResult = fileUrl + todayStr + "_result.log";
                    File dateReusltLog = new File(fileNameResult);
                    Boolean createResult = createFile(new File(fileNameResult));
                    if (createResult) {
                        writeTxtFile(appendUtils.toString(), dateReusltLog);
                    } else {
                        System.out.println("创建文件" + fileName + "失败");
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            }
        };
        executorService.execute(writeReportThread);
    }

    private void writeReport(final String key,final String fileUrl){
        Runnable writeReportThread = new Runnable(){
            @Override
            public void run() {
                while(true){
                String pid = selectPid(key);
                if(isNotBlank(pid)){
                    String topInfo = selectPidIntop(pid);
                    System.out.println(topInfo);
                    String todayStr = DATE_FORMAT.format(new Date());
                    String fileName = fileUrl+todayStr+".log";
                    File dateLog = new File(fileName);
                    Boolean createResult = createFile(new File(fileName));
                    if(createResult){
                        writeTxtFile(topInfo,dateLog);
                    }else{
                        System.out.println("创建文件"+fileName+"失败");
                    }
                }
                try {
                    Thread.currentThread().sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            }
        };
        executorService.execute(writeReportThread);
    }


    /**
     * 根据参数key查找pid
     * @param key
     * @return
     */
    public String selectPid(String key){
        Process process;
        String pid = "";
        try {
            process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","ps -ef | grep "+key+" | grep java  | awk '{print $2}'"});
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
        return pid;
    }

    /**
     * 根据pid查找top日志
     * @param pid
     * @return
     */
    public String selectPidIntop(String pid){
        Process process;
        String info = "";
        try {
            process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","top -d 2 -n 1 -b | grep "+pid});
            try (InputStreamReader ir = new InputStreamReader(process.getInputStream())) {
                try (LineNumberReader input = new LineNumberReader(ir)) {
                    String line;
                    while ((line = input.readLine()) != null) {
                        info = line;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return info +" "+DATETTIME_FORMAT.format(new Date()) + "\r\n";
    }

    /**
     * 将top日志信息写入List
     * @param topVOList
     * @param fileUrl
     */
    public void splitToFile(List<TopVO> topVOList, String fileUrl){
        File file = new  File(fileUrl);
        if(!file.isFile() || !file.exists()){
            System.out.println("file "+fileUrl+" invalid or do not exits!");
            return;
        }
        String lineTxt = null;
        try{
            try(InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8")){
                try(BufferedReader bufferedReader = new BufferedReader(read)){
                    while ((lineTxt = bufferedReader.readLine()) != null) {
                        if(!isNotBlank(lineTxt)){
                            break;
                        }
                        String[] arry = lineTxt.split("\\s+");
                        if(arry.length != 13 || arry.length<13){
                             System.out.println("lineTxt "+lineTxt+" invalided!");
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
             System.out.println(e);
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

    /**
     * 集合
     * @param collection
     * @return
     */
    private boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public void reportCPU(List<TopVO> topVOList, AppendUtils appendUtils,String todayStr){
        if(!isEmpty(topVOList)){
            TopVO max = topVOList.get(0);
            String maxStr = String.format(CPU_MAX_STR,todayStr,max.getLogDate(),max.getCpuProportion());
            System.out.println(maxStr);
            appendUtils.append(maxStr);
            TopVO min = topVOList.get(topVOList.size()-1);
            String minStr = String.format(CPU_MIN_STR,todayStr,min.getLogDate(),min.getCpuProportion());
            appendUtils.append(minStr);
            System.out.println(minStr);
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

    /**
     * 报告内存情况
     * @param topVOList
     * @param appendUtils
     * @param todayStr
     */
    public void reportMEN(List<TopVO> topVOList, AppendUtils appendUtils,String todayStr){
        if(!isEmpty(topVOList)){
            //String todayStr = DATE_FORMAT.format(new Date());
            TopVO max = topVOList.get(0);
            String maxStr = String.format(MEM_MAX_STR,todayStr,max.getLogDate(),max.getMenProportion());
            appendUtils.append(maxStr);
             System.out.println(maxStr);
            TopVO min = topVOList.get(topVOList.size()-1);
            String minStr = String.format(MEM_MIN_STR,todayStr,min.getLogDate(),min.getMenProportion());
             System.out.println(minStr);
            appendUtils.append(minStr);
        }
    }

    /**
     * String是否为空字符串
     * @param str
     * @return
     */
    private Boolean isNotBlank(String str){
        return str != null && !(str.trim().equals(""));
    }

    private static boolean createFile(File fileName){
        boolean flag=true;
        try{
            if (!fileName.getParentFile().exists()){
                fileName.getParentFile().mkdirs();
            }
            if(!fileName.exists()){
                fileName.createNewFile();
                flag=true;
            }
        }catch(Exception e){
            System.out.println(e);
            flag=false;
        }
        return flag;
    }

    public static boolean writeTxtFile(String content,File fileName){
        boolean flag=false;
        try {
           try(Writer writer = new FileWriter(fileName,true)){
               writer.write(content);
               flag=true;
           }
        } catch (Exception e) {
            System.out.println(e);
        }
        return flag;
    }


    public  class AppendUtils {
        private StringBuilder stringBuilder;
        public  AppendUtils(){
            this.stringBuilder = new StringBuilder();
        }

        public void  append(String str){
            stringBuilder.append(str+"\r\n");
        }

        @Override
        public String toString() {
            return "打印时间:"+DATETTIME_FORMAT.format(new Date())+"\r\n"+stringBuilder.toString()+"\r\n";
        }
    }

    public class TopVO implements java.io.Serializable {

        private Integer pid;

        private String userName;

        private Double cpuProportion;

        private Double menProportion;

        private String courseType;

        private String logDate;

        public Integer getPid() {
            return pid;
        }

        public void setPid(Integer pid) {
            this.pid = pid;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Double getCpuProportion() {
            return cpuProportion;
        }

        public void setCpuProportion(Double cpuProportion) {
            this.cpuProportion = cpuProportion;
        }

        public Double getMenProportion() {
            return menProportion;
        }

        public void setMenProportion(Double menProportion) {
            this.menProportion = menProportion;
        }

        public String getCourseType() {
            return courseType;
        }

        public void setCourseType(String courseType) {
            this.courseType = courseType;
        }

        public String getLogDate() {
            return logDate;
        }

        public void setLogDate(String logDate) {
            this.logDate = logDate;
        }

        @Override
        public String toString() {
            return "TopVO{" +
                    "pid=" + pid +
                    ", userName='" + userName + '\'' +
                    ", cpuProportion=" + cpuProportion +
                    ", menProportion=" + menProportion +
                    ", courseType='" + courseType + '\'' +
                    ", logDate='" + logDate + '\'' +
                    '}';
        }
    }
}