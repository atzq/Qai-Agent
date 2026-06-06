package net.itzq.mira.modules.ai.agent.event.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 聊天结束事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatEndEvent extends BaseEvent {
    private boolean success;
    private Exception exception;
}
