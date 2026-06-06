package net.itzq.mira.modules.ai.client.handle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.itzq.mira.modules.ai.client.openai.chat.entity.ChatMessage;
import net.itzq.mira.modules.ai.client.openai.chat.entity.StreamOptions;
import net.itzq.mira.modules.ai.client.openai.tool.Tool;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  ApiSetting
 *
 *  @author tangzq
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRequestParams {

    /**
     * API参数Map，用于存储所有请求参数
     * key: 参数名
     * value: 参数值
     */
    @JsonIgnore
    private Map<String, Object> apiParams = new ConcurrentHashMap<>();

    /**
     * 获取模型名称
     */
    @JsonIgnore
    public String getModel() {
        return apiParams != null ? (String) apiParams.get("model") : null;
    }

    /**
     * 设置模型名称
     */
    @JsonIgnore
    public void setModel(String model) {
        apiParams.put("model", model);
    }

    /**
     * 获取消息列表
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public List<ChatMessage> getMessages() {
        return apiParams != null ? (List<ChatMessage>) apiParams.get("messages") : null;
    }

    /**
     * 设置消息列表
     */
    @JsonIgnore
    public void setMessages(List<ChatMessage> messages) {

        apiParams.put("messages", messages);
    }

    /**
     * 获取stream参数
     */
    @JsonIgnore
    public Boolean getStream() {
        return apiParams != null ? (Boolean) apiParams.getOrDefault("stream", false) : false;
    }

    /**
     * 设置stream参数
     */
    @JsonIgnore
    public void setStream(Boolean stream) {

        apiParams.put("stream", stream);
    }

    /**
     * 获取streamOptions
     */
    @JsonIgnore
    public StreamOptions getStreamOptions() {
        return apiParams != null ? (StreamOptions) apiParams.get("stream_options") : null;
    }

    /**
     * 设置streamOptions
     */
    @JsonIgnore
    public void setStreamOptions(StreamOptions streamOptions) {

        apiParams.put("stream_options", streamOptions);
    }

    /**
     * 获取frequencyPenalty
     */
    @JsonIgnore
    public Float getFrequencyPenalty() {
        return apiParams != null ? (Float) apiParams.getOrDefault("frequency_penalty", 0f) : 0f;
    }

    /**
     * 设置frequencyPenalty
     */
    @JsonIgnore
    public void setFrequencyPenalty(Float frequencyPenalty) {

        apiParams.put("frequency_penalty", frequencyPenalty);
    }

    /**
     * 获取temperature
     */
    @JsonIgnore
    public Float getTemperature() {
        return apiParams != null ? (Float) apiParams.getOrDefault("temperature", 1f) : 1f;
    }

    /**
     * 设置temperature
     */
    @JsonIgnore
    public void setTemperature(Float temperature) {

        apiParams.put("temperature", temperature);
    }

    /**
     * 获取topP
     */
    @JsonIgnore
    @JsonProperty("top_p")
    public Float getTopP() {
        return apiParams != null ? (Float) apiParams.getOrDefault("top_p", 1f) : 1f;
    }

    /**
     * 设置topP
     */
    @JsonIgnore
    @JsonProperty("top_p")
    public void setTopP(Float topP) {

        apiParams.put("top_p", topP);
    }

    /**
     * 获取maxTokens
     */
    @JsonIgnore
    @JsonProperty("max_tokens")
    public Integer getMaxTokens() {
        return apiParams != null ? (Integer) apiParams.get("max_tokens") : null;
    }

    /**
     * 设置maxTokens
     */
    @JsonIgnore
    @JsonProperty("max_tokens")
    public void setMaxTokens(Integer maxTokens) {

        apiParams.put("max_tokens", maxTokens);
    }

    /**
     * 获取tools
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public List<Tool> getTools() {
        return apiParams != null ? (List<Tool>) apiParams.get("tools") : null;
    }

    /**
     * 设置tools
     */
    @JsonIgnore
    public void setTools(List<Tool> tools) {
        if (tools == null) {
            return;
        }

        apiParams.put("tools", tools);
    }

    /**
     * 获取functions（辅助属性）
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public List<String> getFunctions() {
        return apiParams != null ? (List<String>) apiParams.get("functions") : null;
    }

    /**
     * 设置functions
     */
    @JsonIgnore
    public void setFunctions(List<String> functions) {

        apiParams.put("functions", functions);
    }

    /**
     * 添加function
     */
    @JsonIgnore
    public void addFunction(String function) {

        List<String> functions = getFunctions();
        if (functions == null) {
            functions = new ArrayList<>();
        }
        functions.add(function);
        apiParams.put("functions", functions);
    }

    /**
     * 添加多个functions
     */
    @JsonIgnore
    public void addFunctions(String... functions) {
        if (functions == null || functions.length == 0) {
            return;
        }

        List<String> existingFunctions = getFunctions();
        if (existingFunctions == null) {
            existingFunctions = new ArrayList<>();
        }
        existingFunctions.addAll(Arrays.asList(functions));
        setFunctions(existingFunctions);
    }

    /**
     * 获取toolChoice
     */
    @JsonIgnore
    @JsonProperty("tool_choice")
    public String getToolChoice() {
        return apiParams != null ? (String) apiParams.get("tool_choice") : null;
    }

    /**
     * 设置toolChoice
     */
    @JsonIgnore
    @JsonProperty("tool_choice")
    public void setToolChoice(String toolChoice) {

        apiParams.put("tool_choice", toolChoice);
    }

    /**
     * 获取parallelToolCalls
     */
    @JsonIgnore
    @JsonProperty("parallel_tool_calls")
    public Boolean getParallelToolCalls() {
        return apiParams != null ? (Boolean) apiParams.getOrDefault("parallel_tool_calls", true) : true;
    }

    /**
     * 设置parallelToolCalls
     */
    @JsonIgnore
    @JsonProperty("parallel_tool_calls")
    public void setParallelToolCalls(Boolean parallelToolCalls) {

        apiParams.put("parallel_tool_calls", parallelToolCalls);
    }

    /**
     * 获取responseFormat
     */
    @JsonIgnore
    @JsonProperty("response_format")
    public Object getResponseFormat() {
        return apiParams != null ? apiParams.get("response_format") : null;
    }

    /**
     * 设置responseFormat
     */
    @JsonIgnore
    @JsonProperty("response_format")
    public void setResponseFormat(Object responseFormat) {

        apiParams.put("response_format", responseFormat);
    }

    /**
     * 获取user
     */
    @JsonIgnore
    public String getUser() {
        return apiParams != null ? (String) apiParams.get("user") : null;
    }

    /**
     * 设置user
     */
    @JsonIgnore
    public void setUser(String user) {

        apiParams.put("user", user);
    }

    /**
     * 获取n
     */
    @JsonIgnore
    public Integer getN() {
        return apiParams != null ? (Integer) apiParams.getOrDefault("n", 1) : 1;
    }

    /**
     * 设置n
     */
    @JsonIgnore
    public void setN(Integer n) {

        apiParams.put("n", n);
    }

    /**
     * 获取stop
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public List<String> getStop() {
        return apiParams != null ? (List<String>) apiParams.get("stop") : null;
    }

    /**
     * 设置stop
     */
    @JsonIgnore
    public void setStop(List<String> stop) {

        apiParams.put("stop", stop);
    }

    /**
     * 获取presencePenalty
     */
    @JsonIgnore
    @JsonProperty("presence_penalty")
    public Float getPresencePenalty() {
        return apiParams != null ? (Float) apiParams.getOrDefault("presence_penalty", 0f) : 0f;
    }

    /**
     * 设置presencePenalty
     */
    @JsonIgnore
    @JsonProperty("presence_penalty")
    public void setPresencePenalty(Float presencePenalty) {

        apiParams.put("presence_penalty", presencePenalty);
    }

    /**
     * 获取logitBias
     */
    @JsonIgnore
    @JsonProperty("logit_bias")
    public Map getLogitBias() {
        return apiParams != null ? (Map) apiParams.get("logit_bias") : null;
    }

    /**
     * 设置logitBias
     */
    @JsonIgnore
    @JsonProperty("logit_bias")
    public void setLogitBias(Map logitBias) {

        apiParams.put("logit_bias", logitBias);
    }

    /**
     * 获取logprobs
     */
    @JsonIgnore
    public Boolean getLogprobs() {
        return apiParams != null ? (Boolean) apiParams.getOrDefault("logprobs", false) : false;
    }

    /**
     * 设置logprobs
     */
    @JsonIgnore
    public void setLogprobs(Boolean logprobs) {

        apiParams.put("logprobs", logprobs);
    }

    /**
     * 获取topLogprobs
     */
    @JsonIgnore
    @JsonProperty("top_logprobs")
    public Integer getTopLogprobs() {
        return apiParams != null ? (Integer) apiParams.get("top_logprobs") : null;
    }

    /**
     * 设置topLogprobs
     */
    @JsonIgnore
    @JsonProperty("top_logprobs")
    public void setTopLogprobs(Integer topLogprobs) {

        apiParams.put("top_logprobs", topLogprobs);
    }

    /**
     * 添加自定义参数（用于适配不同厂商的特定参数）
     *
     * @param key 参数名
     * @param value 参数值
     * @return 当前对象
     */
    @JsonIgnore
    public ApiRequestParams addCustomParam(String key, Object value) {

        apiParams.put(key, value);
        return this;
    }

    /**
     * 批量添加自定义参数
     *
     * @param params 参数Map
     * @return 当前对象
     */
    @JsonIgnore
    public ApiRequestParams addCustomParams(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return this;
        }

        apiParams.putAll(params);
        return this;
    }

    /**
     * 获取指定参数
     *
     * @param key 参数名
     * @return 参数值
     */
    @JsonIgnore
    public Object getParam(String key) {
        return apiParams != null ? apiParams.get(key) : null;
    }

    /**
     * 获取指定参数（带类型转换）
     *
     * @param key 参数名
     * @param clazz 目标类型
     * @return 参数值
     */
    @JsonIgnore
    public <T> T getParam(String key, Class<T> clazz) {
        Object value = getParam(key);
        if (value == null) {
            return null;
        }

        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }

        // 可以添加更多类型转换逻辑
        return null;
    }

    /**
     * 移除指定参数
     *
     * @param key 参数名
     * @return 被移除的参数值
     */
    @JsonIgnore
    public Object removeParam(String key) {
        return apiParams != null ? apiParams.remove(key) : null;
    }

    /**
     * 清空所有参数
     */
    @JsonIgnore
    public void clearParams() {
        if (apiParams != null) {
            apiParams.clear();
        }
    }

    /**
     * 获取所有参数
     */
    @JsonIgnore
    public Map<String, Object> getAllParams() {
        return apiParams != null ? Collections.unmodifiableMap(apiParams) : Collections.emptyMap();
    }

    /**
     * 检查是否包含指定参数
     *
     * @param key 参数名
     * @return 是否包含
     */
    @JsonIgnore
    public boolean hasParam(String key) {
        return apiParams != null && apiParams.containsKey(key);
    }
}
