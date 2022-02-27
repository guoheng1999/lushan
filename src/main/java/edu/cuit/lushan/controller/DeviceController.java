package edu.cuit.lushan.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import edu.cuit.lushan.entity.Device;
import edu.cuit.lushan.service.IDeviceService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.vo.DeviceVO;
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
 * @since 2022-02-12
 */
@RestController
@RequestMapping("/device")
@RequiresRoles({"USER"})
public class DeviceController {

    @Autowired
    IDeviceService deviceService;

    @ApiOperation(value = "获取所有设备信息", tags = {"设备管理"})
    @GetMapping("/")
    @RequiresRoles({"MANAGER"})
    public ResponseMessage getAll() {
        return ResponseMessage.success(deviceService.list());
    }

    @ApiOperation(value = "获取一个设备信息", tags = {"设备管理"})
    @GetMapping("/{deviceId}")
    public ResponseMessage getOne(@PathVariable String deviceId) {
        Device device = deviceService.getById(deviceId);
        if (device == null){
            return ResponseMessage.successCodeMsgData(2404, "Device not found!", deviceId);
        }
        DeviceVO deviceVO = DeviceVO.builder()
                .description(device.getDescription())
                .deviceName(device.getDeviceName())
                .id(device.getId())
                .build();
        return ResponseMessage.success(deviceVO);
    }

    @ApiOperation(value = "添加设备信息", tags = {"设备管理"})
    @PostMapping("/")
    public ResponseMessage register(@RequestBody DeviceVO deviceVO) {
        Device device = Device.builder().deviceName(deviceVO.getDeviceName())
                .description(deviceVO.getDescription())
                .build();
        if (deviceService.save(device)) {
            return ResponseMessage.success(deviceVO);
        } else {
            return ResponseMessage.errorMsg(2500, "Server error!", deviceVO);
        }
    }

    @ApiOperation(value = "更改设备信息", tags = {"设备管理"})
    @PutMapping("/")
    public ResponseMessage update(@RequestBody DeviceVO deviceVO) {
        //   暂未实现
        Device device = deviceService.getById(deviceVO.getId());
        deviceVO.setId(null);
        if (device == null) {
            return ResponseMessage.errorMsg(2404, "Device not found!", deviceVO);
        }
        BeanUtil.copyProperties(deviceVO,device,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (deviceService.saveOrUpdate(device)) {
            return ResponseMessage.success(deviceVO);
        } else {
            return ResponseMessage.errorMsg(2500, "Server error!", deviceVO);
        }
    }

    @ApiOperation(value = "删除设备信息", tags = {"设备管理"})
    @DeleteMapping("/{deviceId}")
    public ResponseMessage delete(@PathVariable String deviceId) {
        deviceService.removeById(deviceId);

        return ResponseMessage.success(deviceId);
    }
}

