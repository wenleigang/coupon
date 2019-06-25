package com.coupon.front.robot;

import com.coupon.business.service.Impl.MiaoYouJuanServiceImpl;
import com.coupon.business.service.MiaoYouJuanService;
import com.coupon.core.utils.TbkUtils;
import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.annotation.Bind;
import io.github.biezhi.wechat.api.constant.Config;
import io.github.biezhi.wechat.api.enums.MsgType;
import io.github.biezhi.wechat.api.model.WeChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.front.robot
 * @ClassName: MyRobot
 * @Description: 自定义微信机器人~~~~~~~~
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/21/021 17:39
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/21/021 17:39
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Slf4j
public class MyRobot extends WeChatBot {
    public MyRobot(Config config) {
        super(config);
    }

    /**
     * 消息监听
     * @param message
     */
    @Bind(msgType = MsgType.TEXT)//默认监听好友/群文本类消息
    public void handleMessage(WeChatMessage message) {
        try {
            //实例化喵有劵接口对象
            MiaoYouJuanService miaoYouJuanService = new MiaoYouJuanServiceImpl();
            //判断发送消息的用户标识存在
            if(StringUtils.isNotBlank(message.getFromUserName())) {
                //微信消息文本内容
                String textInfo = message.getText();
                if(TbkUtils.hasTpwdcode(textInfo)) {//1.淘口令类型【淘宝商品分享链接文字包含有淘口令查询优惠】
                    //发送提示消息
                    this.sendMsg(message.getFromUserName(), "正在查询优惠券,请稍候...");
                    String couponMessage = miaoYouJuanService.getitemgyurlbytpwd(textInfo);
                    if(couponMessage != null) {
                        this.sendMsg(message.getFromUserName(), couponMessage);
                    }else {
                        this.sendMsg(message.getFromUserName(), "该商品已下架或暂无优惠券");
                    }
                    return;
                }else {//其他消息全部进入万能高佣转链API接口中查询
                    //输入 . 会查询出一款商品,是个bug吧!!!!!!!!!!!!!!!!!!!!!!!!!
                    if(TbkUtils.isTaobaoLink(textInfo)) {//淘宝链接,二合一链接等进入接口查询
                        //发送提示消息
                        this.sendMsg(message.getFromUserName(), "正在查询优惠券,请稍候...");
                        String couponMessage = miaoYouJuanService.getgyurlbyall(textInfo);
                        if(couponMessage != null) {
                            this.sendMsg(message.getFromUserName(), couponMessage);
                        }else {
                            this.sendMsg(message.getFromUserName(), "该商品已下架或暂无优惠券");
                        }
                        return;
                    }
                    //其他消息不做任何处理
                    log.info("不自动回复消息:"+textInfo);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            //发生异常,给文件传输工具发微信,通知开发者
            StringBuffer sb = new StringBuffer();
            if(StringUtils.isNotBlank(message.getFromNickName())) {
                sb.append("【微信昵称:】").append(message.getFromNickName()).append("\n");
            }
            if(StringUtils.isNotBlank(message.getFromRemarkName())) {
                sb.append("【微信备注:】").append(message.getFromRemarkName()).append("\n");
            }
            if(StringUtils.isNotBlank(message.getText())) {
                sb.append("【信息主体:】").append(message.getText()).append("\n");
            }
            sb.append("【异常类:】").append(e.getClass().toString()).append("\n");
            sb.append("【异常信息:】").append(e.getMessage()).append("\n");
            this.sendMsgToFileHelper(sb.toString());
        }
    }
}
