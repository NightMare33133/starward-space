package top.starward.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.starward.entity.User;

/**
 * 🌌 用户持久层 Mapper
 */
@Mapper
public interface UserMapper {

    User findByUsername(@Param("username") String username);

    User findById(@Param("id") Long id);

    int insertUser(User user);

    int updateUser(User user);
}
