package edu.cuit.lushan.factory;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import edu.cuit.lushan.entity.Device;
import edu.cuit.lushan.enums.DeviceVOEnum;
import edu.cuit.lushan.vo.DeviceInfoVO;

public class DeviceVOFactory extends AbstractFactory<Device> {
    protected DeviceVOFactory() {

    }

    @Override
    public Device buildEntityByVO(Device device, Object vo) {
        if (vo == null || BeanUtil.hasNullField(vo)) {
            return null;
        } else {
            BeanUtil.copyProperties(vo, device, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        }
        return device;
    }

    @Override
    public Object buildVOByEntity(Device entity, String name) {
        CopyOptions copyOptions = CopyOptions.create().setIgnoreError(true).setIgnoreNullValue(true);
        switch (DeviceVOEnum.valueOf(name)) {
            case DEVICE_INFO:
                DeviceInfoVO deviceInfoVO = new DeviceInfoVO();
                BeanUtil.copyProperties(entity, deviceInfoVO, copyOptions);
                return deviceInfoVO;
        }
        return null;
    }
}
