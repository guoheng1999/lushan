package edu.cuit.lushan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuit.lushan.entity.HistoryPictureData;
import edu.cuit.lushan.entity.HistoryXlsxData;
import edu.cuit.lushan.mapper.HistoryPictureDataMapper;
import edu.cuit.lushan.service.IHistoryPictureDataService;
import org.springframework.stereotype.Service;

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
public class HistoryPictureDataServiceImpl extends ServiceImpl<HistoryPictureDataMapper, HistoryPictureData> implements IHistoryPictureDataService {

    @Override
    public List<HistoryPictureData> getHistoryDataPictureByHistoryName(String historyDataName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("history_xlsx_data_name", historyDataName);
        List<HistoryPictureData> list = this.getBaseMapper().selectList(wrapper);
        return list;
    }

    @Override
    public HistoryPictureData getByHistoryNameAndPicName(String historyDataName, String picName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("history_xlsx_data_name", historyDataName);
        wrapper.eq("pic_name", picName);
        List<HistoryPictureData> list = this.getBaseMapper().selectList(wrapper);
        if (list.isEmpty()) {
            return null;
        }else {
            return list.get(0);
        }
    }
}
