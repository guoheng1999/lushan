package edu.cuit.lushan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.cuit.lushan.entity.User;

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
}
