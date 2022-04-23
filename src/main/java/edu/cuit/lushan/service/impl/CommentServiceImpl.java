package edu.cuit.lushan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.cuit.lushan.entity.Comment;
import edu.cuit.lushan.mapper.CommentMapper;
import edu.cuit.lushan.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuit.lushan.vo.CommentAddVO;
import edu.cuit.lushan.vo.CommentSelectVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Guoheng
 * @since 2022-04-19
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Override
    public List<Comment> selectByVO(CommentSelectVO commentSelectVO) {
        QueryWrapper wrapper = new QueryWrapper();
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(commentSelectVO, true, true);
        if (stringObjectMap.isEmpty()) {
            return new LinkedList<>();
        }
        stringObjectMap.forEach((key, val)-> wrapper.eq(key, val));
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Comment save(CommentAddVO commentAddVO) {
        Comment comment = new Comment();
        BeanUtil.copyProperties(commentAddVO, comment);
        if (baseMapper.insert(comment) > 0) {
            return comment;
        }
        return null;
    }
}