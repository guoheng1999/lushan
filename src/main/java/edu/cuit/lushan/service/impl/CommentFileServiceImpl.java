package edu.cuit.lushan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.cuit.lushan.entity.CommentFile;
import edu.cuit.lushan.mapper.CommentFileMapper;
import edu.cuit.lushan.service.ICommentFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Guoheng
 * @since 2022-04-19
 */
@Service
public class CommentFileServiceImpl extends ServiceImpl<CommentFileMapper, CommentFile> implements ICommentFileService {

    @Override
    public List<CommentFile> selectByCommentId(Integer commentId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("comment_id", commentId);
        List list = baseMapper.selectList(wrapper);
        return list;
    }

    @Override
    public CommentFile selectByFileName(String fileName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("file_name", fileName);
        CommentFile commentFile = baseMapper.selectOne(wrapper);
        return commentFile;
    }
}
