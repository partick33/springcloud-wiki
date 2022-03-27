package com.partick.database.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DocMapperCust {

    void increaseViewCount(@Param("id") Long id);

    void increaseVoteCount(@Param("id") Long id);

    void updateEbookInfo();
}
