package com.kang.shell.top;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ServerNet {
    private static final Integer TIMEOUT_COUNT = 1;
    private static final  String RESULT_URL = "RESULT_URL";
    private static final  String TIME_TOTAL ="curl -o /dev/null -s -m "+TIMEOUT_COUNT+" --connect-timeout "+TIMEOUT_COUNT+" -w %{TIME_TOTAL} \"https://api.tycredit.com/credit-front-http/unified/threeElementsCheckSim.json\"";
    private static final  String TIME_TOTAL_All ="curl -o /dev/null -s -m "+TIMEOUT_COUNT+" --connect-timeout "+TIMEOUT_COUNT+" -w %{http_code}:%{time_connect}:%{time_starttransfer}:%{time_total} \""+RESULT_URL+"\"";
    private static final Map<String,String> channelMap = new LinkedHashMap<String,String>();
    static{
        //channelMap = new HashMap<String,String>();
        channelMap.put("天翼征信二要素","https://api.tycredit.com/credit-front-http/unified/twoElements.json");
        channelMap.put("天翼征信三要素","https://api.tycredit.com/credit-front-http/unified/threeElementsCheckSim.json");
        channelMap.put("云码通精确三要素","http://10.79.29.1:18080/exact/bankcard/3/authentication/json");
        channelMap.put("云码通精确四要素","http://10.79.29.1:18080/exact/bankcard/4/authentication/json");
        channelMap.put("深银联易办事","https://168.33.39.220:8843/authcom/service/authentication");
        channelMap.put("厦门银联","http://api.amicloud.cn:8892/v1.0/realname/");
        channelMap.put("金运通","https://www.jytpay.com:9210/JytAuth/tranCenter/authReq.do");
    }
    public static void main(String[] args) {
        ServerNet serverNet = new ServerNet();
        SimpleDateFormat date_format =  new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
        SimpleDateFormat select_format =  new SimpleDateFormat("yyyy-MM-dd-HH_mm_ss");
        while(true){
            System.out.println(select_format.format(new Date())+",检查时间:"+date_format.format(new Date()));
            serverNet.executeCurl();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println("");
        }
    }

    public  void executeCurl(){
        Process process;
        String curlResult = "";
        Set<Map.Entry<String,String>> channelSet =  channelMap.entrySet();
        for(Map.Entry<String,String> urlEntry : channelSet){
            String statements = TIME_TOTAL_All.replace(RESULT_URL,urlEntry.getValue());
            try {
                process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",statements});
                try (InputStreamReader ir = new InputStreamReader(process.getInputStream())) {
                    try (LineNumberReader input = new LineNumberReader(ir)) {
                        String line;
                        while ((line = input.readLine()) != null) {
                            // System.out.println(line);
                            curlResult = "curl "+ urlEntry.getKey()+":"+urlEntry.getValue()+" result:"+line;

                            System.out.println(curlResult);
                            if(isNotBlank(line)){
                                String[] resultArray = line.split("\\:");
                                if(resultArray.length == 4){
                                    String format = "返回结果:"+resultArray[0]+",建立tcp耗时:"+resultArray[1]
                                            +",发送curl后收到服务器返回耗时:"+resultArray[2]
                                            +",总耗时:"+resultArray[3];
                                    System.out.println(format);
                                }
                            }
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private Boolean isNotBlank(String str){
        return str != null && !(str.trim().equals(""));
    }
}