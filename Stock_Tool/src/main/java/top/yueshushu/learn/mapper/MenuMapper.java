package top.yueshushu.learn.mapper;

import org.apache.ibatis.annotations.Param;
import top.yueshushu.learn.mode.vo.MenuVo;
import top.yueshushu.learn.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author 岳建立  自定义的
 * @since 2022-01-02
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据用户编号和角色编号，查询对应的菜单id
     *
     * @param userId
     * @param roleId
     * @return
     */
    List<Menu> listPermissionByUidAndRid(@Param("userId") Integer userId, @Param("roleId") Integer roleId);


}
