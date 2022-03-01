package edu.cuit.lushan.service;

import edu.cuit.lushan.entity.UserProof;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Guoheng
 * @since 2022-02-26
 */
public interface IUserProofService extends IService<UserProof> {
    UserProof getByUserProofFileName(String fileName);
    UserProof getByUserId(String userId);
}
