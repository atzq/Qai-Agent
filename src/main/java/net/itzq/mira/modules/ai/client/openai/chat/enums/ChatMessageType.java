package net.itzq.mira.modules.ai.client.openai.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ChatMessageType
 */
@Getter
@AllArgsConstructor
public enum ChatMessageType {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
    TOOL("tool"),
    ;

    private final String role;

}
