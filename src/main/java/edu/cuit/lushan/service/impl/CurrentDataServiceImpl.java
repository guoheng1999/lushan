package edu.cuit.lushan.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuit.lushan.entity.CurrentData;
import edu.cuit.lushan.entity.HistoryPictureData;
import edu.cuit.lushan.mapper.CurrentDataMapper;
import edu.cuit.lushan.service.ICurrentDataService;
import edu.cuit.lushan.vo.CurrentDataDownloadRequestVO;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Guoheng
 * @since 2022-03-25
 */
@Service
public class CurrentDataServiceImpl extends ServiceImpl<CurrentDataMapper, CurrentData> implements ICurrentDataService {
    @Override
    public CurrentData getByName(String dataName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("data_name", dataName);
        List<CurrentData> list = this.getBaseMapper().selectList(wrapper);
        if (list.isEmpty()){
            return null;
        }else {
            return list.get(0);
        }
    }

    @Override
    public List<CurrentData> getByDeviceId(Integer deviceId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("device_id", deviceId);
        List<CurrentData> list = this.getBaseMapper().selectList(wrapper);
        return list;
    }

    @Override
    public List<CurrentData> getByDeviceIdAndDataLevel(Integer deviceId, Integer dataLevel) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("device_id", deviceId);
        wrapper.eq("data_level", dataLevel);
        List<CurrentData> list = this.getBaseMapper().selectList(wrapper);
        return list;
    }

    @Override
    public List<CurrentData> getByDeviceIdAndDataLevelWithFromDayEndDay(Integer deviceId, Integer dataLevel, String fromDay, String endDay) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("device_id", deviceId);
        wrapper.eq("data_level", dataLevel);
        wrapper.between("log_time", fromDay, endDay);
        List<CurrentData> list = this.getBaseMapper().selectList(wrapper);
        return list;
    }

    @Override
    public List<CurrentData> getByYear(Integer deviceId, Integer dataLevel, Integer year) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("year", year);
        wrapper.eq("device_id", deviceId);
        wrapper.eq("data_level", dataLevel);
        List<CurrentData> list = this.getBaseMapper().selectList(wrapper);
        return list;
    }

    @Override
    public List<CurrentData> getByDownloadVO(CurrentDataDownloadRequestVO currentDataDownloadRequestVO) {
        // 按照VO对象查询
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("device_id", currentDataDownloadRequestVO.getDeviceId());
        wrapper.eq("data_level", currentDataDownloadRequestVO.getDataLevel());
        wrapper.eq("data_type", currentDataDownloadRequestVO.getDataType());
        wrapper.between("log_time", currentDataDownloadRequestVO.getFromDay(), currentDataDownloadRequestVO.getEndDay());
        if (currentDataDownloadRequestVO.getDataLevel() == 1){
            // 数据库存储文件格式为：.csv .txt .xlsx ...
            wrapper.eq("file_type", "." +currentDataDownloadRequestVO.getFileFormat());
        }
        List<CurrentData> list = this.getBaseMapper().selectList(wrapper);
        return list;
    }
}
