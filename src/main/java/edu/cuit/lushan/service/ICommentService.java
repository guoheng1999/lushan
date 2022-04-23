package edu.cuit.lushan.service;

import edu.cuit.lushan.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.cuit.lushan.vo.CommentAddVO;
import edu.cuit.lushan.vo.CommentSelectVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Guoheng
 * @since 2022-04-19
 */
public interface ICommentService extends IService<Comment> {
    public List<Comment> selectByVO(CommentSelectVO commentSelectVO);
    public Comment save(CommentAddVO commentAddVO);
}
