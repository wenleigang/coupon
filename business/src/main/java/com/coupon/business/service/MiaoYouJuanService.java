package com.coupon.business.service;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.business.service
 * @ClassName: MiaoYouJuanService
 * @Description: 喵有劵平台接口
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/21/021 17:44
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/21/021 17:44
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface MiaoYouJuanService {

    //高佣转链接API(淘口令)
    String getitemgyurlbytpwd(String textInfo) throws Exception;

    //万能高佣转链API(任意文字分享格式)
    String getgyurlbyall(String textInfo) throws Exception;

}
