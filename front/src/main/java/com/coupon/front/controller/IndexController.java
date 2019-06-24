package com.coupon.front.controller;

import com.alibaba.fastjson.JSON;
import com.coupon.business.service.MiaoYouJuanService;
import com.coupon.business.service.TuLingService;
import com.coupon.front.robot.MyRobot;
import io.github.biezhi.wechat.api.constant.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.robot.controller
 * @ClassName: IndexController
 * @Description: 项目的主入口,调用登录控制台返回登录二维码
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/18/018 10:57
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/18/018 10:57
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/index")
@Slf4j
public class IndexController {

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
     * 淘口令测试
     * @param info
     * @return
     */
    @RequestMapping(value = "/code")
    @ResponseBody
    public String code(String info) {
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
    public String all(String info) {
        try {
            String getgyurlbyall = miaoYouJuanService.getgyurlbyall(info);
            return getgyurlbyall;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().toString();
        }
    }

    /**
     * 登录
     * @return
     */
    @RequestMapping(value = "/login")
    public void login() {
        new MyRobot(Config.me().autoLogin(false).showTerminal(true)).start();
    }
}
