package com.xlt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlt.model.po.ChatLinePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatLineMapper extends BaseMapper<ChatLinePo> {

    int addBatchChatLines(@Param("list") List<ChatLinePo> chatLinePoList);
}
