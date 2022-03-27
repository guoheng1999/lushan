package edu.cuit.lushan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.cuit.lushan.entity.UserProof;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Guoheng
 * @since 2022-02-26
 */
public interface IUserProofService extends IService<UserProof> {
    UserProof getByUserProofFileName(String fileName);

    List<UserProof> getByUserId(Integer userId);
}
