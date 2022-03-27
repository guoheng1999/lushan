package edu.cuit.lushan.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import edu.cuit.lushan.annotation.DataLog;
import edu.cuit.lushan.annotation.WebLog;
import edu.cuit.lushan.entity.DataFile;
import edu.cuit.lushan.entity.Device;
import edu.cuit.lushan.service.IDataFileService;
import edu.cuit.lushan.service.IDeviceService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.utils.UserAgentUtil;
import edu.cuit.lushan.vo.DataFileVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 数据文件表 前端控制器
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-29
 */
@RestController
@RequestMapping("/dataFile")
@Slf4j
@CrossOrigin
public class DataFileController {

    @Autowired
    IDataFileService dataFileService;
    @Autowired
    IDeviceService deviceService;
    @Autowired
    UserAgentUtil userAgentUtil;

    @ApiOperation(value = "获取所有数据文件信息", tags = {"数据文件管理"})
    @GetMapping("/")
    @DataLog
    public ResponseMessage getAll() {
        return ResponseMessage.success(dataFileService.list());
    }

    @ApiOperation(value = "获取一个数据文件信息", tags = {"数据文件管理"})
    @GetMapping("/{fileId}")
    @DataLog
    public ResponseMessage getOne(@PathVariable String fileId) {

        DataFile dataFile = dataFileService.getById(fileId);
        return ResponseMessage.success(dataFile);
    }

    @ApiOperation(value = "通过文件名获取一个数据文件信息", tags = {"数据文件管理"})
    @GetMapping("/name/{dataFileName}")
    @DataLog
    @WebLog
    public ResponseMessage getByFileName(@PathVariable String dataFileName) {
        DataFile dataFile = dataFileService.getOneByDataFileName(dataFileName);
        if (dataFile == null) {
            return ResponseMessage.errorMsg(2404, "Data file is not found!");
        }
        return ResponseMessage.success(dataFile);
    }

    @ApiOperation(value = "通过文件名修改一个数据文件信息", tags = {"数据文件管理"})
    @PutMapping("/name/{dataFileName}")
    @DataLog
    @WebLog
    public ResponseMessage updateByFileName(@PathVariable String dataFileName, @RequestBody DataFileVO dataFileVO) {
        // 前端未传入dataFileVO对象，直接返回
        if (dataFileVO == null) {
            return ResponseMessage.errorMsg(2500, "DataFile is not allowed null!");
        }
        // 根据文件名没有找到DataFile，直接返回
        DataFile dataFile = dataFileService.getOneByDataFileName(dataFileName);
        if (dataFile == null) {
            return ResponseMessage.errorMsg(2404, "Data file is not found!");
        }
        // 若deviceId不为null值，表示需要修改device信息，因此查询device是否存在。
        if (dataFileVO.getDeviceId() != null && deviceService.getById(dataFileVO.getDeviceId()) == null) {
            return ResponseMessage.errorMsg(2404, "Device is not found!");
        }
        // 根据业务需要，该接口不允许修改文件名。
        if (dataFileVO.getFileName() != null && !dataFile.getFileName().equals(dataFileVO.getFileName())) {
            return ResponseMessage.errorMsg(2500, "File name can not be changed！");
        }
        BeanUtil.copyProperties(dataFileVO, dataFile, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (dataFileService.updateById(dataFile)) {
            return ResponseMessage.success(dataFile);
        } else {
            return ResponseMessage.errorMsg(2500, "Server error!", dataFileVO);
        }
    }


    @ApiOperation(value = "添加数据文件信息", tags = {"数据文件管理"})
    @PostMapping("/")
    @DataLog
    @WebLog
    public ResponseMessage register(@RequestBody DataFileVO dataFileVO) {

        if (dataFileVO == null || BeanUtil.hasNullField(dataFileVO)) {
            return ResponseMessage.errorMsg(2500, "Data file information must not be null!", dataFileVO);
        }
        Device device = deviceService.getById(dataFileVO.getDeviceId());
        if (device == null) {
            return ResponseMessage.errorMsg(2404, "Device is not found!", dataFileVO);
        }
        DataFile dataFile = DataFile.builder()
                .dataName(dataFileVO.getDataName())
                .deviceId(dataFileVO.getDeviceId())
                .fileType(dataFileVO.getFileType())
                .fileName(dataFileVO.getFileName())
                .build();
        if (dataFileService.save(dataFile)) {
            return ResponseMessage.success(dataFileVO);
        } else {
            return ResponseMessage.errorMsg(2500, "Server error!", dataFileVO);
        }
    }


    @ApiOperation(value = "更改数据文件信息", tags = {"数据文件管理"})
    @PutMapping("/{fileId}")
    @DataLog
    @WebLog
    public ResponseMessage update(@PathVariable String fileId, @RequestBody DataFileVO dataFileVO, HttpServletRequest request) {
        DataFile myDataFile = dataFileService.getById(fileId);
        if (myDataFile == null) {
            return ResponseMessage.errorMsg(2404, "DataFile not found!", dataFileVO);
        }
        if (dataFileVO.getDeviceId() != null && deviceService.getById(dataFileVO.getDeviceId()) == null) {
            return ResponseMessage.errorMsg(2404, "Device is not found!");
        }
        // 根据业务需要，该接口不允许修改文件名。
        if (!myDataFile.getFileName().equals(dataFileVO.getFileName())) {
            return ResponseMessage.errorMsg(2500, "The file name cannot be modified!");
        }
        BeanUtil.copyProperties(dataFileVO, myDataFile, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true).setIgnoreProperties("fileName"));
        myDataFile.setModifyUserId(userAgentUtil.getUserId(request));

        if (dataFileService.updateById(myDataFile)) {
            return ResponseMessage.success(dataFileVO);
        } else {
            return ResponseMessage.errorMsg(2500, "Server error!", dataFileVO);
        }
    }

    @ApiOperation(value = "删除数据文件信息", tags = {"数据文件管理"})
    @DeleteMapping("/{fileId}")
    @DataLog
    @WebLog
    public ResponseMessage delete(@PathVariable String fileId, HttpServletRequest request) {
        DataFile dataFile = dataFileService.getById(fileId);
        if (dataFile == null) {
            return ResponseMessage.errorMsg(2404, "Data file not found!");
        }
        dataFile.setModifyUserId(userAgentUtil.getUserId(request));
        dataFileService.save(dataFile);

        if (dataFileService.removeById(fileId)) {
            return ResponseMessage.success(fileId);
        } else {
            return ResponseMessage.errorMsg(2500, "Server error!", fileId);
        }
    }
}

