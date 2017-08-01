package com.wfc.web.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;

import java.util.Random;

public class SecurityUtils {

    static Random RAND = new Random();


    public static int decrypt(String s) {
        if(StringUtils.isNotBlank(s) && s.length() > 14) {
            String de = s.substring(7, s.length() - 7);
            System.out.println(de);
            if(StringUtils.isNumeric(de)) {
                int d = Integer.parseInt(de);
                return d / 308 + 221 - 1105;
            }
        }
        return 0;
    }

    public static String encrypt(int sharedId) {
        String encryptionId;
        int n1 = 10000+RAND.nextInt(9990000);
        int n2 = 10000+RAND.nextInt(9990000);

        encryptionId = String.valueOf(n1) +
                (sharedId + 1105 - 221) * 308 +
                String.valueOf(n2);
        return encryptionId;
    }

    public static long decrypt1(long rank) {
        //解码
        rank = rank & 17592186044415L ^ 589520242836L;
        long px2 = ((rank ^ 32430011) & 33554431) << 19;
        px2 |= (rank ^ 17002634674176L) >> 25;
        rank = px2 ^ 17002665801579L;//rank的结果是原文1611100000020
        return rank;
    }

    public static long encrypt1(long rank) {
        //编码
//        long rank = 1611100000020;//输入rank， 不能大于17592186044415,不能小于0
        rank = rank & 17592186044415L ^ 17002665801579L;
        long px1 = rank >> 19 ^ 32430011;
        px1 |= (rank & 524287) << 25 ^ 17002634674176L;
        rank = px1 ^ 589520242836L;
        return rank;
    }

}
