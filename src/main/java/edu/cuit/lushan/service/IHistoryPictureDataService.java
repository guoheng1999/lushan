package edu.cuit.lushan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.cuit.lushan.entity.HistoryPictureData;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Guoheng
 * @since 2022-03-25
 */
public interface IHistoryPictureDataService extends IService<HistoryPictureData> {
    List<HistoryPictureData> getHistoryDataPictureByHistoryName(String historyDataName);
    HistoryPictureData getByHistoryNameAndPicName(String historyDataName, String picName);
}
