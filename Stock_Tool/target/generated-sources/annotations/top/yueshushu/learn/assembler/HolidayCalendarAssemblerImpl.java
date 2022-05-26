package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.HolidayCalendarDo;
import top.yueshushu.learn.entity.HolidayCalendar;
import top.yueshushu.learn.mode.vo.HolidayCalendarVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-25T11:30:44+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class HolidayCalendarAssemblerImpl implements HolidayCalendarAssembler {

    @Override
    public HolidayCalendar doToEntity(HolidayCalendarDo holidayCalendarDo) {
        if ( holidayCalendarDo == null ) {
            return null;
        }

        HolidayCalendar holidayCalendar = new HolidayCalendar();

        holidayCalendar.setId( holidayCalendarDo.getId() );
        holidayCalendar.setHolidayDate( holidayCalendarDo.getHolidayDate() );
        holidayCalendar.setCurrYear( holidayCalendarDo.getCurrYear() );
        holidayCalendar.setDateType( holidayCalendarDo.getDateType() );

        return holidayCalendar;
    }

    @Override
    public HolidayCalendarDo entityToDo(HolidayCalendar holidayCalendar) {
        if ( holidayCalendar == null ) {
            return null;
        }

        HolidayCalendarDo holidayCalendarDo = new HolidayCalendarDo();

        holidayCalendarDo.setId( holidayCalendar.getId() );
        holidayCalendarDo.setHolidayDate( holidayCalendar.getHolidayDate() );
        holidayCalendarDo.setCurrYear( holidayCalendar.getCurrYear() );
        holidayCalendarDo.setDateType( holidayCalendar.getDateType() );

        return holidayCalendarDo;
    }

    @Override
    public HolidayCalendarVo entityToVo(HolidayCalendar holidayCalendar) {
        if ( holidayCalendar == null ) {
            return null;
        }

        HolidayCalendarVo holidayCalendarVo = new HolidayCalendarVo();

        holidayCalendarVo.setId( holidayCalendar.getId() );
        holidayCalendarVo.setHolidayDate( holidayCalendar.getHolidayDate() );
        holidayCalendarVo.setCurrYear( holidayCalendar.getCurrYear() );
        holidayCalendarVo.setDateType( holidayCalendar.getDateType() );

        return holidayCalendarVo;
    }
}
