package edu.cuit.lushan.controller;


import edu.cuit.lushan.annotation.WebLog;
import edu.cuit.lushan.entity.SysLog;
import edu.cuit.lushan.service.ISysLogService;
import edu.cuit.lushan.utils.ResponseMessage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-29
 */
@RestController
@RequestMapping("/sysLog")
@CrossOrigin
public class SysLogController {

    @Autowired
    ISysLogService sysLogDetailService;

    @ApiOperation(value = "获取所有接口调用日志", tags = {"接口调用日志"})
    @GetMapping("/")
//    @WebLog
    public ResponseMessage getAll() {
        return ResponseMessage.success(sysLogDetailService.list());
    }

    @ApiOperation(value = "获取单个接口调用日志", tags = {"接口调用日志"})
    @GetMapping("/{logId}")
//    @WebLog
    public ResponseMessage getOne(@PathVariable String logId) {
        SysLog sysLog = sysLogDetailService.getById(logId);
        return ResponseMessage.success(sysLog);
    }

    @ApiOperation(value = "添加接口调用日志", tags = {"接口调用日志"})
    @PostMapping("/")
//    @WebLog
    public ResponseMessage add(@RequestBody SysLog sysLog) {
        if (sysLogDetailService.save(sysLog)) {
            return ResponseMessage.success(sysLog);
        } else {
            return ResponseMessage.success();
        }
    }

    @ApiOperation(value = "更改接口调用日志", tags = {"接口调用日志"})
    @PutMapping("/{logId}")
//    @WebLog
    public ResponseMessage update(@PathVariable String logId, @RequestBody SysLog sysLog) {
        //   暂未实现
        return ResponseMessage.success(sysLog);
    }

    @ApiOperation(value = "删除接口调用日志", tags = {"接口调用日志"})
    @DeleteMapping("/{logId}")
//    @WebLog
    public ResponseMessage delete(@PathVariable String logId) {
        sysLogDetailService.removeById(logId);
        return ResponseMessage.success(logId);
    }
}

