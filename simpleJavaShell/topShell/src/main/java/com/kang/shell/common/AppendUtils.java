package com.kang.shell.common;

/**
 * @Title 类名
 * @Description 描述
 * @Date 2017/9/5.
 * @Author Healthy
 * @Version
 */
public class AppendUtils {

    private StringBuilder stringBuilder;
    private  AppendUtils(StringBuilder stringBuilder){
        this.stringBuilder = stringBuilder;
    }

    public static  AppendUtils  getInstance(){
        return new AppendUtils(new StringBuilder());
    }

    public void  append(String str){
        stringBuilder.append(str+"\n\r");
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
