package com.coupon.front.controller;

import com.alibaba.fastjson.JSON;
import com.coupon.business.service.MiaoYouJuanService;
import com.coupon.business.service.TuLingService;
import com.coupon.core.utils.TbkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.front.controller
 * @ClassName: TestController
 * @Description: java类作用描述
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/27/027 15:10
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/27/027 15:10
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private TuLingService tuLingService;

    @Autowired
    private MiaoYouJuanService miaoYouJuanService;

    /**
     * 图灵机器人1.0
     * @param info
     * @return
     */
    @RequestMapping(value = "/tuling")
    @ResponseBody
    public Object testTuLing(String info) {
        String test = tuLingService.TuLingTest(info);
        Map map = JSON.parseObject(test);
        return map;
    }

    /**
     * 二维码界面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/qrcode")
    public ModelAndView testQrcode(ModelAndView modelAndView) {
        modelAndView.setViewName("test/qrcode");
        return modelAndView;
    }

    /**
     * 优惠券界面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/coupon")
    public ModelAndView testCoupon(ModelAndView modelAndView) {
        modelAndView.setViewName("test/coupon");
        return modelAndView;
    }

    /**
     * 淘口令测试
     * @param info
     * @return
     */
    @RequestMapping(value = "/code")
    @ResponseBody
    public String testCode(String info) {
        try {
            String getitemgyurlbytpwd = miaoYouJuanService.getitemgyurlbytpwd(info);
            return getitemgyurlbytpwd;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().toString();
        }
    }

    /**
     * 任意口令
     * @param info
     * @return
     */
    @RequestMapping(value = "/all")
    @ResponseBody
    public String testAll(String info) {
        try {
            String getgyurlbyall = miaoYouJuanService.getgyurlbyall(info);
            return getgyurlbyall;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().toString();
        }
    }

    public static void main(String[] args) {
        String textInfo1 = "【顺艺衣架成人防滑衣架10只装家用晾衣架衣撑衣服撑子裙裤架衣服架】https://m.tb.cn/h.eTMZgds?sm=c496f6 点击链接，再选择浏览器咑閞；或復·制这段描述￥xo4cYT8q3Jb￥后到淘♂寳♀";
        String textInfo2 = "【顺艺衣架成人防滑衣架10只装家用晾衣架衣撑衣服撑子裙裤架衣服架】https://m.tb.cn/h.eTMZgds?sm=c496f6 点击链接，再选择浏览器咑閞；或復·制这段描述€xo4cYT8q3Jb€后到淘♂寳♀";
        String textInfo3 = "【顺艺衣架成人防滑衣架10只装家用晾衣架衣撑衣服撑子裙裤架衣服架】https://m.tb.cn/h.eTMZgds?sm=c496f6 点击链接，再选择浏览器咑閞；或復·制这段描述$xo4cYT8q3Jb$后到淘♂寳♀";
        String textInfo4 = "【顺艺衣架成人防滑衣架10只装家用晾衣架衣撑衣服撑子裙裤架衣服架】https://m.tb.cn/h.eTMZgds?sm=c496f6 点击链接，再选择浏览器咑閞；或復·制这段描述&xo4cYT8q3Jb&后到淘♂寳♀";
        String s1 = TbkUtils.hasTpwdcode(textInfo1);
        if(s1 != null) {
            String tpwdcode = TbkUtils.extractTpwdcode(textInfo1);
            System.out.println(tpwdcode);
        }else {
            System.out.println("无链接信息");
        }
        String s2 = TbkUtils.hasTpwdcode(textInfo2);
        if(s2 != null) {
            String tpwdcode = TbkUtils.extractTpwdcode(textInfo2);
            System.out.println(tpwdcode);
        }else {
            System.out.println("无链接信息");
        }
        String s3 = TbkUtils.hasTpwdcode(textInfo3);
        if(s3 != null) {
            String tpwdcode = TbkUtils.extractTpwdcode(textInfo3);
            System.out.println(tpwdcode);
        }else {
            System.out.println("无链接信息");
        }
        String s4 = TbkUtils.hasTpwdcode(textInfo4);
        if(s4 != null) {
            String tpwdcode = TbkUtils.extractTpwdcode(textInfo4);
            System.out.println(tpwdcode);
        }else {
            System.out.println("无链接信息");
        }
    }
}
