package com.coupon.core.utils;

import com.coupon.core.common.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.common.utils
 * @ClassName: TbkUtils
 * @Description: 淘宝客工具类
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/18/018 16:48
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/18/018 16:48
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TbkUtils {

    /*在Java中，不管是String.split()，还是正则表达式，有一些特殊字符需要转义，需要转义的字符有：
            (    [     {    /    ^    -    $     ¦     }    ]    )    ?    *    +    . 
      转义方法为字符前面加上"\\"，这样在split、replaceAll时就不会报错了；
      但是有一点需要注意，String.contains()方法是不需要转义的。*/
    //public static final String[] tempStr = {"(", ")", "[", "]", "{", "}" , "+", "/", "|", "-", "?", "\\\\", "^", ".", "*", "$", "¦"};
    public static final String[] tempStr = {"[", "]", "{", "}" , "+", "|", "-", "?", "^", "*", "$", "¦"};

    /**
     * 判断是否是汉字
     * @param achar
     * @return
     */
    //String.valueOf(c).matches("[\u4e00-\u9fa5]");
    public static boolean isChinese(char achar) {
        boolean matches = String.valueOf(achar).matches("[\u4e00-\u9fa5]");
        return matches;
    }

    /**
     * 数字字母
     * @param achar
     * @return
     */
    public static boolean isNumWord(char achar) {
        boolean matches = String.valueOf(achar).matches("[0-9a-zA-Z]");
        return matches;
    }

    /**
     * 数字字母
     * @param achar
     * @return
     */
    public static boolean isCustomize(char achar) {
        String str = String.valueOf(achar);
        if(" ".equals(str) || "/".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是特殊字符
     * @param str
     * @return
     */
    public static boolean isSpecial(String str) {
        for (int i = 0; i < tempStr.length; i++) {
            if(str.equals(tempStr[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是淘口令格式字符串
     * @param str
     * @return
     */
    public static boolean isCode(String str) {
        String regex = "[a-zA-Z0-9]{10,11}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 提取淘口令
     * @param textInfo
     * @return
     */
    public static String extractTbCode(String textInfo) {
        Map<Character,Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < textInfo.length(); i++) {
            char c = textInfo.charAt(i);
            if(!isChinese(c) && !isNumWord(c) && !isCustomize(c)) {
                if(map.containsKey(c)){
                    int val = map.get(c)+1;
                    map.put(c, val);
                }else{
                    map.put(c, 1);
                }
            }
        }
        for (Character chars : map.keySet()) {
            Integer integer = map.get(chars);
            if(integer > 1) {
                String charStr = String.valueOf(chars);
                String splitStr = charStr;
                if(isSpecial(splitStr)) {
                    splitStr = "\\"+splitStr;
                }
                String[] split = textInfo.split(splitStr.toString());
                for (String s : split) {
                    boolean code = isCode(s);
                    if(code) {
                        return charStr+s+charStr;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 判断文字信息是否是淘宝类链接
     * @param text
     * @return
     */
    public static boolean isTaobaoLink(String text) {
        //（1）淘宝产品id：比如 578327349261(后期有必要在家id直接查询) 或直接产品链接 //item.taobao.com/item.htm?id=578327349261
        //（2）二合一链接：比如 https://uland.taobao.com/coupon/edetail?e=...
        //（3）s.click.taobao.com 长和短链接，比如 https://s.click.taobao.com/CM8azwL
        //（4）天猫的喵口令 如 http://zmnxbc.com/s/ZAa8Z
        //（5）手淘APP的分享链接：http://m.tb.cn/h.ZAa8Z
        if(text.contains(Constants.URL_TYPE_ITEM_LINK) || text.contains(Constants.URL_TYPE_TMALL_ITEM_LINK)
                || text.contains(Constants.URL_TYPE_LONG_SHORT_LINK) ||text.contains(Constants.URL_TYPE_MOBILE_SHARE_LINK)
                || text.contains(Constants.URL_TYPE_TMALL_CAT_LINK) ||text.contains(Constants.URL_TYPE_TWO_ONE_LINK)) {
            return true;
        }
        return false;
    }

    /**
     * 判断文字信息是否是京东类分享链接
     * @param text
     * @return
     */
    public static boolean isJdLink(String text) {
        if(text.contains(Constants.JD_WEB_SHARE_URL) || text.contains(Constants.JD_MOBILE_SHARE_URL)) {
            return true;
        }
        return false;
    }

    /**
     * 提取京东商品id
     * @param text
     * @return
     */
    public static String extractJdItemId(String text) {
        String temp = text.substring(0, text.indexOf(".html"));
        String itemId = temp.substring(temp.lastIndexOf("/")+1);
        if(itemId != null) {
            return itemId;
        }else {
            return null;
        }
    }

    /**
     * 生成11位随机数工具
     * @return
     */
    public static String randomTag () {
        //生成随机数字和字母,
        String val = "";
        Random random = new Random();
        //length为几位密码
        for(int i = 0; i < 11; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符,最长35个字符长度
     * @param str
     * @return
     */
    public static String extractKeyword(String str) {
        String dest = "";
        if (str!=null) {
            dest = str.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "");  //去除数字，英文，汉字  之外的内容
            //最多返回长度40
            if(dest.length() > 35) {
                dest = dest.substring(0, 35);
            }
        }
        return dest;
    }
}
