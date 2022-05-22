package top.yueshushu.learn.stock.util;

import cn.hutool.Hutool;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName:MyDateUtil
 * @Description TODO
 * @Author 岳建立
 * @Date 2021/11/12 22:14
 * @Version 1.0
 **/
public class MyDateUtil {
    /**
     * 当前时间是否在下午3点之后
     * @return 当前时间是否在下午3点之后
     */
    public static boolean after15Hour(){
        Date now= DateUtil.date();
        //组装一个下午3点的时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,15);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Date hour15 = calendar.getTime();
        if(now.before(hour15)){
            return false;
        }
        return true;
    }
    /**
     * 当前时间是否在9点30之前
     * @return 当前时间是否在9点30之前
     */
    public static boolean before930(){
        Date now= DateUtil.date();
        //组装一个下午3点的时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,30);
        calendar.set(Calendar.SECOND,0);
        Date hour930 = calendar.getTime();
        if(now.before(hour930)){
            return true;
        }
        return false;
    }

    /**
     * 当前时间是否在9点半到下午3点之间
     * @return 当前时间是否在9点半到下午3点之间
     */
    public static boolean between930And15(){
       return !before930()&&!after15Hour();
    }

    public static void main(String[] args) {
        System.out.println(before930());
    }
}
