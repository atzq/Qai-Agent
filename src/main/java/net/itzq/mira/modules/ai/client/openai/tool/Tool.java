package net.itzq.mira.modules.ai.client.openai.tool;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tool {

    /**
     * 工具类型，目前为“function”
     */
    private String type;
    private Function function;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Function {

        /**
         * 函数名称
         */
        private String name;

        /**
         * 函数描述
         */
        private String description;

        @JsonIgnore
        private String display;

        @JsonIgnore
        private Boolean subAgent;

        /**
         * 函数的参数 key为参数名称，value为参数属性
         */
        private Parameter parameters;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Parameter {

            private String type = "object";

            /**
             * 函数的参数 key为参数名称，value为参数属性
             */
            private Map<String, Property> properties;

            /**
             * 必须的参数
             */
            private List<String> required;

        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Property {
            /**
             * 属性类型
             */
            private String type;

            /**
             * 属性描述
             */
            private String description;

            /**
             * 枚举项
             */
            @JsonProperty("enum")
            private List<String> enumValues;
        }

    }

}
