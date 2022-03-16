package edu.cuit.lushan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.mapper.UserMapper;
import edu.cuit.lushan.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-29
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User loginByEmail(String email, String password) {
        User user = selectByEmail(email);
        if (user == null) return null;
        if (user.getAccountStatus() != 1){
            return null;
        }
        if (password.equals(user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public User selectByEmail(String email) {
        QueryWrapper wrapper = new QueryWrapper<User>();
        wrapper.eq("email", email);
        User user = this.baseMapper.selectOne(wrapper);
        return user;
    }
}
