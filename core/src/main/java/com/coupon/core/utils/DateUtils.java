package com.coupon.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ProjectName: workspace_coupon
 * @Package: com.coupon.core.utils
 * @ClassName: DateUtils
 * @Description: java类作用描述
 * @Author: Ryan.Gosling
 * @CreateDate: 2019/8/30/030 9:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/8/30/030 9:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DateUtils {
    /**
     * 获取查询日期周每一天的开始时间和结束时间
     * @return
     */
    private static Map<String, Map<String, Date>> setWeekRange(Date date){
        //初始化日期容器
        String[] weekArr = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};
        List<String> weekList = Arrays.asList(weekArr);
        Map<String, Map<String, Date>> weekMap = new LinkedHashMap<>();
        //初始化calendar日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for (String weekStr : weekList) {
            Date startTime = calendar.getTime();
            calendar.add(Calendar.DAY_OF_WEEK, 1);
            Date endTime = calendar.getTime();
            Map<String, Date> map = new HashMap<>();
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            weekMap.put(weekStr, map);
        }
        return weekMap;
    }

    public static void main(String[] args) throws ParseException {
        String dateStr = "2019-8-3";
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sDateFormat.parse(dateStr);

        Map<String, Map<String, Date>> stringMapMap = setWeekRange(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        for (String s : stringMapMap.keySet()) {
            Map<String, Date> stringDateMap = stringMapMap.get(s);
            System.out.println(s+"__"+sdf.format(stringDateMap.get("startTime"))+"__"+sdf.format(stringDateMap.get("endTime")));
        }
    }
}
