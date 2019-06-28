package com.coupon.core.common;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.common
 * @ClassName: Constants
 * @Description: 常量值
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/18/018 9:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/18/018 9:45
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface Constants {
    //===================== 请求相关参数定义 ===================================================================
    String CONTEXT_PATH = "ctx"; //当前项目根地址
    String BACK_URL = "backURL"; //请求的上一次的地址
    String CURRENT_FIELDS = "fields";
    String NO_QUERYSTRING_CURRENT_URL = "noQueryStringCurrentURL";

    //===================== 时间类型参数定义参数定义 =============================================================
    String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    String YMD_HM = "yyyy-MM-dd HH:mm";
    String YMD = "yyyy-MM-dd";
    String YM = "yyyy-MM";
    String YMD_HMS_CN = "yyyy年MM月dd日 HH时:mm分:ss秒";
    String YMD_HM_CN = "yyyy年MM月dd日 HH时:mm分";
    String YMD_CN = "yyyy年MM月dd日";
    String YM_CN = "yyyy年MM月";

    //===================== 服务端接口数据校验正则表达式 ==========================================================
    /**
     * 手机号
     */
    String REGEX_PHONE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";

    /**
     * 数字和字母
     */
    String REGEX_NUM_LATTER = "^[a-zA-Z0-9]*$";

    /**
     * 数字和字母(6-20)
     */
    String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";

    /**
     * 汉字
     */
    String REGEX_CHINESE = "^[\u4e00-\u9fa5]*$";

    /**
     * 身份证号
     */
    String REGEX_IDENTITY_CARD_NUM = "^(\\d{6})(18|19|20)?(\\d{2})([01]\\d)([0123]\\d)(\\d{3})(\\d|X|x)?$";

    /**
     * 非0正整数
     */
    String REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$";

    /**
     * 正数
     */
    String REGEX_POSITIVE_DOUBLE = "^[+]?[.\\d]*$";


    //===================== 图灵机器人 ============================================================================
    //图灵机器人接口调用地址
    String TULING_URL = "http://www.tuling123.com/openapi/api";
    //图灵机器人apikey
    String TULING_APIKEY = "42f52b0939ae4a159da5bdcd9988036e";

    //===================== 淘宝/喵有劵 ============================================================================
    //喵有劵对接秘钥
    String MYJ_APKEY = "177982ef-f5bc-0b00-b2e9-2430763ffd67";

    //推广位ID（*必须是授权淘宝号下的推广位）
    String PID = "mm_319880138_330050252_92359200165";

    //在喵有劵授权登录后的淘宝名称
    String TBNAME = "wenleigang834147811";

    //===================== 淘宝客api详细接口 ========================================================================
    //淘宝客API请求地址
    String TBK_URL = "https://api.open.21ds.cn/apiv1";

    //高佣转链接API(淘口令)
    String GY_TPWD = TBK_URL+"/getitemgyurlbytpwd?apkey="+MYJ_APKEY+"&pid="+PID+"&tbname="+TBNAME+"&shorturl=1&tpwd=1&tpwdcode=";

    //获取简版淘客商品信息API
    String ITEM_INFO = TBK_URL+"/getiteminfo?apkey="+MYJ_APKEY+"&itemid=";

    //万能高佣转链API接口
    String GY_ALL = TBK_URL+"/getgyurlbyall?apkey="+MYJ_APKEY+"&pid="+PID+"&tbname="+TBNAME+"&shorturl=1&tpwd=1&content=";

    //===================== 其他格式需要接口查询优惠券 ===================================================================
    //（1）产品链接 //item.taobao.com/item.htm?id=578327349261
    String URL_TYPE_ITEM_LINK = "item.taobao.com";
    String URL_TYPE_TMALL_ITEM_LINK = "detail.tmall.com";

    // （2）二合一链接：比如 https://uland.taobao.com/coupon/edetail?e=...
    String URL_TYPE_TWO_ONE_LINK = "uland.taobao.com";

    //（3）s.click.taobao.com 长和短链接，比如 https://s.click.taobao.com/CM8azwL
    String URL_TYPE_LONG_SHORT_LINK = "s.click.taobao.com";

    //（4）天猫的喵口令 如 http://zmnxbc.com/s/ZAa8Z
    String URL_TYPE_TMALL_CAT_LINK = "zmnxbc.com";

    //（5）手淘APP的分享链接：http://m.tb.cn/h.ZAa8Z
    String URL_TYPE_MOBILE_SHARE_LINK = "m.tb.cn";
}
