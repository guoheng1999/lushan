package edu.cuit.lushan;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONUtil;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.vo.UserInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LushanApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void testHutool(){
        UserInfoVO releaseVersion = new UserInfoVO();
        releaseVersion.setEmail("sss");
        User old = new User();
        old.setId(1);
        old.setPassword("phone");
        copyIgnoreNullValue(releaseVersion, old);
        System.out.println(JSONUtil.toJsonStr(old));
    }

    private void copyIgnoreNullValue(UserInfoVO releaseVersion, User old) {
        BeanUtil.copyProperties(releaseVersion,old, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));


    }
}
