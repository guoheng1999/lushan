package edu.cuit.lushan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.cuit.lushan.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-29
 */
public interface UserMapper extends BaseMapper<User> {
    @Delete("DELETE FROM user WHERE email = #{email} ")
    boolean deleteByEmail(@Param("email") String email);
}
