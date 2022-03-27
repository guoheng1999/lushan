package edu.cuit.lushan.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import edu.cuit.lushan.annotation.WebLog;
import edu.cuit.lushan.entity.HistoryXlsxData;
import edu.cuit.lushan.service.IHistoryXlsxDataService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.vo.HistoryXlsxDataInfoVO;
import edu.cuit.lushan.vo.HistoryXlsxDataVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/historyXlsxData")
public class HistoryXlsxDataController {

    @Autowired
    IHistoryXlsxDataService historyXlsxDataService;

    @ApiOperation(value = "获取所有历史数据", tags = {"历史数据管理"})
    @GetMapping("/")
    @CrossOrigin
    public ResponseMessage getAll() {
        return ResponseMessage.success(historyXlsxDataService.list());
    }


    @ApiOperation(value = "获取单个历史数据", tags = {"历史数据管理"})
    @GetMapping("/{historyDataName}")
    @CrossOrigin
    public ResponseMessage getByHistoryDataName(@PathVariable String historyDataName) {
        if (historyDataName == null || historyDataName.equals("")) {
            return ResponseMessage.nullError(historyDataName);
        }
        HistoryXlsxData currentData = historyXlsxDataService.getByName(historyDataName);
        if (currentData == null) {
            return ResponseMessage.notFound(historyDataName);
        }
        return ResponseMessage.success(currentData);
    }

    @ApiOperation(value = "通过类别历史数据", tags = {"历史数据管理"})
    @GetMapping("/type/{type}")
    @CrossOrigin
    public ResponseMessage getByType(@PathVariable String type) {
        if (type == null || type.equals("")) {
            return ResponseMessage.nullError(type);
        }
        List<HistoryXlsxData> list = historyXlsxDataService.getByType(type);
        List<HistoryXlsxDataInfoVO> result = new LinkedList<>();
        list.forEach(e -> {
            result.add(
                    HistoryXlsxDataInfoVO.builder()
                            .dataName(e.getDataName())
                            .logTime(e.getLogTime())
                            .pages(e.getPages())
                            .station(e.getStation())
                            .type(e.getType())
                            .build());
        });
        return ResponseMessage.success(result);
    }

    @ApiOperation(value = "添加历史数据", tags = {"历史数据管理"})
    @PostMapping("/")
    
    public ResponseMessage add(@RequestBody HistoryXlsxDataVO historyXlsxDataVO) {
        if (BeanUtil.hasNullField(historyXlsxDataVO)) {
            return ResponseMessage.nullError(historyXlsxDataVO);
        }
        HistoryXlsxData currentData = new HistoryXlsxData();
        BeanUtil.copyProperties(historyXlsxDataVO, currentData, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (historyXlsxDataService.save(currentData)) {
            return ResponseMessage.success(historyXlsxDataVO);
        } else {
            return ResponseMessage.serverError(historyXlsxDataVO);
        }
    }

    @ApiOperation(value = "修改历史数据", tags = {"历史数据管理"})
    @PutMapping("/{historyXlsxDataId}")
    
    public ResponseMessage update(@PathVariable String historyXlsxDataId, @RequestBody HistoryXlsxDataVO historyXlsxDataVO) {
        if (BeanUtil.hasNullField(historyXlsxDataVO) || StrUtil.isEmpty(historyXlsxDataId)) {
            return ResponseMessage.nullError(historyXlsxDataVO);
        }

        HistoryXlsxData currentData = historyXlsxDataService.getById(historyXlsxDataId);
        if (currentData == null) {
            return ResponseMessage.notFound(historyXlsxDataId);
        }

        BeanUtil.copyProperties(historyXlsxDataVO, currentData, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (historyXlsxDataService.updateById(currentData)) {
            return ResponseMessage.success(historyXlsxDataVO);
        } else {
            return ResponseMessage.serverError(historyXlsxDataVO);
        }
    }

    @ApiOperation(value = "删除历史数据", tags = {"历史数据管理"})
    @DeleteMapping("/{historyXlsxDataId}")
    
    public ResponseMessage delete(@PathVariable String historyXlsxDataId) {
        if (StrUtil.isEmpty(historyXlsxDataId)) {
            return ResponseMessage.nullError(historyXlsxDataId);
        }

        HistoryXlsxData currentData = historyXlsxDataService.getById(historyXlsxDataId);
        if (currentData == null) {
            return ResponseMessage.notFound(historyXlsxDataId);
        }
        if (historyXlsxDataService.removeById(currentData)) {
            return ResponseMessage.success(historyXlsxDataId);
        } else {
            return ResponseMessage.serverError(historyXlsxDataId);
        }
    }
}

