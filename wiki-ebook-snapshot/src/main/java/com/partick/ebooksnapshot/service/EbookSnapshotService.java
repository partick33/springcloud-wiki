package com.partick.ebooksnapshot.service;


import com.partick.common.resp.StatisticResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EbookSnapshotService {


    int insertEbookSnapshot();

    int updateEbookSnapshot();

    int updateEbookSnapshotIncrease();

    int deleteEbookSnapshot(@Param("id") Long id);

    List<StatisticResp> getStatistic();

    List<StatisticResp> get30Statistic();

    Boolean putEbookSnapshotES();

    Boolean delEbookSnapshotES();
}
