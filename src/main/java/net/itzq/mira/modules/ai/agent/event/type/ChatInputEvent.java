package net.itzq.mira.modules.ai.agent.event.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 聊天输入事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatInputEvent extends BaseEvent {
    private String question;
}
