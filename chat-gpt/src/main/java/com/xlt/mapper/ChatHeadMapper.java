package com.xlt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlt.model.po.ChatHeadPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatHeadMapper extends BaseMapper<ChatHeadPo> {

    int addChatHead(ChatHeadPo chatHeadPo);
}
