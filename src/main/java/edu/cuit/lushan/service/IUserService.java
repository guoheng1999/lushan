package edu.cuit.lushan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.cuit.lushan.entity.User;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-29
 */
public interface IUserService extends IService<User> {
    User loginByEmail(String email, String password);

    User selectByEmail(String email);

    List<User> selectAllUnderReviewed();

    List<User> selectAllBanedUser();

    List<User> selectAllUser();

    boolean deleteByEmail(String email);
}
