package edu.cuit.lushan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.cuit.lushan.entity.SysLogDetail;
import edu.cuit.lushan.service.ISysLogDetailService;
import edu.cuit.lushan.utils.ResponseMessage;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 日志详情表 前端控制器
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-29
 */
@RestController
@RequestMapping("/sysLogDetail")
@RequiresRoles({"USER"})
@CrossOrigin
public class SysLogDetailController {
    @Autowired
    ISysLogDetailService sysLogDetailService;

    @ApiOperation(value = "查询所有日志详情", notes = "", tags = {"日志详情管理"})
    @GetMapping("/")
    public ResponseMessage all() {
        return ResponseMessage.success(sysLogDetailService.list());
    }

    @ApiOperation(value = "查询单个日志的详情", tags = {"日志详情管理"})
    @GetMapping("/{logId}")
    public ResponseMessage one(@PathVariable String logId) {
        List<SysLogDetail> sysLogDetailList = sysLogDetailService.getBaseMapper().selectList(new QueryWrapper<SysLogDetail>().eq("log_id", logId));
        return ResponseMessage.success(sysLogDetailList);
    }
}

