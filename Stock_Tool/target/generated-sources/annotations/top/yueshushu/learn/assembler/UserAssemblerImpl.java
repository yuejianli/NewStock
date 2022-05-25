package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.UserDo;
import top.yueshushu.learn.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-25T10:42:46+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class UserAssemblerImpl implements UserAssembler {

    @Override
    public User doToEntity(UserDo userDo) {
        if ( userDo == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDo.getId() );
        user.setAccount( userDo.getAccount() );
        user.setName( userDo.getName() );
        user.setPassword( userDo.getPassword() );
        user.setToken( userDo.getToken() );
        user.setPhone( userDo.getPhone() );
        user.setEmail( userDo.getEmail() );
        user.setCreateTime( userDo.getCreateTime() );
        user.setUpdateTime( userDo.getUpdateTime() );
        user.setLastLoginTime( userDo.getLastLoginTime() );
        user.setStatus( userDo.getStatus() );
        user.setFlag( userDo.getFlag() );

        return user;
    }

    @Override
    public UserDo entityToDo(User user) {
        if ( user == null ) {
            return null;
        }

        UserDo userDo = new UserDo();

        userDo.setId( user.getId() );
        userDo.setAccount( user.getAccount() );
        userDo.setName( user.getName() );
        userDo.setPassword( user.getPassword() );
        userDo.setToken( user.getToken() );
        userDo.setPhone( user.getPhone() );
        userDo.setEmail( user.getEmail() );
        userDo.setCreateTime( user.getCreateTime() );
        userDo.setUpdateTime( user.getUpdateTime() );
        userDo.setLastLoginTime( user.getLastLoginTime() );
        userDo.setStatus( user.getStatus() );
        userDo.setFlag( user.getFlag() );

        return userDo;
    }
}
