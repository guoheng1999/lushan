package edu.cuit.lushan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.cuit.lushan.entity.UserProof;
import edu.cuit.lushan.mapper.UserProofMapper;
import edu.cuit.lushan.service.IUserProofService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Guoheng
 * @since 2022-02-26
 */
@Service
public class UserProofServiceImpl extends ServiceImpl<UserProofMapper, UserProof> implements IUserProofService {

    @Override
    public UserProof getByUserProofFileName(String fileName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("file_name", fileName);
        return getBaseMapper().selectOne(queryWrapper);
    }

    @Override
    public UserProof getByUserId(String userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        return getBaseMapper().selectOne(queryWrapper);
    }
}
