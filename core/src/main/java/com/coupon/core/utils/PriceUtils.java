package com.coupon.core.utils;

import java.math.BigDecimal;

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

    /**
     * 计算实际最大返利
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
        //高佣金比例系数
        BigDecimal ratio = new BigDecimal("0.01");
        //预付款结算金额达到优惠券使用条件,减去优惠券金额计算返利佣金;否则直接计算返利佣金
        if(targetPrice.compareTo(bQuanLimit) >= 0) {
            finalPrice = targetPrice.subtract(bYouHuiJuan);
        }else {
            finalPrice = targetPrice;
        }

        /**
         * 返利用户规则;
         * 所得佣金都需要乘0.8的系数,因为淘宝会抽成技术服务费以及个税等;
         * 【1】 0.50元 >= 所得返利 > 0元, 返利系数 1.0;
         * 【2】 5.00元 >= 所得返利 > 0.50元, 返利系数 0.8;
         * 【3】 50.00元 >= 所得返利 > 5.00元, 返利系数 0.7;
         * 【3】 所得返利 > 50.00元, 返利系数 0.6;
         */
        BigDecimal fiftyRatio = new BigDecimal("50.00");
        BigDecimal fiveRatio = new BigDecimal("5.00");
        BigDecimal oneRatio = new BigDecimal("1.00");
        BigDecimal zeroEightRatio = new BigDecimal("0.80");
        BigDecimal zeroSevenRatio = new BigDecimal("0.70");
        BigDecimal zeroSixRatio = new BigDecimal("0.60");
        BigDecimal zeroFiveRatio = new BigDecimal("0.50");
        BigDecimal zeroRatio = new BigDecimal("0.00");
        //此计算结果均为最高佣金,如为初级账户,走通用佣金(强烈不建议初级账号使用此结算标准为用户返利!!!!)
        BigDecimal ratePrice = finalPrice.multiply(bMaxCommissionRate).multiply(ratio);
        //预返利金额
        BigDecimal customPrice = ratePrice.multiply(zeroEightRatio);
        //0.50元 >= 所得返利 > 0元, 返利系数 1.0;
        if(customPrice.compareTo(zeroRatio) > 0 && customPrice.compareTo(zeroFiveRatio) <= 0) {
            rebatePrice = customPrice.multiply(oneRatio).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //5.00元 >= 所得返利 > 0.50元, 返利系数 0.8;
        if(customPrice.compareTo(zeroFiveRatio) > 0 && customPrice.compareTo(fiveRatio) <= 0) {
            rebatePrice = customPrice.multiply(zeroEightRatio).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //50.00元 >= 所得返利 > 5.00元, 返利系数 0.7;
        if(customPrice.compareTo(fiveRatio) > 0 && customPrice.compareTo(fiftyRatio) <= 0) {
            rebatePrice = customPrice.multiply(zeroSevenRatio).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //所得返利 > 50.00元, 返利系数 0.6;
        if(customPrice.compareTo(fiftyRatio) > 0) {
            rebatePrice = customPrice.multiply(zeroSixRatio).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return rebatePrice;
    }
}
