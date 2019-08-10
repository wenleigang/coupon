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
import java.util.*;

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


    public static final String BASE_PATH = "http://www.findcoupon.top/tb/goodsInfoUi/";

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

    @Override
    public String getjdunionitems(String textInfo) throws Exception {
        String jdItemId = TbkUtils.extractJdItemId(textInfo);
        if(StringUtils.isNotBlank(jdItemId)) {
            //京东商品查询API
            String contentUrl = Constants.JD_ITEM_INFO + jdItemId;
            //发送请求返回数据
            String listData = HttpClientUtils.sendGet(contentUrl);
            if(StringUtils.isNotBlank(listData)) {
                JSONObject listJsonObject = JSON.parseObject(listData);
                if(listJsonObject.getInteger("code") == 200) {
                    JSONObject resultJson = (JSONObject)listJsonObject.get("data");
                    //请求到的总商品数
                    Integer totalCount = resultJson.getInteger("totalCount");
                    //商品列表
                    JSONArray jsonArrayData = (JSONArray)resultJson.get("lists");
                    for(int i = 0; i < jsonArrayData.size(); i++) {
                        JSONObject object = jsonArrayData.getJSONObject(i);
                        //类目信息
                        /*JSONObject categoryInfo = object.getJSONObject("categoryInfo");
                        String cid1 = categoryInfo.getString("cid1");//一级类目ID
                        String cid1Name = categoryInfo.getString("cid1Name");//一级类目名
                        String cid2 = categoryInfo.getString("cid2");//二级类目ID
                        String cid2Name = categoryInfo.getString("cid2Name");//二级类目名
                        String cid3 = categoryInfo.getString("cid3");//三级类目ID
                        String cid3Name = categoryInfo.getString("cid3Name");//三级类目名*/
                        //佣金信息
                        JSONObject commissionInfo = object.getJSONObject("commissionInfo");
                        String commission = commissionInfo.getString("commission");//佣金
                        String commissionShare = commissionInfo.getString("commissionShare");//佣金比例
                        //优惠券信息，返回内容为空说明该SKU无可用优惠券
                        JSONObject couponInfo = object.getJSONObject("couponInfo");
                        StringBuffer cb = new StringBuffer();
                        TreeMap<String, String> treeMap = new TreeMap<>(new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                Double a = Double.valueOf(o1);
                                Double b = Double.valueOf(o2);
                                if(b - a > 0) {
                                    return 1;
                                }else if(b - a == 0) {
                                    return 0;
                                }else {
                                    return -1;
                                }
                            }
                        });
                        if(couponInfo != null && couponInfo.size() != 0) {
                            JSONArray couponList = couponInfo.getJSONArray("couponList");//优惠券合集
                            for(int j = 0; j < couponList.size(); j++) {
                                JSONObject couponObject = couponList.getJSONObject(j);
                                //优惠券种类：0 - 全品类，1 - 限品类（自营商品），2 - 限店铺，3 - 店铺限商品券
                                //「」『』〖〗
                                String bindType = couponObject.getString("bindType");
                                String typeText = "』";
                                if(bindType.equals("0")) {
                                    typeText = "/全品类』";
                                }
                                if(bindType.equals("1")) {
                                    typeText = "/限品类』";
                                }
                                if(bindType.equals("2")) {
                                    typeText = "/限店铺』";
                                }
                                if(bindType.equals("3")) {
                                    typeText = "/店铺限商品』";
                                }
                                //券面额
                                String discount = couponObject.getString("discount");
                                //券链接
                                //String link = couponObject.getString("link");
                                //平台类型：0 - 全平台券，1 - 限平台券
                                String platformType = couponObject.getString("platformType");
                                //券消费限额
                                String quota = couponObject.getString("quota");
                                treeMap.put(quota, discount);
                                //领取开始时间
                                //String getStartTime = couponObject.getString("getStartTime");
                                //券领取结束时间
                                //String getEndTime = couponObject.getString("getEndTime");
                                //券有效使用开始时间
                                //String useStartTime = couponObject.getString("useStartTime");
                                //券有效使用结束时间
                                //String useEndTime = couponObject.getString("useEndTime");
                                String couponTag = "『满"+quota+"元减"+discount+"元"+typeText;
                                cb.append(couponTag);
                            }
                        }
                        //图片信息
                        /*JSONObject imageInfo = object.getJSONObject("imageInfo");
                        JSONArray imageList = imageInfo.getJSONArray("imageList");//图片合集
                        for(int k = 0; k < imageList.size(); k++) {
                            JSONObject imageObject = imageList.getJSONObject(k);
                            String url = imageObject.getString("url");//图片链接地址
                        }*/
                        //价格信息
                        JSONObject priceInfo = object.getJSONObject("priceInfo");
                        String price = priceInfo.getString("price");//无线价格
                        //店铺信息
                        JSONObject shopInfo = object.getJSONObject("shopInfo");
                        //String shopName = shopInfo.getString("shopName");//店铺名称(或供应商名称)
                        //String shopId = shopInfo.getString("shopId");//店铺ID
                        //评论数
                        //String comments = object.getString("comments");
                        //商品好评率
                        //String goodCommentsShare = object.getString("goodCommentsShare");
                        //30天引单数量
                        //String inOrderCount30Days = object.getString("inOrderCount30Days");
                        //是否自营 (1 : 是, 0 : 否)，后续会废弃，请用owner
                        //String isJdSale = object.getString("isJdSale");
                        //商品落地页
                        //String materialUrl = object.getString("materialUrl");
                        //商品ID
                        //String skuId = object.getString("skuId");
                        //商品名称
                        String skuName = object.getString("skuName");
                        //是否爆款 1：是，0：否
                        //String isHot = object.getString("isHot");
                        //spuid，其值为同款商品的主skuid
                        //String spuid = object.getString("spuid");
                        //品牌code
                        //String brandCode = object.getString("brandCode");
                        //品牌名
                        //String brandName = object.getString("brandName");
                        //g=自营，p=pop
                        //String owner = object.getString("owner");

                        //获取分享链接
                        //京东商品查询API
                        //使用Urlencode进行编码
                        String encode = URLEncoder.encode(textInfo, "GBK");
                        String shareInfoUrl = Constants.JD_ITEM_SHARE_INFO + encode;
                        //发送请求返回数据
                        String urlData = HttpClientUtils.sendGet(shareInfoUrl);
                        if(StringUtils.isNotBlank(urlData)) {
                            JSONObject jsonObject = JSON.parseObject(urlData);
                            if(jsonObject.getInteger("code") == 200) {
                                JSONObject dataJson = (JSONObject)jsonObject.get("data");
                                //生成的推广目标链接，以短链接形式，有效期为半年
                                String shortURL = dataJson.getString("shortURL");

                                //拼接返回的优惠信息模板
                                StringBuffer sb = new StringBuffer();
                                //商品title
                                sb.append("【").append(skuName).append("】\n");
                                //显示商品价格
                                sb.append("【价格】").append(price).append("元\n");
                                //返利给客户金额;预估最多返利
                                Double rebateJdPrice = PriceUtils.rebateJdPrice(commission, commissionShare, price, treeMap);
                                sb.append("【最高返利】").append(rebateJdPrice).append("元\n");
                                //优惠券
                                if(StringUtils.isNotBlank(cb.toString())) {
                                    sb.append("【优惠券】").append(cb.toString()).append("\n");
                                }
                                //下单地址
                                sb.append("【下单地址】\n").append(shortURL).append("\n");
                                sb.append("------------------------------\n");
                                //提示
                                sb.append("点击【下单地址】跳转京东app下单!\n");
                                sb.append("------------------------------\n");
                                sb.append("【推广福利】发送订单截图立刻获得返利红包!");
                                return sb.toString();
                            }
                        }

                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getitemcpsurl() throws Exception {
        return null;
    }

    @Override
    public List<Map<String, Object>> gettkmaterial(String keyword, Integer pageno) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        //获取全网淘客商品API
        String encode = URLEncoder.encode(keyword, "UTF-8");
        /*
            &keyword=keywordText
            &pageno=pagenoText
            &sort=sortText
            &isoverseas=isoverseasText
            &istmall=istmallText
            &hascoupon=hascouponText";
        */
        String text = Constants.TK_MATERIAL.replace("keywordText", encode);
        String contentUrl = text+pageno;
        //发送请求返回数据
        String listData = HttpClientUtils.sendGet(contentUrl);
        if(StringUtils.isNotBlank(listData)) {
            JSONObject listJsonObject = JSON.parseObject(listData);
            if(listJsonObject.getInteger("code") == 200) {
                Integer totalcount = listJsonObject.getInteger("totalcount");
                if(totalcount > 0) {
                    if(totalcount == 1) {
                        JSONObject object = (JSONObject)listJsonObject.get("data");
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
                    }else {
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
            }
        }
        return list;
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
                            sb.append("【现价】").append(zkFinalPrice).append("元\n");
                        }else {
                            sb.append("【现价】").append(reservePrice).append("元\n");
                        }
                        //返利给客户金额;预估最多返利
                        Double rebatePrice;
                        if(hasCoupon) {
                            rebatePrice = PriceUtils.rebatePrice(reservePrice, zkFinalPrice, quanlimit.toString(), youhuiquan.toString(), maxCommissionRate);
                        }else {
                            rebatePrice = PriceUtils.rebatePrice(reservePrice, zkFinalPrice, null, null, maxCommissionRate);
                        }
                        if(rebatePrice > 0) {
                            sb.append("【最高返利】").append(rebatePrice).append("元\n");
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
