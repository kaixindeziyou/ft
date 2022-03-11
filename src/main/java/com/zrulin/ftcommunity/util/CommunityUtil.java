package com.zrulin.ftcommunity.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author zrulin
 * @create 2022-03-11 8:19
 */
public class CommunityUtil {
    //生成随机字符串
    public static String generateUUID(){
        //UUID是java自带的类，然后replace是因为生成的随机字符串里面会有-。
        return UUID.randomUUID().toString().replace("-","");
    }
    // MD5加密
    //hello -> abc123def456
    //hello + s32fd3 -> abc123def456dsj23
    public static String md5(String key){
        //commons.lang 的jar包的方法，这个方法会判定，如果key是null，“”，空格，都认为是空的。非常方便
        if(StringUtils.isBlank(key)){
            return null;
        }
        //DigestUtils是spring自带的一个工具,方法的意思是，把你传的结果加密成一个16进制的字符串返回。
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
