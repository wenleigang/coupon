package com.coupon.core.utils;

import com.coupon.core.common.Constants;
import org.apache.commons.lang3.StringUtils;

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

    /**
     * 判断文字中是否包含淘口令
     * @param textInfo
     * @return
     */
    public static String hasTpwdcode(String textInfo) {
        String regex1 = "\\￥([a-zA-Z0-9]{11})\\￥";
        String regex2 = "\\€([a-zA-Z0-9]{11})\\€";
        String regex3 = "\\$([a-zA-Z0-9]{11})\\$";
        String regex4 = "\\&([a-zA-Z0-9]{11})\\&";
        String regex5 = "\\₤([a-zA-Z0-9]{11})\\₤";
        Pattern p1 = Pattern.compile(regex1);
        Matcher m1 = p1.matcher(textInfo);
        if(m1.find()) {
            return "￥";
        }
        Pattern p2 = Pattern.compile(regex2);
        Matcher m2 = p2.matcher(textInfo);
        if(m2.find()) {
            return "€";
        }
        Pattern p3 = Pattern.compile(regex3);
        Matcher m3 = p3.matcher(textInfo);
        if(m3.find()) {
            return "$";
        }
        Pattern p4 = Pattern.compile(regex4);
        Matcher m4 = p4.matcher(textInfo);
        if(m4.find()) {
            return "&";
        }
        Pattern p5 = Pattern.compile(regex5);
        Matcher m5 = p5.matcher(textInfo);
        if(m5.find()) {
            return "₤";
        }
        return null;
    }

    /**
     * 判断文字是淘口令
     * @param textInfo
     * @return
     */
    public static String isTpwdcode(String textInfo) {
        String regex1 = "\\￥([a-zA-Z0-9]{11})\\￥";
        String regex2 = "\\€([a-zA-Z0-9]{11})\\€";
        String regex3 = "\\$([a-zA-Z0-9]{11})\\$";
        String regex4 = "\\&([a-zA-Z0-9]{11})\\&";
        String regex5 = "\\₤([a-zA-Z0-9]{11})\\₤";
        Pattern p1 = Pattern.compile(regex1);
        Matcher m1 = p1.matcher(textInfo);
        if(m1.matches()) {
            return "￥";
        }
        Pattern p2 = Pattern.compile(regex2);
        Matcher m2 = p2.matcher(textInfo);
        if(m2.matches()) {
            return "€";
        }
        Pattern p3 = Pattern.compile(regex3);
        Matcher m3 = p3.matcher(textInfo);
        if(m3.matches()) {
            return "$";
        }
        Pattern p4 = Pattern.compile(regex4);
        Matcher m4 = p4.matcher(textInfo);
        if(m4.matches()) {
            return "&";
        }
        Pattern p5 = Pattern.compile(regex5);
        Matcher m5 = p5.matcher(textInfo);
        if(m5.find()) {
            return "₤";
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
     * 商品信息描述文字中提取淘口令
     * @param itemShareInfo
     * @return
     */
    public static String extractTpwdcode(String itemShareInfo) {
        String tpwdcode = "";
        if(StringUtils.isNotBlank(itemShareInfo)) {
            String pattern = hasTpwdcode(itemShareInfo);
            String splitTag = pattern;
            //特殊符号转义
            if(pattern == "$") {
                splitTag = "\\$";
            }
            if(pattern != null) {
                tpwdcode = pattern+itemShareInfo.split(splitTag)[1]+pattern;
            }
        }
        return tpwdcode;
    }

    /**
     * 商品描述信息中提取商品标题title
     * @param itemShareInfo
     * @return
     */
    public static String extractItemTitle(String itemShareInfo) {
        String itemTitle = "";
        if(StringUtils.isNotBlank(itemShareInfo)) {
            if(StringUtils.isNotBlank(itemShareInfo)) {
                itemTitle = itemShareInfo.substring(itemShareInfo.indexOf("https"));
            }
        }
        return itemTitle;
    }

}
