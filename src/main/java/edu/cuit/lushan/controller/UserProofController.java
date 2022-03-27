package edu.cuit.lushan.controller;

import edu.cuit.lushan.annotation.WebLog;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.entity.UserProof;
import edu.cuit.lushan.service.IUserProofService;
import edu.cuit.lushan.service.IUserService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.vo.UserProofVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Guoheng
 * @since 2022-02-26
 */
@RestController
@RequestMapping("/userProof")
@CrossOrigin
public class UserProofController {

    @Autowired
    IUserProofService userProofService;
    @Autowired
    IUserService userService;

    @ApiOperation(value = "获取所有用户的审核资料", tags = {"用户审核"})
    @GetMapping("/")
    @WebLog
    public ResponseMessage getAll() {
        return ResponseMessage.success(userProofService.list());
    }

    @ApiOperation(value = "获取单个审核资料", tags = {"用户审核"})
    @GetMapping("/{userProofId}")
    @WebLog
    public ResponseMessage getOne(@PathVariable String userProofId) {
        UserProof userProof = userProofService.getById(userProofId);
        return ResponseMessage.success(userProof);
    }

    @ApiOperation(value = "获取单个用户的审核资料", tags = {"用户审核"})
    @GetMapping("/user/{email}")
    @WebLog
    public ResponseMessage getByUserId(@PathVariable String email) {

        User user = userService.selectByEmail(email);
        if (user == null) {
            return ResponseMessage.notFound(email);
        }

        List<UserProof> userProofList = userProofService.getByUserId(user.getId());
        List<UserProofVO> results = new LinkedList<>();
        userProofList.forEach((item) -> {
            results.add(UserProofVO.builder().userId(item.getUserId())
                    .filePathName(item.getFileName())
                    .fileType(item.getFileType()).build());
        });
        return ResponseMessage.success(results);
    }

    @ApiOperation(value = "添加用户的审核资料", tags = {"用户审核"})
    @PostMapping("/")
    @WebLog
    public ResponseMessage add(@RequestBody UserProof userProof) {
        if (userProofService.save(userProof)) {
            return ResponseMessage.success(userProof);
        } else {
            return ResponseMessage.success();
        }
    }

    @ApiOperation(value = "修改用户的审核资料", tags = {"用户审核"})
    @PutMapping("/{userProofId}")
    @WebLog
    public ResponseMessage update(@PathVariable String userProofId, @RequestBody UserProof userProof) {
        //   暂未实现
        return ResponseMessage.success(userProof);
    }

    @ApiOperation(value = "删除用户的审核资料", tags = {"用户审核"})
    @DeleteMapping("/{userProofId}")
    @WebLog
    public ResponseMessage delete(@PathVariable String userProofId) {
        userProofService.removeById(userProofId);
        return ResponseMessage.success(userProofId);
    }
}

