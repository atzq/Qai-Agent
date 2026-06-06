package net.itzq.mira.modules.ai.entity.chat;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 *  Response
 *
 *  @author tangzq
 */
@Data
public class AnsResponse {

    String type;

    String answer;

    String historyId;

    String agentId;

    String parentAgentId;

    String agentName;

    String roundId;

    String msgId;

    JSONObject info;

}
