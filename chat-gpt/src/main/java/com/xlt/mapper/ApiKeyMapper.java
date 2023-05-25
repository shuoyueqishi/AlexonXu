package com.xlt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlt.model.po.OpenAiApiKeyPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiKeyMapper extends BaseMapper<OpenAiApiKeyPo> {
}
