package com.xlt.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatGptConfigVo implements Serializable {
    private static final long serialVersionUID = -1409376255350051677L;

    private String token;

    private boolean useProxy;

    private String proxyDomain;

    private String openAiDomain;
}
