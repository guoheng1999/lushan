package edu.cuit.lushan.factory;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import edu.cuit.lushan.entity.CurrentData;
import edu.cuit.lushan.entity.Device;
import edu.cuit.lushan.enums.CurrentDataVOEnum;
import edu.cuit.lushan.enums.DeviceVOEnum;
import edu.cuit.lushan.vo.CurrentDataVO;
import edu.cuit.lushan.vo.DeviceInfoVO;

public class CurrentDataVOFactorty extends AbstractFactory<CurrentData> {
    @Override
    public CurrentData buildEntityByVO(CurrentData entity, Object vo) {

        if (vo == null || BeanUtil.hasNullField(vo)) {
            return null;
        } else {
            BeanUtil.copyProperties(vo, entity, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        }
        return entity;
    }

    @Override
    public Object buildVOByEntity(CurrentData entity, String name) {
        CopyOptions copyOptions = CopyOptions.create().setIgnoreError(true).setIgnoreNullValue(true);
        switch (CurrentDataVOEnum.valueOf(name)) {
            case CURRENT_DATA_VO:
                CurrentDataVO currentDataVO = new CurrentDataVO();
                BeanUtil.copyProperties(entity, currentDataVO, copyOptions);
                return currentDataVO;
        }
        return null;
    }
}
