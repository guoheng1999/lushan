package edu.cuit.lushan.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import edu.cuit.lushan.annotation.DataLog;
import edu.cuit.lushan.annotation.WebLog;
import edu.cuit.lushan.entity.Device;
import edu.cuit.lushan.service.IDeviceService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.utils.UserAgentUtil;
import edu.cuit.lushan.vo.DeviceInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Guoheng
 * @since 2022-02-12
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    IDeviceService deviceService;
    @Autowired
    UserAgentUtil userAgentUtil;

    @ApiOperation(value = "获取所有设备的信息", tags = {"设备管理"})
    @GetMapping("/")
    @DataLog
    @CrossOrigin
    public ResponseMessage getAll() {
        List result = new LinkedList();
        deviceService.list().forEach((item) -> {
            result.add(
                    DeviceInfoVO.builder()
                            .deviceName(item.getDeviceName())
                            .description(item.getDescription())
                            .id(item.getId())
                            .build()
            );
        });
        return ResponseMessage.success(result);
    }

    @ApiOperation(value = "获取一个设备信息", tags = {"设备管理"})
    @GetMapping("/{deviceId}")
    @DataLog
    public ResponseMessage getOne(@PathVariable String deviceId) {
        Device device = deviceService.getById(deviceId);
        if (device == null) {
            return ResponseMessage.successCodeMsgData(2404, "没有查询到该设备的信息。", deviceId);
        }
        DeviceInfoVO deviceVO = DeviceInfoVO.builder()
                .description(device.getDescription())
                .deviceName(device.getDeviceName())
                .id(device.getId())
                .build();
        return ResponseMessage.success(deviceVO);
    }

    @ApiOperation(value = "添加设备信息", tags = {"设备管理"})
    @PostMapping("/")
    @DataLog
//    @WebLog
    public ResponseMessage register(@RequestBody DeviceInfoVO deviceVO, HttpServletRequest request) {
        Device device = Device.builder().deviceName(deviceVO.getDeviceName())
                .description(deviceVO.getDescription())
                .modifyUserId(userAgentUtil.getUserId(request))
                .build();
        if (deviceService.save(device)) {
            return ResponseMessage.success(deviceVO);
        } else {
            return ResponseMessage.serverError(deviceVO);
        }
    }

    @ApiOperation(value = "更改设备信息", tags = {"设备管理"})
    @PutMapping("/")
    @DataLog
//    @WebLog
    public ResponseMessage update(@RequestBody DeviceInfoVO deviceVO) {
        Device device = deviceService.getById(deviceVO.getId());
        if (device == null) {
            return ResponseMessage.errorMsg(2404, "没有查询到该设备的信息。", deviceVO);
        }
        BeanUtil.copyProperties(deviceVO, device,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (deviceService.updateById(device)) {
            return ResponseMessage.success(deviceVO);
        } else {
            return ResponseMessage.serverError(deviceVO);
        }
    }

    @ApiOperation(value = "删除设备信息", tags = {"设备管理"})
    @DeleteMapping("/{deviceId}")
    @DataLog
//    @WebLog
    public ResponseMessage delete(@PathVariable String deviceId) {
        if (deviceService.getById(deviceId) == null) {
            return ResponseMessage.errorMsg(2500, "没有查询到该设备的信息。", deviceId);
        }
        if (deviceService.removeById(deviceId)) {
            return ResponseMessage.success(deviceId);
        } else {
            return ResponseMessage.serverError(deviceId);
        }
    }
}

