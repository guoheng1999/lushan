package edu.cuit.lushan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuit.lushan.entity.SysLog;
import edu.cuit.lushan.mapper.SysLogMapper;
import edu.cuit.lushan.service.ISysLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-29
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

}
