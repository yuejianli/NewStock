package top.yueshushu.learn.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.mode.ro.IdRo;
import top.yueshushu.learn.mode.vo.MenuVo;
import top.yueshushu.learn.pojo.Menu;
import top.yueshushu.learn.mapper.MenuMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public OutputResult getMenuListByUid(Integer userId) {

        //如果不为空的话，则进行转换后返回
        return OutputResult.success(
                listMenuListByUid(userId)
        );
    }

    @Override
    public List<MenuVo> listMenuListByUid(Integer userId) {
        List<MenuVo> menuVoList = new ArrayList<>();
        //获取所有的权限
        List<Menu> menuList = menuMapper.listPermissionByUidAndRid(
                userId,null
        );
        if(CollectionUtils.isEmpty(menuList)){
           return menuVoList;
        }
        //进行处理
        menuList.stream().forEach(
                n->{
                    MenuVo menuVo = new MenuVo();
                    BeanUtils.copyProperties(n,menuVo);
                    menuVoList.add(menuVo);
                }
        );
        return menuVoList;
    }
}
