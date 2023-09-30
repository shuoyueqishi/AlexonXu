package com.xlt.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WenxinChatReq implements Serializable {
    private static final long serialVersionUID = 7434348498264943757L;
    /**
     * 聊天上下文信息。说明：
     * （1）messages成员不能为空，1个成员表示单轮对话，多个成员表示多轮对话
     * （2）最后一个message为当前请求的信息，前面的message为历史对话信息
     * （3）必须为奇数个成员，成员中message的role必须依次为user(or function)、assistant
     * （4）最后一个message的content长度（即此轮对话的问题）不能超过3000 token；如果messages中content总长度大于3000 token，系统会依次遗忘最早的历史会话，直到content的总长度不超过3000 token
     */
    private List<WenxinMessage> messages;

    /**
     * 一个可触发函数的描述列表
     */
    private List<WenxinFunction> functions;

    /**
     * （1）较高的数值会使输出更加随机，而较低的数值会使其更加集中和确定
     * （2）默认0.95，范围 (0, 1.0]，不能为0
     * （3）建议该参数和top_p只设置1个
     * （4）建议top_p和temperature不要同时更改
     */
    private Float temperature;

    /**
     * （1）影响输出文本的多样性，取值越大，生成文本的多样性越强
     * （2）默认0.8，取值范围 [0, 1.0]
     * （3）建议该参数和temperature只设置1个
     * （4）建议top_p和temperature不要同时更改
     */
    private Float top_p;

    /**
     * 通过对已生成的token增加惩罚，减少重复生成的现象。说明：
     *（1）值越大表示惩罚越大
     *（2）默认1.0，取值范围：[1.0, 2.0]
     */
    private Float penalty_score;

    /**
     * 是否以流式接口的形式返回数据，默认false
     */
    private Boolean stream;

    /**
     * 表示最终用户的唯一标识符，可以监视和检测滥用行为，防止接口恶意调用
     */
    private String user_id;

}
