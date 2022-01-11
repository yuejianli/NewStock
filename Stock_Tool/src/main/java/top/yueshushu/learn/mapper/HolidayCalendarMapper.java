package top.yueshushu.learn.mapper;

import org.apache.ibatis.annotations.Param;
import top.yueshushu.learn.pojo.HolidayCalendar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 法定假期表(只写入法定的类型) Mapper 接口
 * </p>
 *
 * @author 岳建立  自定义的
 * @since 2022-01-02
 */
public interface HolidayCalendarMapper extends BaseMapper<HolidayCalendar> {
    /**
     * 查询今年的假期信息
     *
     * @param year
     * @return
     */
    List<HolidayCalendar> selectByYear(@Param("year") Integer year);

    /**
     * 根据年进行删除
     *
     * @param year
     */
    void deleteByYear(@Param("year") Integer year);

    /**
     * 根据年份获取假期数据
     *
     * @param year
     * @return int
     * @date 2022/1/11 20:00
     * @author zk_yjl
     */
    int countByYear(@Param("year") Integer year);
}
