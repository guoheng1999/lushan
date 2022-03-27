package edu.cuit.lushan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuit.lushan.entity.HistoryXlsxData;
import edu.cuit.lushan.mapper.HistoryXlsxDataMapper;
import edu.cuit.lushan.service.IHistoryXlsxDataService;
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
public class HistoryXlsxDataServiceImpl extends ServiceImpl<HistoryXlsxDataMapper, HistoryXlsxData> implements IHistoryXlsxDataService {

    @Override
    public HistoryXlsxData getByName(String dataName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("data_name", dataName);
        List<HistoryXlsxData> list = this.getBaseMapper().selectList(wrapper);
        if (list.isEmpty()){
            return null;
        }else {
            return list.get(0);
        }
    }

    @Override
    public List<HistoryXlsxData> getByType(String type) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type", type);
        List<HistoryXlsxData> list = this.getBaseMapper().selectList(wrapper);
        return list;
    }
}
