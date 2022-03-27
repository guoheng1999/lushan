package edu.cuit.lushan.thread;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.service.IUserProofService;
import edu.cuit.lushan.service.IUserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserOperateThread implements Runnable {
    private String email;
    IUserProofService userProofService;
    IUserService userService;


    @Override
    public void run() {
        try {
            User user = userService.selectByEmail(this.email);
            if (user == null) {
                return;
            }
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("user_id", user.getId());
            userProofService.remove(wrapper);
            userService.deleteByEmail(user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}