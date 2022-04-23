package edu.cuit.lushan.service;

import edu.cuit.lushan.entity.CommentFile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Guoheng
 * @since 2022-04-19
 */
public interface ICommentFileService extends IService<CommentFile> {

    List<CommentFile> selectByCommentId(Integer commentId);
    CommentFile selectByFileName(String fileName);
}
