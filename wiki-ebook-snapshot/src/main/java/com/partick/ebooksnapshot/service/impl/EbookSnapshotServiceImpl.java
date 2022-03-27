package com.partick.ebooksnapshot.service.impl;


import com.partick.common.resp.StatisticResp;
import com.partick.database.entity.EbookSnapshot;
import com.partick.database.mapper.EbookSnapshotMapperCust;
import com.partick.ebooksnapshot.elasticsearch.EbookSnapshotEs;
import com.partick.ebooksnapshot.service.EbookSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookSnapshotServiceImpl implements EbookSnapshotService {
    @Autowired
    EbookSnapshotMapperCust ebookSnapshotMapperCust;

    @Autowired
    private EbookSnapshotEs ebookSnapshotEs;

    @Override
    public int insertEbookSnapshot() {
        return ebookSnapshotMapperCust.insertEookSnapshot();
    }

    @Override
    public int updateEbookSnapshot(){
        return ebookSnapshotMapperCust.updateEookSnapshot();
    }

    @Override
    public int updateEbookSnapshotIncrease() {
        return ebookSnapshotMapperCust.updateEbookSnapshotIncrease();
    }

    @Override
    public int deleteEbookSnapshot(Long id) {
        return ebookSnapshotMapperCust.deleteEbookSnapshotByEbookId(id);
    }

    @Override
    public List<StatisticResp> getStatistic() {
        return ebookSnapshotMapperCust.getStatistic();
    }

    @Override
    public List<StatisticResp> get30Statistic() {
        return ebookSnapshotMapperCust.get30Statistic();
    }

    @Override
    public Boolean putEbookSnapshotES() {
        List<EbookSnapshot> ebookSnapshotList = ebookSnapshotMapperCust.selectForEs();
        return ebookSnapshotEs.putBulkEbookSnapshotEs(ebookSnapshotList);
    }

    @Override
    public Boolean delEbookSnapshotES() {
        List<EbookSnapshot> ebookSnapshotList = ebookSnapshotMapperCust.selectForEs();
        return ebookSnapshotEs.delBulkEbookSnapshotEs(ebookSnapshotList);
    }
}
