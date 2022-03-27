package edu.cuit.lushan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.cuit.lushan.entity.DataFile;

/**
 * <p>
 * 数据文件表 服务类
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-29
 */
public interface IDataFileService extends IService<DataFile> {
    DataFile getOneByDataFileName(String dataFileName);
}
