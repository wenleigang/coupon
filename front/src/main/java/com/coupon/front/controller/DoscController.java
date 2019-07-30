package com.coupon.front.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.front.controller
 * @ClassName: DoscController
 * @Description: 帮助文档类controller
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/7/27/027 11:03
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/7/27/027 11:03
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Controller
@RequestMapping("/docs")
@Slf4j
public class DoscController {

    /**
     * 跳转到淘宝返利教程页面 淘宝app入口
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/tbAppHelp", method = RequestMethod.GET)
    public ModelAndView tbAppHelpUi(ModelAndView modelAndView) {
        modelAndView.setViewName("docs/tb_app_help");
        return modelAndView;
    }

    @RequestMapping(value = "/tbQrcodeHelp", method = RequestMethod.GET)
    public ModelAndView tbQrcodeHelpUi(ModelAndView modelAndView) {
        modelAndView.setViewName("docs/tb_qrcode_help");
        return modelAndView;
    }

    /**
     * 跳转到京东返利教程页面 京东app入口
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/jdAppHelp", method = RequestMethod.GET)
    public ModelAndView jdAppHelp(ModelAndView modelAndView) {
        modelAndView.setViewName("docs/jd_app_help");
        return modelAndView;
    }

}
