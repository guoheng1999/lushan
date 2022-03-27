package edu.cuit.lushan.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import edu.cuit.lushan.annotation.WebLog;
import edu.cuit.lushan.entity.CurrentData;
import edu.cuit.lushan.service.ICurrentDataService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.vo.CurrentDataVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Guoheng
 * @since 2022-03-25
 */
@RestController
@RequestMapping("/currentData")
public class CurrentDataController {

    @Autowired
    ICurrentDataService currentDataService;

    @ApiOperation(value = "获取所有现代数据", tags = {"现代数据管理"})
    @GetMapping("/")
    @CrossOrigin
    public ResponseMessage getAll() {
        return ResponseMessage.success(currentDataService.list());
    }

    @ApiOperation(value = "按设备获取现代数据", tags = {"现代数据管理"})
    @GetMapping("/deviceId/")
    @CrossOrigin
    public ResponseMessage getByDeviceId(@RequestParam Integer deviceId) {
        if (deviceId == null || deviceId.equals("")){
            return ResponseMessage.nullError(deviceId);
        }
        List<CurrentData> list = currentDataService.getByDeviceId(deviceId);
        List<CurrentDataVO> result = new LinkedList<>();
        list.forEach(e -> {
            result.add(CurrentDataVO.builder()
                    .dataLevel(e.getDataLevel())
                    .dataName(e.getDataName())
                    .deviceId(e.getDeviceId())
                    .fileType(e.getFileType())
                    .logTime(e.getLogTime())
                    .build());
        });
        return ResponseMessage.success(result);
    }

    @ApiOperation(value = "按设备和数据级别获取现代数据", tags = {"现代数据管理"})
    @GetMapping("/deviceId/dataLevel")
    @CrossOrigin
    public ResponseMessage getByDeviceIdAndDataLevel(@RequestParam Integer deviceId, @RequestParam Integer dataLevel) {
        if (deviceId == null || deviceId.equals("")){
            return ResponseMessage.nullError(deviceId);
        }
        List<CurrentData> list = currentDataService.getByDeviceIdAndDataLevel(deviceId, dataLevel);
        List<CurrentDataVO> result = new LinkedList<>();
        list.forEach(e -> result.add(CurrentDataVO.builder()
                .dataLevel(e.getDataLevel())
                .dataName(e.getDataName())
                .deviceId(e.getDeviceId())
                .fileType(e.getFileType())
                .logTime(e.getLogTime())
                .build()));
        return ResponseMessage.success(result);
    }

    @ApiOperation(value = "按设备和数据级别获取现代数据的时间序列", tags = {"现代数据管理"})
    @GetMapping("/deviceId/dataLevel/times")
    @CrossOrigin
    public ResponseMessage getTimeListByDeviceIdAndDataLevel(@RequestParam Integer deviceId, @RequestParam Integer dataLevel) {
        if (deviceId == null || deviceId.equals("")){
            return ResponseMessage.nullError(deviceId);
        }
        List<CurrentData> list = currentDataService.getByDeviceIdAndDataLevel(deviceId, dataLevel);
        List<String> result = new LinkedList<>();
        list.forEach(e -> result.add(e.getLogTime()));
        return ResponseMessage.success(result);
    }

    @ApiOperation(value = "按设备和数据级别获取特定时间段的现代数据", tags = {"现代数据管理"})
    @GetMapping("/deviceId/dataLevel/edge")
    @CrossOrigin
    public ResponseMessage getByDeviceIdAndDataLevelWithFromDayEndDay(@RequestParam Integer deviceId,
                                                                      @RequestParam Integer dataLevel,
                                                                      @RequestParam String fromDay,
                                                                      @RequestParam String endDay) {
        if (deviceId == null || deviceId.equals("")){
            return ResponseMessage.nullError(deviceId);
        }
        List<CurrentData> list = currentDataService.getByDeviceIdAndDataLevelWithFromDayEndDay(deviceId, dataLevel, fromDay, endDay);
        List<String> result = new LinkedList<>();
        list.forEach(e -> result.add(e.getLogTime()));
        return ResponseMessage.success(result);
    }

    @ApiOperation(value = "按设备和数据级别获取特定年的现代数据", tags = {"现代数据管理"})
    @GetMapping("/deviceId/dataLevel/year")
    @CrossOrigin
    public ResponseMessage getByYear(@RequestParam Integer deviceId,
                                                                      @RequestParam Integer dataLevel,
                                                                      @RequestParam Integer year) {
        if (deviceId == null || deviceId.equals("")){
            return ResponseMessage.nullError(deviceId);
        }
        List<CurrentData> list = currentDataService.getByYear(deviceId, dataLevel, year);
        Set<String> result = new HashSet<>();
        list.forEach(e -> result.add(e.getLogTime()));
        return ResponseMessage.success(result);
    }




    @ApiOperation(value = "获取单个现代数据", tags = {"现代数据管理"})
    @GetMapping("/{dataName}")
    @CrossOrigin
    public ResponseMessage getByUserId(@PathVariable String dataName) {
        if (dataName == null || dataName.equals("")) {
            return ResponseMessage.nullError(dataName);
        }
        CurrentData currentData = currentDataService.getByName(dataName);
        if (currentData == null) {
            return ResponseMessage.notFound(dataName);
        }
        return ResponseMessage.success(currentData);
    }

    @ApiOperation(value = "添加现代数据", tags = {"现代数据管理"})
    @PostMapping("/")
    @CrossOrigin
    public ResponseMessage add(@RequestBody CurrentDataVO currentDataVO) {
        if (BeanUtil.hasNullField(currentDataVO)) {
            return ResponseMessage.nullError(currentDataVO);
        }
        CurrentData currentData = new CurrentData();
        System.err.println(currentData);
        System.err.println(currentDataVO);
        BeanUtil.copyProperties(currentDataVO, currentData, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        System.err.println(currentData);
        if (currentDataService.save(currentData)) {
            return ResponseMessage.success(currentDataVO);
        } else {
            return ResponseMessage.serverError(currentDataVO);
        }
    }

    @ApiOperation(value = "修改现代数据", tags = {"现代数据管理"})
    @PutMapping("/{currentDataName}")
    @CrossOrigin
    public ResponseMessage update(@PathVariable String currentDataName, @RequestBody CurrentDataVO currentDataVO) {
        if (StrUtil.isEmpty(currentDataName)) {
            return ResponseMessage.nullError(currentDataVO);
        }

        CurrentData currentData = currentDataService.getByName(currentDataName);
        if (currentData == null) {
            return ResponseMessage.notFound(currentDataName);
        }

        BeanUtil.copyProperties(currentDataVO, currentData, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (currentDataService.updateById(currentData)) {
            return ResponseMessage.success(currentDataVO);
        } else {
            return ResponseMessage.serverError(currentDataVO);
        }
    }

    @ApiOperation(value = "删除现代数据", tags = {"现代数据管理"})
    @DeleteMapping("/{currentDataName}")
    @CrossOrigin
    public ResponseMessage delete(@PathVariable String currentDataName) {
        if (StrUtil.isEmpty(currentDataName)) {
            return ResponseMessage.nullError(currentDataName);
        }

        CurrentData currentData = currentDataService.getByName(currentDataName);
        if (currentData == null) {
            return ResponseMessage.notFound(currentDataName);
        }
        if (currentDataService.removeById(currentData)) {
            return ResponseMessage.success(currentDataName);
        } else {
            return ResponseMessage.serverError(currentDataName);
        }
    }
}

