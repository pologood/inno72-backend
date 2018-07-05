package com.inno72.util;

import java.util.Random;

/**
 * @Auther: wxt
 * @Date: 2018/7/3 10:49
 * @Description:随机生成机器Id
 */
public class GenerateMachineIdUtil {

        private static final String NUMBER_CHAR = "0123456789";

        public static String generateNumberString(int length){
            StringBuffer sb = new StringBuffer();
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                sb.append(NUMBER_CHAR.charAt(random.nextInt(NUMBER_CHAR.length())));
            }
            return sb.toString();
        }



    /*public static void main(String[] args) throws Exception {
        for(int i = 0;i <= 2000;i++){
           String time = generateNumberString(10);

            System.out.println(" " + time);
        }

    }*/

}

