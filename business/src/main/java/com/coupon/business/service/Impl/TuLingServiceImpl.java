package com.coupon.business.service.Impl;

import com.coupon.business.service.TuLingService;
import com.coupon.core.common.Constants;
import com.coupon.core.utils.HttpClientUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.service.Impl
 * @ClassName: TuLingServiceImpl
 * @Description: java类作用描述
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/6/18/018 10:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/18/018 10:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
@Transactional
public class TuLingServiceImpl implements TuLingService {

    @Override
    public String TuLingTest(String info) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", Constants.TULING_APIKEY);
        map.put("info", info);
        String s = HttpClientUtils.sendPost(Constants.TULING_URL, map);
        return s;
    }
}
