package edu.cuit.lushan.controller;


import edu.cuit.lushan.annotation.RequireRoles;
import edu.cuit.lushan.annotation.WebLog;
import edu.cuit.lushan.entity.Comment;
import edu.cuit.lushan.entity.CommentFile;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.entity.UserProof;
import edu.cuit.lushan.enums.RoleEnum;
import edu.cuit.lushan.exception.MyRuntimeException;
import edu.cuit.lushan.service.ICommentFileService;
import edu.cuit.lushan.service.ICommentService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.vo.CommentFileVO;
import edu.cuit.lushan.vo.UserProofVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
@RequestMapping("/commentFile")
@CrossOrigin
public class CommentFileController {

    @Autowired
    ICommentService commentService;
    @Autowired
    ICommentFileService commentFileService;

    @ApiOperation(value = "获取所有数据反馈信息文件", tags = {"数据反馈文件"})
    @GetMapping("/")
    @WebLog
    @RequireRoles(RoleEnum.MANAGER)
    @CrossOrigin
    public ResponseMessage getAll() {
        return ResponseMessage.success(commentFileService.list());
    }


    @ApiOperation(value = "获取单个数据反馈信息的所有文件", tags = {"数据反馈文件"})
    @GetMapping("/commentId/{commentId}")
    @WebLog
    @RequireRoles(RoleEnum.MANAGER)
    @CrossOrigin
    public ResponseMessage getByCommentId(@PathVariable Integer commentId) {
        if (commentId == null){
            throw new MyRuntimeException("commentId can not be null!");
        }
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            throw new MyRuntimeException("Current comment not found!");
        }
        List<CommentFileVO> result = new LinkedList<>();
        commentFileService.selectByCommentId(comment.getId()).forEach( e-> {
            CommentFileVO commentFileVO = CommentFileVO.builder()
                    .fileName(e.getFileName())
                    .commentId(e.getCommentId())
                    .fileType(e.getFileType())
                    .build();
            result.add(commentFileVO);
        });
        if (result.isEmpty()) {
            return ResponseMessage.success(commentId);
        }else {
            return ResponseMessage.success(result);
        }
    }

    @ApiOperation(value = "删除数据反馈信息文件", tags = {"数据反馈文件"})
    @DeleteMapping("/{commentId}")
    @WebLog
    @RequireRoles(RoleEnum.MANAGER)
    @CrossOrigin
    public ResponseMessage delete(@PathVariable Integer commentId) {
        if (commentId == null){
            throw new MyRuntimeException("commentId can not be null!");
        }
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            throw new MyRuntimeException("Current comment not found!");
        }
        List<CommentFile> commentFileList = commentFileService.selectByCommentId(comment.getId());
        return ResponseMessage.success(commentFileList);
    }

}

