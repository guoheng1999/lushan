package edu.cuit.lushan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuit.lushan.entity.DataFile;
import edu.cuit.lushan.mapper.DataFileMapper;
import edu.cuit.lushan.service.IDataFileService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据文件表 服务实现类
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-29
 */
@Service
public class DataFileServiceImpl extends ServiceImpl<DataFileMapper, DataFile> implements IDataFileService {

    @Override
    public DataFile getOneByDataFileName(String dataFileName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("file_name", dataFileName);
        DataFile dataFile = getBaseMapper().selectOne(wrapper);
        return dataFile;
    }
}
