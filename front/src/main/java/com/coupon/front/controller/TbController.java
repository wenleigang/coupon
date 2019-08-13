package com.coupon.front.controller;

import com.coupon.business.service.MiaoYouJuanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.front.controller
 * @ClassName: TbController
 * @Description: java类作用描述
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/27/027 15:35
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/27/027 15:35
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/tb")
@Slf4j
public class TbController {

    @Autowired
    private MiaoYouJuanService miaoYouJuanService;

    /**
     * 跳转到商品列表页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/goodsListUi", method = RequestMethod.GET)
    public ModelAndView goodsListUi(ModelAndView modelAndView) {
        modelAndView.setViewName("tb/goods_list");
        return modelAndView;
    }

    /**
     * 跳转到商品详情页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/goodsInfoUi/{goodsId}", method = RequestMethod.GET)
    public ModelAndView goodsInfoUi(ModelAndView modelAndView, @PathVariable("goodsId")String goodsId) {
        modelAndView.addObject("goodsId", goodsId);
        modelAndView.setViewName("tb/goods_info");
        return modelAndView;
    }

    /**
     * 根据商品id获取商品基本信息以及优惠券情况
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/goodsInfo/{goodsId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> goodsInfo(@PathVariable("goodsId")String goodsId) {
        try {
            Map<String, Object> map = miaoYouJuanService.getiteminfo(goodsId);
            if(map.size() == 0) {
                return null;
            }
            return map;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取限时优惠页面商品列表
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/goodsList", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> goodsList(Integer pageNum) {
        try {
            List<Map<String, Object>> list = miaoYouJuanService.getcouponrealtime(pageNum);
            return list;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param keyword 关键字
     * @param pageno 页码数
     * @param defaultTag 默认 0 未选择 1选择默认
     * @param priceTag 0 未选择 1价格降序 2价格升序
     * @param numberTag 0 未选择 1销量降序 2销量升序
     * @param tmalltTag 天猫商品 0 未选择 1天猫
     * @param couponTag 优惠券 0 未选择 1优惠券
     * @return
     */
    @RequestMapping(value = "/searchGoodsList", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> searchGoodsList(String keyword, Integer pageno, Integer defaultTag,
            Integer priceTag, Integer numberTag, Integer tmalltTag, Integer couponTag) {
        try {
            List<Map<String, Object>> list = miaoYouJuanService.gettkmaterial(keyword, pageno, defaultTag, priceTag, numberTag, tmalltTag, couponTag);
            return list;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
