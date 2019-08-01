package com.coupon.core.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.core.utils
 * @ClassName: PriceUtils
 * @Description: 价钱的工具类
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/19/019 17:47
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/19/019 17:47
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PriceUtils {

    private static final BigDecimal FIFTY_RATIO = new BigDecimal("50.00");
    private static final BigDecimal TWENTY_RATIO = new BigDecimal("20.00");
    private static final BigDecimal FIVE_RATIO = new BigDecimal("5.00");
    private static final BigDecimal ONE_RATIO = new BigDecimal("1.00");
    private static final BigDecimal ZERO_NINE_FIVE_RATIO = new BigDecimal("0.95");
    private static final BigDecimal ZERO_NINE_RATIO = new BigDecimal("0.90");
    private static final BigDecimal ZERO_EIGHT_FIVE_RATIO = new BigDecimal("0.85");
    private static final BigDecimal ZERO_EIGHT_RATIO = new BigDecimal("0.80");
    private static final BigDecimal ZERO_SEVEN_FIVE_RATIO = new BigDecimal("0.75");
    private static final BigDecimal ZERO_SEVEN_RATIO = new BigDecimal("0.70");
    private static final BigDecimal ZERO_SIX_FIVE_RATIO = new BigDecimal("0.65");
    private static final BigDecimal ZERO_SIX_RATIO = new BigDecimal("0.60");
    private static final BigDecimal ZERO_FIVE_FIVE_RATIO = new BigDecimal("0.55");
    private static final BigDecimal ZERO_FIVE_RATIO = new BigDecimal("0.50");
    private static final BigDecimal ZERO_ZERO_ONE_RATIO = new BigDecimal("0.01");
    private static final BigDecimal ZERO_RATIO = new BigDecimal("0.00");

    /**
     * 淘宝计算实际最大返利
     * @param reservePrice 商品价格
     * @param zkFinalPrice 折后价
     * @param quanLimit 优惠券使用条件, 满quanLimit才能使用
     * @param youHuiJuan 优惠券面额
     * @param maxCommissionRate 计划中的最高佣金，如果阿里妈妈帐户为初级，则会走通用佣金
     * @return
     */
    public static Double rebatePrice(String reservePrice, String zkFinalPrice, String quanLimit, String youHuiJuan, String maxCommissionRate) {
        Double rebatePrice = 0d;
        
        //将各个参数String转BigDecimal后进行高精度计算
        BigDecimal bReservePrice = new BigDecimal(reservePrice == null ? "0" : reservePrice);
        BigDecimal bZkFinalPrice = new BigDecimal(zkFinalPrice == null ? "0" : zkFinalPrice);
        BigDecimal bQuanLimit = new BigDecimal(quanLimit == null ? "0" : quanLimit);
        BigDecimal bYouHuiJuan = new BigDecimal(youHuiJuan == null ? "0" : youHuiJuan);
        BigDecimal bMaxCommissionRate = new BigDecimal(maxCommissionRate == null ? "0" : maxCommissionRate);

        //商品实际买卖的价格
        BigDecimal targetPrice;
        //商品价格大于折后价,使用折后价计算
        if(bReservePrice.compareTo(bZkFinalPrice) == 1) {
            targetPrice = bZkFinalPrice;
        }else {//商品价格小于等于折后价,使用商品价格计算
            targetPrice = bReservePrice;
        }

        //单件商品付款金额
        BigDecimal finalPrice;
        //预付款结算金额达到优惠券使用条件,减去优惠券金额计算返利佣金;否则直接计算返利佣金
        if(targetPrice.compareTo(bQuanLimit) >= 0) {
            finalPrice = targetPrice.subtract(bYouHuiJuan);
        }else {
            finalPrice = targetPrice;
        }

        /**
         * 返利用户规则;
         * 所得佣金都需要乘0.8的系数,因为淘宝会抽成技术服务费以及个税等;
         * 【1】 1.0元 >= 所得返利 > 0元, 返利系数 1.0;
         * 【2】 5.00元 >= 所得返利 > 1.00元, 返利系数 0.90;
         * 【3】 20.00元 >= 所得返利 > 5.00元, 返利系数 0.85;
         * 【4】 50.00元 >= 所得返利 > 20.00元, 返利系数 0.7;
         * 【5】 所得返利 > 50.00元, 返利系数 0.6;
         */

        //此计算结果均为最高佣金,如为初级账户,走通用佣金(强烈不建议初级账号使用此结算标准为用户返利!!!!)
        BigDecimal ratePrice = finalPrice.multiply(bMaxCommissionRate).multiply(ZERO_ZERO_ONE_RATIO);
        //预返利金额
        BigDecimal customPrice = ratePrice.multiply(ZERO_EIGHT_RATIO);
        //【1】 1.0元 >= 所得返利 > 0元, 返利系数 1.0;
        if(customPrice.compareTo(ZERO_RATIO) > 0 && customPrice.compareTo(ONE_RATIO) <= 0) {
            rebatePrice = customPrice.multiply(ONE_RATIO).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //【2】5.00元 >= 所得返利 > 1.00元, 返利系数 0.9;
        if(customPrice.compareTo(ONE_RATIO) > 0 && customPrice.compareTo(FIVE_RATIO) <= 0) {
            rebatePrice = customPrice.multiply(ZERO_NINE_RATIO).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //【3】20.00元 >= 所得返利 > 5.00元, 返利系数 0.85;
        if(customPrice.compareTo(FIVE_RATIO) > 0 && customPrice.compareTo(TWENTY_RATIO) <= 0) {
            rebatePrice = customPrice.multiply(ZERO_EIGHT_FIVE_RATIO).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //【4】50.00元 >= 所得返利 > 20.00元, 返利系数 0.7;
        if(customPrice.compareTo(TWENTY_RATIO) > 0 && customPrice.compareTo(FIFTY_RATIO) <= 0) {
            rebatePrice = customPrice.multiply(ZERO_SEVEN_RATIO).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //【5】 所得返利 > 50.00元, 返利系数 0.6;
        if(customPrice.compareTo(FIFTY_RATIO) > 0) {
            rebatePrice = customPrice.multiply(ZERO_SIX_RATIO).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return rebatePrice;
    }

    /**
     * 京东返利计算
     * @param commission
     * @param commissionShare
     * @return
     */
    public static Double rebateJdPrice(String commission, String commissionShare, String price, TreeMap<String, String> treeMap) {
        Double rebatePrice = 0d;
        //将各个参数String转BigDecimal后进行高精度计算
        BigDecimal bCommission = new BigDecimal(commission == null ? "0" : commission);
        BigDecimal bCommissionShare = new BigDecimal(commissionShare == null ? "0" : commissionShare);
        BigDecimal bPrice = new BigDecimal(price == null ? "0" : price);
        BigDecimal discount = new BigDecimal(0);
        //根据商品原价获取最高可用优惠券!//接口只会返回一个优惠券后期跟进!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        for (String quota : treeMap.keySet()) {
            BigDecimal bQuota = new BigDecimal(price == null ? "0" : price);
            if(bPrice.compareTo(bQuota) >= 0) {
                discount = new BigDecimal(treeMap.get(quota));
                break;
            }
        }

        BigDecimal finalPrice = bPrice.subtract(discount);
        BigDecimal finalCommission = finalPrice.multiply(bCommissionShare).multiply(ZERO_ZERO_ONE_RATIO);
        /**
         * 返利用户规则;
         * 【1】 1.0元 >= 所得返利 > 0元, 返利系数 1.0;
         * 【2】 5.00元 >= 所得返利 > 1.00元, 返利系数 0.9;
         * 【3】 20.00元 >= 所得返利 > 5.00元, 返利系数 0.8;
         * 【4】 50.00元 >= 所得返利 > 20.00元, 返利系数 0.7;
         * 【5】 所得返利 > 50.00元, 返利系数 0.6;
         */

        //【1】 1.0元 >= 所得返利 > 0元, 返利系数 1.0;
        if(finalCommission.compareTo(ZERO_RATIO) > 0 && finalCommission.compareTo(ONE_RATIO) <= 0) {
            rebatePrice = finalCommission.multiply(ONE_RATIO).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //【2】5.00元 >= 所得返利 > 1.00元, 返利系数 0.9;
        if(finalCommission.compareTo(ONE_RATIO) > 0 && finalCommission.compareTo(FIVE_RATIO) <= 0) {
            rebatePrice = finalCommission.multiply(ZERO_NINE_RATIO).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //【3】20.00元 >= 所得返利 > 5.00元, 返利系数 0.8;
        if(finalCommission.compareTo(FIVE_RATIO) > 0 && finalCommission.compareTo(TWENTY_RATIO) <= 0) {
            rebatePrice = finalCommission.multiply(ZERO_EIGHT_RATIO).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //【4】50.00元 >= 所得返利 > 20.00元, 返利系数 0.7.5;
        if(finalCommission.compareTo(TWENTY_RATIO) > 0 && finalCommission.compareTo(FIFTY_RATIO) <= 0) {
            rebatePrice = finalCommission.multiply(ZERO_SEVEN_FIVE_RATIO).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //【5】 所得返利 > 50.00元, 返利系数 0.6.5;
        if(finalCommission.compareTo(FIFTY_RATIO) > 0) {
            rebatePrice = finalCommission.multiply(ZERO_SIX_FIVE_RATIO).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return rebatePrice;
    }
}
