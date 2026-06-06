package net.itzq.mira.modules.ai.entity.chat;

import lombok.Data;

/**
 *  MsgInfo
 *
 *  @author tangzq
 */
@Data
public class ReplyId {

    String historyId;

    String agentId;

    String parentAgentId;

    String roundId;

    Integer step;

    String msgId;

    String agentName;

    public ReplyId(String historyId, String agentId, String roundId,Integer step, String msgId, String agentName) {
        this.historyId = historyId;
        this.agentId = agentId;
        this.roundId = roundId;
        this.step = step;
        this.msgId = msgId;
        this.agentName = agentName;
    }
}
