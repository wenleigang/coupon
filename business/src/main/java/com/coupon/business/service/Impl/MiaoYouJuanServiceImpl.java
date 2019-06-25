package com.coupon.business.service.Impl;

import com.alibaba.fastjson.JSON;
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

    //高佣转链接API(淘口令)
    @Override
    public String getitemgyurlbytpwd(String textInfo) throws Exception {
        //商品描述提取淘口令
        String tpwdcode = TbkUtils.extractTpwdcode(textInfo);
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
        //文字中包含淘口令需要urlencode编码
        if(!TbkUtils.isTpwdcode(textInfo)&&TbkUtils.hasTpwdcode(textInfo)) {
            textInfo = URLEncoder.encode(textInfo, "GBK");
        }
        //拼接万能高佣请求url
        String contentUrl = Constants.GY_ALL+textInfo;
        //发送请求返回数据
        String couponData = HttpClientUtils.sendGet(contentUrl);
        //返回信息组装返回模板信息并返回
        return packageCouponMessage(couponData);
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
