package com.xlt.model.response;

import com.xlt.model.request.WenxinFunctionCall;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WenxinChatRes implements Serializable {
    private static final long serialVersionUID = -8566644206145971330L;

    /**
     * 本轮对话的id
     */
    private String id;
    /**
     * 回包类型
     */
    private String object;
    /**
     * 时间戳
     */
    private Long created;
    /**
     * 表示当前子句的序号。只有在流式接口模式下会返回该字段
     */
    private Long sentence_id;
    /**
     * 表示当前子句是否是最后一句。只有在流式接口模式下会返回该字段
     */
    private Boolean is_end;
    /**
     * 当前生成的结果是否被截断
     */
    private Boolean is_truncated;
    /**
     * 对话返回结果
     */
    private String result;
    /**
     * 表示用户输入是否存在安全，是否关闭当前会话，清理历史会话信息
     *    true：是，表示用户输入存在安全风险，建议关闭当前会话，清理历史会话信息
     *    false：否，表示用户输入无安全风险
     */
    private Boolean need_clear_history;
    /**
     * 当need_clear_history为true时，此字段会告知第几轮对话有敏感信息，如果是当前问题，ban_round=-1
     */
    private Integer ban_round;
    /**
     * token统计信息，token数 = 汉字数+单词数*1.3 （仅为估算逻辑）
     */
    private WenxinUsage usage;
    /**
     * 由模型生成的函数调用，包含函数名称，和调用参数
     */
    private WenxinFunctionCall function_call;

}
