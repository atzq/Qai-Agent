package net.itzq.mira.modules.ai.client.openai.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.itzq.mira.modules.ai.client.openai.usage.Usage;

import java.util.List;

/**
 * ChatCompletionResponse
 */

@Data
@NoArgsConstructor()
@AllArgsConstructor()
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletionResponse {
    /**
     * 该对话的唯一标识符。
     */
    private String id;

    /**
     * 对象的类型, 其值为 handle.completion 或 handle.completion.chunk
     */
    private String object;

    /**
     * 创建聊天完成时的 Unix 时间戳（以秒为单位）。
     */
    private Long created;

    /**
     * 生成该 completion 的模型名。
     */
    private String model;

    /**
     * 该指纹代表模型运行时使用的后端配置。
     */
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    /**
     * 模型生成的 completion 的选择列表。
     */
    private List<Choice> choices;

    /**
     * 该对话补全请求的用量信息。
     */
    private Usage usage;
}
