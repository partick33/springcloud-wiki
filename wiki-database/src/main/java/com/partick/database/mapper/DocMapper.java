package com.partick.database.mapper;


import com.partick.database.entity.Doc;
import com.partick.database.entity.DocExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DocMapper {
    long countByExample(DocExample example);

    int deleteByExample(DocExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Doc record);

    int insertSelective(Doc record);

    List<Doc> selectByExample(DocExample example);

    Doc selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Doc record, @Param("example") DocExample example);

    int updateByExample(@Param("record") Doc record, @Param("example") DocExample example);

    int updateByPrimaryKeySelective(Doc record);

    int updateByPrimaryKey(Doc record);
}