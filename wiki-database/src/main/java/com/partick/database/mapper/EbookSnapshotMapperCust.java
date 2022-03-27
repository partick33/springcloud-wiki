package com.partick.database.mapper;


import com.partick.common.resp.StatisticResp;
import com.partick.database.entity.EbookSnapshot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EbookSnapshotMapperCust {

    int insertEookSnapshot();

    int updateEookSnapshot();

    int updateEbookSnapshotIncrease();

    int deleteEbookSnapshotByEbookId(Long id);

    List<StatisticResp> getStatistic();

    List<StatisticResp> get30Statistic();

    List<EbookSnapshot> selectForEs();
}
