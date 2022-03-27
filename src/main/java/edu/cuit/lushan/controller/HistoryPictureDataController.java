package edu.cuit.lushan.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import edu.cuit.lushan.entity.HistoryPictureData;
import edu.cuit.lushan.service.IHistoryPictureDataService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.vo.HistoryPictureDataVO;
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
 * @since 2022-03-25
 */
@RestController
@RequestMapping("/historyPictureData")
public class HistoryPictureDataController {

    @Autowired
    IHistoryPictureDataService historyPictureDataService;

    @ApiOperation(value = "获取所有历史数据原始记录", tags = {"历史数据原始记录管理"})
    @GetMapping("/")
    @CrossOrigin
    public ResponseMessage getAll() {
        return ResponseMessage.success(historyPictureDataService.list());
    }


    @ApiOperation(value = "获取单个历史数据原始记录", tags = {"历史数据原始记录管理"})
    @GetMapping("/{historyDataName}")
    @CrossOrigin
    public ResponseMessage getByUserId(@PathVariable String dataName) {
        if (dataName == null || dataName.equals("")) {
            return ResponseMessage.nullError(dataName);
        }
        HistoryPictureData currentData = historyPictureDataService.getById(dataName);
        if (currentData == null) {
            return ResponseMessage.notFound(dataName);
        }
        return ResponseMessage.success(currentData);
    }

    @ApiOperation(value = "通过历史数据集名称获取原始记录", tags = {"历史数据原始记录管理"})
    @GetMapping("/historyDataName/{historyDataName}")
    @CrossOrigin
    public ResponseMessage getByHistoryData(@PathVariable String historyDataName) {

        if (historyDataName == null || historyDataName.equals("")) {
            return ResponseMessage.nullError(historyDataName);
        }
        List<HistoryPictureData> list = historyPictureDataService.getHistoryDataPictureByHistoryName(historyDataName);
        if (list.isEmpty()) {
            return ResponseMessage.notFound(historyDataName);
        }
        List result = new LinkedList();
        list.forEach(e->{
            result.add(HistoryPictureDataVO.builder()
                    .historyXlsxDataName(e.getHistoryXlsxDataName())
                    .picName(e.getPicName()).build());
        });
        return ResponseMessage.success(result);
    }

    @ApiOperation(value = "添加历史数据原始记录", tags = {"历史数据原始记录管理"})
    @PostMapping("/")
    @CrossOrigin
    
    public ResponseMessage add(@RequestBody HistoryPictureDataVO historyPictureDataVO) {
        if (BeanUtil.hasNullField(historyPictureDataVO)) {
            return ResponseMessage.nullError(historyPictureDataVO);
        }
        HistoryPictureData currentData = new HistoryPictureData();
        BeanUtil.copyProperties(historyPictureDataVO, currentData, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (historyPictureDataService.save(currentData)) {
            return ResponseMessage.success(historyPictureDataVO);
        } else {
            return ResponseMessage.serverError(historyPictureDataVO);
        }
    }

    @ApiOperation(value = "修改历史数据原始记录", tags = {"历史数据原始记录管理"})
    @PutMapping("/{historyPictureDataId}")
    @CrossOrigin
    
    public ResponseMessage update(@PathVariable String historyPictureDataId, @RequestBody HistoryPictureDataVO historyPictureDataVO) {
        if (BeanUtil.hasNullField(historyPictureDataVO) || StrUtil.isEmpty(historyPictureDataId)) {
            return ResponseMessage.nullError(historyPictureDataVO);
        }

        HistoryPictureData currentData = historyPictureDataService.getById(historyPictureDataId);
        if (currentData == null) {
            return ResponseMessage.notFound(historyPictureDataId);
        }

        BeanUtil.copyProperties(historyPictureDataVO, currentData, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (historyPictureDataService.updateById(currentData)) {
            return ResponseMessage.success(historyPictureDataVO);
        } else {
            return ResponseMessage.serverError(historyPictureDataVO);
        }
    }

    @ApiOperation(value = "删除历史数据原始记录", tags = {"历史数据原始记录管理"})
    @DeleteMapping("/{historyPictureDataId}")
    @CrossOrigin
    
    public ResponseMessage delete(@PathVariable String historyPictureDataId) {
        if (StrUtil.isEmpty(historyPictureDataId)) {
            return ResponseMessage.nullError(historyPictureDataId);
        }

        HistoryPictureData currentData = historyPictureDataService.getById(historyPictureDataId);
        if (currentData == null) {
            return ResponseMessage.notFound(historyPictureDataId);
        }
        if (historyPictureDataService.removeById(currentData)) {
            return ResponseMessage.success(historyPictureDataId);
        } else {
            return ResponseMessage.serverError(historyPictureDataId);
        }
    }
}

