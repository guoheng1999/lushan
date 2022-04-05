package edu.cuit.lushan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.cuit.lushan.entity.CurrentData;
import edu.cuit.lushan.vo.CurrentDataDownloadRequestVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Guoheng
 * @since 2022-03-25
 */
public interface ICurrentDataService extends IService<CurrentData> {
    CurrentData getByName(String dataName);
    List<CurrentData> getByDeviceId(Integer deviceId);
    List<CurrentData> getByDeviceIdAndDataLevel(Integer deviceId, Integer dataLevel);
    List<CurrentData> getByDeviceIdAndDataLevelWithFromDayEndDay(Integer deviceId, Integer dataLevel, String fromDay, String endDay);
    List<CurrentData> getByYear(Integer deviceId, Integer dataLevel, Integer year);
    List<CurrentData> getByDownloadVO(CurrentDataDownloadRequestVO currentDataDownloadRequestVO);
}
