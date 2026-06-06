package net.itzq.mira.modules.ai.persistence;

import net.itzq.mira.modules.ai.agent.AgentContextHolder;
import net.itzq.mira.modules.ai.client.openai.chat.entity.ChatMessage;

import java.util.List;

/**
 *  HistoryPersistHandler
 *
 *  @author tangzq
 */
public abstract class AbstractHistoryPersist {

    public abstract void saveChatMessages(List<ChatMessage> chatMessages, AgentContextHolder context);

    public abstract void saveEventMessages(List<?> eventMessages);

}
