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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.logging.Level.parse;

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
        /*String textInfo1 = "【草本善野生红心番石榴茶 番石榴干片番石榴果芭乐干番石榴片 干片】https://m.tb.cn/h.e6tAbYD?sm=0eedde 点击链接，再选择浏览器咑閞；或復·制这段描述/CLVXYTNKYUg/后到淘♂寳♀";
        String textInfo2 = "【草本善野生红心番石榴茶 番石榴干片番石榴果芭乐干番石榴片 干片】https://m.tb.cn/h.e6tAbYD?sm=0eedde 点击链接，再选择浏览器咑閞；或復·制这段描述.CLVXYTNKYUg.后到淘♂寳♀";
        String textInfo3 = "【2019春季 通城学典小学数学计算能手四年级下册北师大版 小学生4年级提升思维训练口算估算笔算练习册提升默写能力练习工具书/正版】https://m.tb.cn/h.e6r2ZGZ?sm=0f8da9 点击链接，再选择浏览器咑閞；或復·制这段描述¢OADFYTkmGcd¢后到淘♂寳♀";
        String textInfo4 = "【BEAMS JAPAN 三针手表腕表 日本制】https://m.tb.cn/h.eTJK1Wt?sm=538dbf 点击链接，再选择浏览器咑閞；或復·制这段描述$K7s1YTOsl7w$后到淘♂寳♀";
        String textInfo5 = "【2019春季 通城学典小学数学计算能手四年级下册北师大版 小学生4年级提升思维训练口算估算笔算练习册提升默写能力练习工具书/正版】https://m.tb.cn/h.e6r2ZGZ?sm=0f8da9 点击链接，再选择浏览器咑閞；或復·制这段描述¢OADFYTkmGcd¢后到淘♂寳♀";
        String textInfo6 = "【衣架家用晒不锈钢成人防滑衣撑子儿童无痕衣挂钩晾衣服架挂衣架子】https://m.tb.cn/h.e6ph6az?sm=4c672b 点击链接，再选择浏览器咑閞；或復·制这段描述₤8tVgYT8pF3u₤后到淘♂寳♀";
        String textInfo7 = "【衣架家用晒不锈钢成人防滑衣撑子儿童无痕衣挂钩晾衣服架挂衣架子】https://m.tb.cn/h.e6ph6az?sm=4c672b 点击链接，再选择浏览器咑閞；或復·制这段描述₤8tVgYT8pF3u₤后到淘♂寳♀";
        String textInfo8 = "【40个加粗衣架浸塑料成人防滑晾挂钩衣架子批发家用无痕衣撑衣服架】https://m.tb.cn/h.eTAIdtt?sm=6386f6 点击链接，再选择浏览器咑閞；或復·制这段描述€HNQQYT8LvGh€后到淘♂寳♀";
        String s1 = TbkUtils.extractTbCode(textInfo1);
        String s2 = TbkUtils.extractTbCode(textInfo2);
        String s3 = TbkUtils.extractTbCode(textInfo3);
        String s4 = TbkUtils.extractTbCode(textInfo4);
        String s5 = TbkUtils.extractTbCode(textInfo5);
        String s6 = TbkUtils.extractTbCode(textInfo6);
        String s7 = TbkUtils.extractTbCode(textInfo7);
        String s8 = TbkUtils.extractTbCode(textInfo8);
        System.err.println(s1);
        System.err.println(s2);
        System.err.println(s3);
        System.err.println(s4);
        System.err.println(s5);
        System.err.println(s6);
        System.err.println(s7);
        System.err.println(s8);*/
        //String text = "/tb/goodsInfoUi/549906105947";
        //String text = "896   肖祖根 13015989585";
        //String text = "http://47.98.133.210:8081/tb/goodsListUi";
        //String s = TbkUtils.extractTbCode(text);
        //System.err.println(s);
        /*String text = "\n" +
                "我的手机 2019/7/13/周六 14:13:51\n" +
                "三只松鼠 坚果大礼包1353g混合坚果礼盒零食小吃7份装坚果礼包 \n" +
                "【在售价】98.00元\n" +
                "【券后价】78.00元\n" +
                "【下单链接】https://m.tb.cn/h.ehVfK3m \n" +
                "----------------- \n" +
                "复制这条信息，￥gZ0iYh3BmY0￥，到【手机淘宝】即可查看";
        int i = text.indexOf("到【手机淘宝】即可查看");
        int j = text.indexOf("到【手机淘宝】即111111可查看");
        System.out.println(i +"--"+ j);*/
        String textInfo1 = "【草本善野生红心番石榴茶 番石榴干片番石榴果芭乐干番石榴片 干片】https://m.tb.cn/h.e6tAbYD?sm=0eedde 点击链接，再选择浏览器咑閞；或復·制这段描述/CLVXYTNKYUg/后到淘♂寳♀";
        String textInfo2 = "【草本善野生红心番石榴茶 番石榴干片番石榴果芭乐干番石榴片 干片】https://m.tb.cn/h.e6tAbYD?sm=0eedde 点击链接，再选择浏览器咑閞；或復·制这段描述.CLVXYTNKYUg.后到淘♂寳♀";
        String s1 = TbkUtils.extractTbCode(textInfo1);
        String s2 = TbkUtils.extractTbCode(textInfo2);
        System.err.println(s1);
        System.err.println(s2);
    }
}
