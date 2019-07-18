package com.coupon.business.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coupon.business.service.MiaoYouJuanService;
import com.coupon.core.common.Constants;
import com.coupon.core.utils.HttpClientUtils;
import com.coupon.core.utils.PriceUtils;
import com.coupon.core.utils.TbkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.business.service.Impl
 * @ClassName: MiaoYouJuanServiceImpl
 * @Description: java类作用描述
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/21/021 17:47
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/21/021 17:47
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
@Transactional
@Slf4j
public class MiaoYouJuanServiceImpl implements MiaoYouJuanService {

    public static final String BASE_PATH = "http://47.98.133.210:8081/tb/goodsInfoUi/";

    //高佣转链接API(淘口令)
    @Override
    public String getitemgyurlbytpwd(String tpwdcode) throws Exception {
        if(StringUtils.isNotBlank(tpwdcode)) {
            //拼接高佣转链接API
            String tpwdcodeUrl = Constants.GY_TPWD+tpwdcode;
            //发送请求返回数据
            String couponData = HttpClientUtils.sendGet(tpwdcodeUrl);
            //返回信息组装返回模板信息并返回
            return packageCouponMessage(couponData);
        }
        return null;
    }

    //万能高佣转链API(任意文字分享格式)
    @Override
    public String getgyurlbyall(String textInfo) throws Exception {
        //拼接万能高佣请求url
        String contentUrl = Constants.GY_ALL+textInfo;
        //发送请求返回数据
        String couponData = HttpClientUtils.sendGet(contentUrl);
        //返回信息组装返回模板信息并返回
        return packageCouponMessage(couponData);
    }

    @Override
    public Map<String, Object> getiteminfo(String goodsId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        //拼接获取简版淘客商品信息请求url
        String contentUrl = Constants.ITEM_INFO+goodsId;
        //发送请求返回数据
        String itemData = HttpClientUtils.sendGet(contentUrl);
        if(StringUtils.isNotBlank(itemData)) {
            JSONObject itemJsonObject = JSON.parseObject(itemData);
            if(itemJsonObject.getInteger("code") == 200) {
                JSONObject itemDataJson = (JSONObject)itemJsonObject.get("data");
                JSONObject nTbkItemJson = (JSONObject)itemDataJson.get("n_tbk_item");
                //一级类目名称
                map.put("catName", nTbkItemJson.getString("cat_name"));
                //商品id
                map.put("numIid", nTbkItemJson.getString("num_iid"));
                //商品标题
                map.put("title", nTbkItemJson.getString("title"));
                //商品主图
                map.put("pictUrl", nTbkItemJson.getString("pict_url"));
                //商品小图列表
                JSONObject smallImageJson = (JSONObject)nTbkItemJson.get("small_images");
                List<String> imageList = new ArrayList<>();
                if(smallImageJson != null) {
                    String imageStr = smallImageJson.getString("string");
                    imageList = (List<String>) JSON.parseObject(imageStr, List.class);
                }
                map.put("imageList", imageList);
                //商品一口价格
                map.put("reservePrice", nTbkItemJson.getString("reserve_price"));
                //商品折扣价格
                map.put("zkFinalPrice", nTbkItemJson.getString("zk_final_price"));
                //卖家类型
                //0表示淘宝店铺，1表示天猫店铺
                map.put("userType", nTbkItemJson.getString("user_type"));
                //商品所在地
                map.put("provcity", nTbkItemJson.getString("provcity"));
                //商品链接
                map.put("itemUrl", nTbkItemJson.getString("item_url"));
                //卖家id
                map.put("sellerId", nTbkItemJson.getString("seller_id"));
                //30天销量
                map.put("volume", nTbkItemJson.getString("volume"));
                //店铺名称
                map.put("nick", nTbkItemJson.getString("nick"));
                //叶子类目名称
                map.put("catLeafName", nTbkItemJson.getString("cat_leaf_name"));
                //是否加入消费者保障
                map.put("isPrepay", nTbkItemJson.getString("is_prepay"));
                //店铺dsr 评分
                map.put("shopDsr", nTbkItemJson.getString("shop_dsr"));
                //卖家等级
                map.put("ratesum", nTbkItemJson.getString("ratesum"));
                //退款率是否低于行业均值
                //true，是
                map.put("iRfdRate", nTbkItemJson.getString("i_rfd_rate"));
                //好评率是否高于行业均值
                //true，是
                map.put("hGoodRate", nTbkItemJson.getString("h_good_rate"));
                //成交转化是否高于行业均值
                //true，是
                map.put("hPayRate30", nTbkItemJson.getString("h_pay_rate30"));
                //是否包邮
                //true，是
                map.put("freeShipment", nTbkItemJson.getString("free_shipment"));
                //商品库类型
                //支持多库类型输出，以“，”区分，1:营销商品主推库
                map.put("materialLibType", nTbkItemJson.getString("material_lib_type"));

                //拼接高佣转链接API
                String itemIdUrl = Constants.GY_ITEMID+nTbkItemJson.getString("num_iid");
                //发送请求返回数据
                String couponData = HttpClientUtils.sendGet(itemIdUrl);
                if(StringUtils.isNotBlank(couponData)) {
                    JSONObject jsonObject = JSON.parseObject(couponData);
                    if(jsonObject.getInteger("code") == 200) {
                        JSONObject resultJson = (JSONObject)jsonObject.get("result");
                        JSONObject dataJson = (JSONObject)resultJson.get("data");
                        //商品ID
                        map.put("itemId", dataJson.getLong("item_id"));
                        //商品分类ID
                        map.put("categoryId", dataJson.getLong("category_id"));
                        //短链接
                        //商品有优惠券时，短链接打开为二合一链接页面，无优惠券时，口令打开为商品页面（注：均为高佣金淘客链接，购买之后均有佣金）
                        map.put("shortUrl", dataJson.getString("short_url"));
                        //淘口令
                        //商品有优惠券时，口令打开为二合一链接页面，无优惠券时，口令打开为商品页面（注：均为高佣金淘客链接，购买之后均有佣金）
                        map.put("tpwd", dataJson.getString("tpwd"));
                        //商品淘客链接
                        //同样为高佣，has_coupon为false时，建议使用此链接
                        map.put("itemUrl", dataJson.getString("item_url"));
                        //高佣金比例
                        //计划中的最高佣金，如果阿里妈妈帐户为初级，则会走通用佣金
                        map.put("maxCommissionRate", dataJson.getString("max_commission_rate"));
                        //二合一链接
                        map.put("couponClickUrl", dataJson.getString("coupon_click_url"));
                        //是否有优惠券
                        //true，有；false，没有
                        map.put("hasCoupon", dataJson.getBoolean("has_coupon"));
                        //优惠券类型
                        //1 公开券，2 私有券，3 妈妈券
                        Integer couponType = null;
                        //优惠券开始时间
                        String couponStartTime = null;
                        //优惠券总量
                        //没有优惠券则不显示此字段
                        String couponTotalCount = null;
                        //优惠券剩余量
                        //没有优惠券则不显示此字段
                        String couponRemainCount = null;
                        //优惠券信息
                        //没有优惠券则不显示此字段
                        String couponInfo = null;
                        //优惠券有效期截止日期
                        //没有优惠券则不显示此字段
                        String couponEndTime = null;
                        //优惠券面额
                        //没有优惠券则不显示此字段
                        Double youhuiquan = null;
                        //优惠券使用条件
                        //优惠券使用限制条件，即满xx元使用
                        Double quanlimit = null;
                        Boolean hasCoupon = dataJson.getBoolean("has_coupon");
                        if (hasCoupon) {
                            couponType = dataJson.getInteger("coupon_type");
                            couponStartTime = dataJson.getString("coupon_start_time");
                            couponTotalCount = dataJson.getString("coupon_total_count");
                            couponRemainCount = dataJson.getString("coupon_remain_count");
                            couponInfo = dataJson.getString("coupon_info");
                            couponEndTime = dataJson.getString("coupon_end_time");
                            youhuiquan = dataJson.getDouble("youhuiquan");
                            quanlimit = dataJson.getDouble("quanlimit");
                        }
                        map.put("couponType", couponType);
                        map.put("couponStartTime", couponStartTime);
                        map.put("couponTotalCount", couponTotalCount);
                        map.put("couponRemainCount", couponRemainCount);
                        map.put("couponInfo", couponInfo);
                        map.put("couponEndTime", couponEndTime);
                        map.put("youhuiquan", youhuiquan);
                        map.put("quanlimit", quanlimit);
                    }
                }
            }
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> getcouponrealtime(Integer pageNum) {
        List<Map<String, Object>> list = new ArrayList<>();
        //拼接好券直播Url
        String contentUrl = Constants.BEST_COUPON_INFO_LIVE+pageNum;
        //发送请求返回数据
        String listData = HttpClientUtils.sendGet(contentUrl);
        if(StringUtils.isNotBlank(listData)) {
            JSONObject listJsonObject = JSON.parseObject(listData);
            if(listJsonObject.getInteger("code") == 200) {
                JSONArray jsonArrayData = (JSONArray)listJsonObject.get("data");
                for(int i = 0; i < jsonArrayData.size(); i++) {
                    JSONObject object = jsonArrayData.getJSONObject(i);
                    Map<String, Object> map = new HashMap<>();
                    //商品ID
                    map.put("numIid", object.getLong("num_iid"));
                    //商品主图
                    map.put("pictUrl", object.getString("pict_url"));
                    //商品标题
                    map.put("title", object.getString("title"));
                    //30天销量
                    map.put("volume", object.getLong("volume"));
                    //优惠券总量
                    map.put("couponTotalCount", object.getLong("coupon_total_count"));
                    //优惠券剩余量
                    map.put("couponRemainCount", object.getLong("coupon_remain_count"));
                    //折扣价
                    map.put("zkFinalPrice", object.getDouble("zk_final_price"));
                    //优惠券面额
                    map.put("youhuiquan", object.getDouble("youhuiquan"));
                    //优惠券使用条件，如：满XX元使用
                    map.put("quanlimit", object.getDouble("quanlimit"));
                    //优惠券信息
                    map.put("couponInfo", object.getString("coupon_info"));
                    list.add(map);
                }
            }
        }
        return list;
    }

    @Override
    public String goodsIdUrlLink(String textInfo) {
        if(StringUtils.isNotBlank(textInfo)) {
            String tpwdcode = TbkUtils.extractTbCode(textInfo);
            if(StringUtils.isNotBlank(tpwdcode)) {
                //拼接高佣转链接API
                String tpwdcodeUrl = Constants.GY_TPWD+tpwdcode;
                //发送请求返回数据
                String couponData = HttpClientUtils.sendGet(tpwdcodeUrl);
                //返回信息组装返回模板信息并返回
                if(StringUtils.isNotBlank(couponData)) {
                    JSONObject jsonObject = JSON.parseObject(couponData);
                    if(jsonObject.getInteger("code") == 200) {
                        JSONObject resultJson = (JSONObject)jsonObject.get("result");
                        JSONObject dataJson = (JSONObject)resultJson.get("data");
                        //商品ID
                        Long itemId = dataJson.getLong("item_id");
                        if(itemId != null) {
                            return BASE_PATH + itemId;
                        }
                    }
                }
            }
        }
        return null;
    }

    //组装返回优惠信息
    private String packageCouponMessage(String couponData) {
        if(StringUtils.isNotBlank(couponData)) {
            JSONObject jsonObject = JSON.parseObject(couponData);
            if(jsonObject.getInteger("code") == 200) {
                JSONObject resultJson = (JSONObject)jsonObject.get("result");
                JSONObject dataJson = (JSONObject)resultJson.get("data");
                //商品ID
                Long itemId = dataJson.getLong("item_id");
                //商品分类ID
                //Long categoryId = dataJson.getLong("category_id");
                //短链接
                //商品有优惠券时，短链接打开为二合一链接页面，无优惠券时，口令打开为商品页面（注：均为高佣金淘客链接，购买之后均有佣金）
                String shortUrl = dataJson.getString("short_url");
                //淘口令
                //商品有优惠券时，口令打开为二合一链接页面，无优惠券时，口令打开为商品页面（注：均为高佣金淘客链接，购买之后均有佣金）
                String tpwd = dataJson.getString("tpwd");
                //商品淘客链接
                //同样为高佣，has_coupon为false时，建议使用此链接
                //String itemUrl = dataJson.getString("item_url");
                //高佣金比例
                //计划中的最高佣金，如果阿里妈妈帐户为初级，则会走通用佣金
                String maxCommissionRate = dataJson.getString("max_commission_rate");
                //二合一链接
                //String couponClickUrl = dataJson.getString("coupon_click_url");
                //是否有优惠券
                //true，有；false，没有
                Boolean hasCoupon = dataJson.getBoolean("has_coupon");
                //优惠券类型
                //1 公开券，2 私有券，3 妈妈券
                ///Integer couponType = null;
                //优惠券开始时间
                //String couponStartTime = null;
                //优惠券总量
                //没有优惠券则不显示此字段
                //String couponTotalCount = null;
                //优惠券剩余量
                //没有优惠券则不显示此字段
                //String couponRemainCount = null;
                //优惠券信息
                //没有优惠券则不显示此字段
                String couponInfo = null;
                //优惠券有效期截止日期
                //没有优惠券则不显示此字段
                //String couponEndTime = null;
                //优惠券面额
                //没有优惠券则不显示此字段
                Double youhuiquan = null;
                //优惠券使用条件
                //优惠券使用限制条件，即满xx元使用
                Double quanlimit = null;
                if (hasCoupon) {
                    //couponType = dataJson.getInteger("coupon_type");
                    //couponStartTime = dataJson.getString("coupon_start_time");
                    //couponTotalCount = dataJson.getString("coupon_total_count");
                    //couponRemainCount = dataJson.getString("coupon_remain_count");
                    couponInfo = dataJson.getString("coupon_info");
                    //couponEndTime = dataJson.getString("coupon_end_time");
                    youhuiquan = dataJson.getDouble("youhuiquan");
                    quanlimit = dataJson.getDouble("quanlimit");
                }
                //获取简版淘客商品信息
                //拼接请求url
                String itemIdUrl = Constants.ITEM_INFO+itemId;
                //发送请求返回数据
                String itemData = HttpClientUtils.sendGet(itemIdUrl);
                if(StringUtils.isNotBlank(itemData)) {
                    JSONObject itemJsonObject = JSON.parseObject(itemData);
                    if(itemJsonObject.getInteger("code") == 200) {
                        JSONObject itemDataJson = (JSONObject)itemJsonObject.get("data");
                        JSONObject nTbkItemJson = (JSONObject)itemDataJson.get("n_tbk_item");
                        //一级类目名称
                        //String catName = nTbkItemJson.getString("cat_name");
                        //商品id
                        //String numIid = nTbkItemJson.getString("num_iid");
                        //商品标题
                        String title = nTbkItemJson.getString("title");
                        //商品主图
                        //String pictUrl = nTbkItemJson.getString("pict_url");
                        //商品小图列表
                        /*JSONObject smallImageJson = (JSONObject)nTbkItemJson.get("small_images");
                        List<String> imageList = new ArrayList<>();
                        if(smallImageJson != null) {
                            String imageStr = smallImageJson.getString("string");
                            imageList = (List<String>) JSON.parseObject(imageStr, List.class);
                        }*/
                        //商品一口价格
                        String reservePrice = nTbkItemJson.getString("reserve_price");
                        //商品折扣价格
                        String zkFinalPrice = nTbkItemJson.getString("zk_final_price");
                        //卖家类型
                        //0表示淘宝店铺，1表示天猫店铺
                        //String userType = nTbkItemJson.getString("user_type");
                        //商品所在地
                        //String provcity = nTbkItemJson.getString("provcity");
                        //商品链接
                        //String itemUrl1 = nTbkItemJson.getString("item_url");
                        //卖家id
                        //String sellerId = nTbkItemJson.getString("seller_id");
                        //30天销量
                        //String volume = nTbkItemJson.getString("volume");
                        //店铺名称
                        //String nick = nTbkItemJson.getString("nick");
                        //叶子类目名称
                        //String catLeafName = nTbkItemJson.getString("cat_leaf_name");
                        //是否加入消费者保障
                        //String isPrepay = nTbkItemJson.getString("is_prepay");
                        //店铺dsr 评分
                        //String shopDsr = nTbkItemJson.getString("shop_dsr");
                        //卖家等级
                        //String ratesum = nTbkItemJson.getString("ratesum");
                        //退款率是否低于行业均值
                        //true，是
                        //String iRfdRate = nTbkItemJson.getString("i_rfd_rate");
                        //好评率是否高于行业均值
                        //true，是
                        //String hGoodRate = nTbkItemJson.getString("h_good_rate");
                        //成交转化是否高于行业均值
                        //true，是
                        //String hPayRate30 = nTbkItemJson.getString("h_pay_rate30");
                        //是否包邮
                        //true，是
                        //String freeShipment = nTbkItemJson.getString("free_shipment");
                        //商品库类型
                        //支持多库类型输出，以“，”区分，1:营销商品主推库
                        //String materialLibType = nTbkItemJson.getString("material_lib_type");
                        //拼接返回的优惠信息模板
                        StringBuffer sb = new StringBuffer();
                        //商品title
                        sb.append("【").append(title).append("】\n");
                        //显示商品价格
                        BigDecimal bReservePrice = new BigDecimal(reservePrice);
                        BigDecimal bZkFinalPrice = new BigDecimal(zkFinalPrice);
                        int compare = bReservePrice.compareTo(bZkFinalPrice);
                        if(compare == 1) {
                            //原价
                            sb.append("【原价】").append(reservePrice).append("元\n");
                            //折扣价
                            sb.append("【折扣价】").append(zkFinalPrice).append("元\n");
                        }else {
                            sb.append("【价格】").append(reservePrice).append("元\n");
                        }
                        //返利给客户金额;预估最多返利
                        Double rebatePrice;
                        if(hasCoupon) {
                            rebatePrice = PriceUtils.rebatePrice(reservePrice, zkFinalPrice, quanlimit.toString(), youhuiquan.toString(), maxCommissionRate);
                        }else {
                            rebatePrice = PriceUtils.rebatePrice(reservePrice, zkFinalPrice, null, null, maxCommissionRate);
                        }
                        if(rebatePrice > 0) {
                            sb.append("【预估返利】").append(rebatePrice).append("元\n");
                        }
                        if(hasCoupon) {
                            //优惠券
                            sb.append("【优惠券】").append(couponInfo).append("\n");
                            //电脑下单地址
                            sb.append("【电脑下单地址】\n").append(shortUrl).append("\n");
                            sb.append("------------------------------\n");
                            //淘口令
                            sb.append("手机復·制这段信息,").append(tpwd);
                            sb.append("打开【淘♂寳♀】领取优惠券并下单!\n");
                        }else {
                            //电脑下单地址
                            sb.append("【电脑下单地址】\n").append(shortUrl).append("\n");
                            sb.append("------------------------------\n");
                            //淘口令
                            sb.append("手机復·制这段信息,").append(tpwd);
                            sb.append("打开【淘♂寳♀】点击淘口令下单!\n");
                        }
                        sb.append("------------------------------\n");
                        sb.append("【推广福利】发送订单截图立刻获得返利红包!");
                        return sb.toString();
                    }
                }
            }
        }
        return null;
    }
}
