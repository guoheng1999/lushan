package edu.cuit.lushan.controller;


import edu.cuit.lushan.entity.DownloadLog;
import edu.cuit.lushan.service.IDownloadLogService;
import edu.cuit.lushan.utils.ResponseMessage;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-29
 */
@RestController
@RequestMapping("/downloadLog")
@RequiresRoles({"USER"})
@CrossOrigin
public class DownloadLogController {

    @Autowired
    IDownloadLogService downloadLogService;

    @ApiOperation(value = "获取所有下载日志", tags = {"文件下载日志"})
    @GetMapping("/")
    public ResponseMessage getAll() {
        return ResponseMessage.success(downloadLogService.list());
    }

    @ApiOperation(value = "获取一个下载日志", tags = {"文件下载日志"})
    @GetMapping("/{downloadId}")
    public ResponseMessage getOne(@PathVariable String downloadId) {
        DownloadLog downloadLog = downloadLogService.getById(downloadId);
        return ResponseMessage.success(downloadLog);
    }

    @ApiOperation(value = "添加下载日志", tags = {"文件下载日志"})
    @PostMapping("/")
    public ResponseMessage register(@RequestBody DownloadLog downloadLog) {
        if (downloadLogService.save(downloadLog)) {
            return ResponseMessage.success(downloadLog);
        } else {
            return ResponseMessage.success();
        }
    }

    @ApiOperation(value = "更改下载日志信息", tags = {"文件下载日志"})
    @PutMapping("/{downloadId}")
    public ResponseMessage update(@PathVariable String downloadId, @RequestBody DownloadLog downloadLog) {
        //   暂未实现
        return ResponseMessage.success(downloadLog);
    }

    @ApiOperation(value = "删除下载日志", tags = {"文件下载日志"})
    @DeleteMapping("/{downloadId}")
    public ResponseMessage delete(@PathVariable String downloadId) {
        downloadLogService.removeById(downloadId);
        return ResponseMessage.success(downloadId);
    }
}

