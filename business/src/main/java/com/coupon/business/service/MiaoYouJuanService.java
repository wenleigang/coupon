package com.coupon.business.service;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.business.service
 * @ClassName: MiaoYouJuanService
 * @Description: 喵有劵平台接口
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/21/021 17:44
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/21/021 17:44
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface MiaoYouJuanService {

    //高佣转链接API(淘口令)
    Map<String, String> getitemgyurlbytpwd(String tpwdcode) throws Exception;

    //万能高佣转链API(任意文字分享格式)
    Map<String, String> getgyurlbyall(String textInfo) throws Exception;

    //获取简版淘客商品信息
    Map<String,Object> getiteminfo(String goodsId) throws Exception;

    //好券直播
    List<Map<String,Object>> getcouponrealtime(Integer pageNum) throws Exception;

    //获取淘宝商品链接
    String goodsIdUrlLink(String textInfo) throws Exception;

    //(新版京东)京东商品查询API
    String getjdunionitems(String textInfo) throws Exception;

    //(新版)获取商品推广链接
    String getitemcpsurl() throws Exception;

    //获取全网淘客商品API
    List<Map<String,Object>> gettkmaterial(String keyword, Integer pageno, Integer defaultTag,
        Integer priceTag, Integer numberTag, Integer tmalltTag, Integer couponTag) throws Exception;

    //生成相似推荐url
    String getItemMoreUrl(String textInfo) throws Exception;
}
