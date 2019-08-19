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

import java.util.*;

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
            Map<String, String> getitemgyurlbytpwd = miaoYouJuanService.getitemgyurlbytpwd(info);
            return JSON.toJSONString(getitemgyurlbytpwd);
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
            Map<String, String> getgyurlbyall = miaoYouJuanService.getgyurlbyall(info);
            return JSON.toJSONString(getgyurlbyall);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().toString();
        }
    }

    @RequestMapping(value = "/jd")
    @ResponseBody
    public String jd(String info) {
        try {
            if(TbkUtils.isJdLink(info)) {
                String jdRebateMessage = miaoYouJuanService.getjdunionitems(info);
                return jdRebateMessage;
            }
            return "查询失败";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().toString();
        }
    }

    @RequestMapping(value = "/redis")
    @ResponseBody
    public String shareConvetRedis(String text) {
        try {
            String itemMoreUrl = miaoYouJuanService.getItemMoreUrl(text);
            return itemMoreUrl;
        }catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().toString();
        }
    }

    public static final Integer[] arr = {1,2,3,4,5,6,7,8,9};

    public static int initTag() {
        int ran1 = new Random().nextInt(10);
        if(Arrays.asList(arr).contains(ran1)) {
            return initTag();
        }else {
            return ran1;
        }
    }


    public static void main(String[] args) {
        String textInfo8 = "【40个加粗衣架浸塑料成人防滑晾挂钩衣架子批发家用无痕衣撑衣服架】https://m.tb.cn/h.eTAIdtt?sm=6386f6 点击链接，再选择浏览器咑閞；或復·制这段描述€HNQQYT8LvGh€后到淘♂寳♀";
        String text = "【红蜻蜓男鞋官方旗舰店男士皮鞋英伦风潮流商务正装真皮耐磨皮鞋子】\n" +
                "【原价/现价】579元/299元\n" +
                "【券后价】289.0元\n" +
                "【最高返利】6.88元\n" +
                "【优惠券】满199元减10元\n" +
                "【电脑下单地址】\n" +
                "https://s.click.taobao.com/SiVjJ3w\n" +
                "------------------------------\n" +
                "手机復·制这段信息,￥gJUvYQdNtD5￥打开【淘♂寳♀】领取优惠券并下单!\n" +
                "------------------------------\n" +
                "【海量优惠】尽在 http://www.findcoupon.top";

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
        /*String textInfo1 = "【草本善野生红心番石榴茶 番石榴干片番石榴果芭乐干番石榴片 干片】https://m.tb.cn/h.e6tAbYD?sm=0eedde 点击链接，再选择浏览器咑閞；或復·制这段描述/CLVXYTNKYUg/后到淘♂寳♀";
        String textInfo2 = "【草本善野生红心番石榴茶 番石榴干片番石榴果芭乐干番石榴片 干片】https://m.tb.cn/h.e6tAbYD?sm=0eedde 点击链接，再选择浏览器咑閞；或復·制这段描述.CLVXYTNKYUg.后到淘♂寳♀";
        String s1 = TbkUtils.extractTbCode(textInfo1);
        String s2 = TbkUtils.extractTbCode(textInfo2);
        System.err.println(s1);
        System.err.println(s2);*/
        /*String text1 = "https://item.jd.com/44076405945.html#none";
        String text2 = "https://item.m.jd.com/product/51138623981.html?wxa_abtest=o&utm_source=iosapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=CopyURL&ad_od=share";*/

        /*TreeMap<String, String> treeMap = new TreeMap<>(new Comparator<String>() {
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

        treeMap.put("1000", "10");
        treeMap.put("10.25", "22");
        treeMap.put("11.00", "333");
        treeMap.put("1", "444");
        treeMap.put("5000", "555");

        double a = 1300d;

        for (String quota : treeMap.keySet()) {
            if(a >= Double.valueOf(quota)) {
                System.out.println(a+"----"+quota);
                break;
            }
        }*/
    }
}
