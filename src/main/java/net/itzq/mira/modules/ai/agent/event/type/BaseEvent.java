package net.itzq.mira.modules.ai.agent.event.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.itzq.mira.modules.ai.agent.AgentContextHolder;
import net.itzq.mira.modules.ai.entity.chat.ReplyId;

/**
 * 事件基类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEvent {
    protected ReplyId replyId;
    protected AgentContextHolder context;
    protected boolean ignore = false;
}
