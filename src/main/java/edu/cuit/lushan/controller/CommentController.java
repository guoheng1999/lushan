package edu.cuit.lushan.controller;


import cn.hutool.core.bean.BeanUtil;
import edu.cuit.lushan.annotation.RequireRoles;
import edu.cuit.lushan.annotation.WebLog;
import edu.cuit.lushan.entity.Comment;
import edu.cuit.lushan.enums.RoleEnum;
import edu.cuit.lushan.exception.MyRuntimeException;
import edu.cuit.lushan.service.ICommentService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.vo.CommentAddVO;
import edu.cuit.lushan.vo.CommentSelectVO;
import edu.cuit.lushan.vo.CommentVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Guoheng
 * @since 2022-04-19
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    ICommentService commentService;


    @GetMapping("/")
    @CrossOrigin
    @WebLog
    @RequireRoles(RoleEnum.MANAGER)
    @ApiOperation(value = "获取所有反馈信息", tags = {"数据反馈接口"})
    public ResponseMessage getAll(){
        List<CommentVO> result = new LinkedList<>();
        commentService.list().forEach( e -> {
            CommentVO commentVO = new CommentVO();
            BeanUtil.copyProperties(e, commentVO);
            result.add(commentVO);
        });
        if (result.isEmpty()){
            return ResponseMessage.successCodeMsgData(2000, "暂无数据信息反馈!", result);
        }
        return ResponseMessage.success(result);
    }

    @GetMapping("/email")
    @CrossOrigin
    @WebLog
    @RequireRoles(RoleEnum.MANAGER)
    @ApiOperation(value = "通过Email获取反馈信息", tags = {"数据反馈接口"})
    public ResponseMessage getByEmail(@RequestBody CommentSelectVO commentSelectVO){
        List<Comment> commentList = commentService.selectByVO(commentSelectVO);
        if (commentList.isEmpty()){
            return ResponseMessage.successCodeMsgData(2000, "暂无数据信息反馈!", commentSelectVO);
        }
        List<CommentVO> result = new LinkedList<>();
        commentList.forEach(e -> {
            CommentVO commentVO = new CommentVO();
            BeanUtil.copyProperties(e, commentVO);
            result.add(commentVO);
        });
        return ResponseMessage.success(result);
    }
    @PostMapping("/")
    @CrossOrigin
    @WebLog
    @RequireRoles(RoleEnum.VIP)
    @ApiOperation(value = "添加反馈信息", tags = {"数据反馈接口"})
    public ResponseMessage add(@RequestBody CommentAddVO commentAddVO){
        System.out.println(commentAddVO);
        if (BeanUtil.hasNullField(commentAddVO)){
            throw new MyRuntimeException("评论信息不可为空!", commentAddVO);
        }
        Comment comment = commentService.save(commentAddVO);
        if (comment != null) {
            return ResponseMessage.success(comment);
        }else {
            throw new MyRuntimeException("服务器异常!", commentAddVO);
        }
    }
/*
    @DeleteMapping("/")
    @CrossOrigin
    @WebLog
    @RequireRoles(RoleEnum.VIP)
    public ResponseMessage delete(){
        return;
    }

 */
    @PutMapping("/isRead")
    @CrossOrigin
    @WebLog
    @RequireRoles(RoleEnum.MANAGER)
    @ApiOperation(value = "修改反馈信息", tags = {"数据反馈接口"})
    public ResponseMessage update(@RequestBody Integer id){
        if (id == null){
            throw new MyRuntimeException("id can not be null!");
        }
        Comment comment = commentService.getById(id);
        if (comment == null) {
            throw new MyRuntimeException("current comment id is not found!");
        }
        comment.setIsRead(1);
        if (commentService.updateById(comment)) {
            return ResponseMessage.success(id);
        }else {
            throw new MyRuntimeException("服务器异常!", id);
        }
    }
}

