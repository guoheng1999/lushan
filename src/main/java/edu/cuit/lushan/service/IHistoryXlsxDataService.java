package edu.cuit.lushan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.cuit.lushan.entity.CurrentData;
import edu.cuit.lushan.entity.HistoryXlsxData;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Guoheng
 * @since 2022-03-25
 */
public interface IHistoryXlsxDataService extends IService<HistoryXlsxData> {
    HistoryXlsxData getByName(String dataName);
    List<HistoryXlsxData> getByType(String type);
}
