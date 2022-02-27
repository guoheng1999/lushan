package edu.cuit.lushan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuit.lushan.entity.Device;
import edu.cuit.lushan.mapper.DeviceMapper;
import edu.cuit.lushan.service.IDeviceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Guoheng
 * @since 2022-02-12
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

}
