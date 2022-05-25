package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.MenuDo;
import top.yueshushu.learn.entity.Menu;
import top.yueshushu.learn.mode.vo.MenuVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-25T10:42:46+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class MenuAssemblerImpl implements MenuAssembler {

    @Override
    public Menu doToEntity(MenuDo menuDo) {
        if ( menuDo == null ) {
            return null;
        }

        Menu menu = new Menu();

        menu.setId( menuDo.getId() );
        menu.setName( menuDo.getName() );
        menu.setPid( menuDo.getPid() );
        menu.setShowIndex( menuDo.getShowIndex() );
        menu.setUrl( menuDo.getUrl() );
        menu.setShowType( menuDo.getShowType() );
        menu.setCreateTime( menuDo.getCreateTime() );
        menu.setUpdateTime( menuDo.getUpdateTime() );
        menu.setFlag( menuDo.getFlag() );

        return menu;
    }

    @Override
    public MenuDo entityToDo(Menu menu) {
        if ( menu == null ) {
            return null;
        }

        MenuDo menuDo = new MenuDo();

        menuDo.setId( menu.getId() );
        menuDo.setName( menu.getName() );
        menuDo.setPid( menu.getPid() );
        menuDo.setShowIndex( menu.getShowIndex() );
        menuDo.setUrl( menu.getUrl() );
        menuDo.setShowType( menu.getShowType() );
        menuDo.setCreateTime( menu.getCreateTime() );
        menuDo.setUpdateTime( menu.getUpdateTime() );
        menuDo.setFlag( menu.getFlag() );

        return menuDo;
    }

    @Override
    public MenuVo entityToVo(Menu menu) {
        if ( menu == null ) {
            return null;
        }

        MenuVo menuVo = new MenuVo();

        menuVo.setId( menu.getId() );
        menuVo.setName( menu.getName() );
        menuVo.setPid( menu.getPid() );
        menuVo.setShowIndex( menu.getShowIndex() );
        menuVo.setUrl( menu.getUrl() );
        menuVo.setShowType( menu.getShowType() );
        menuVo.setFlag( menu.getFlag() );

        return menuVo;
    }
}
