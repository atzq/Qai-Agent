package net.itzq.mira.modules.ai.agent.event;

import net.itzq.mira.modules.ai.agent.event.type.*;

/**
 *  EventListener
 *
 *  @author tangzq
 */
public interface EventHook {

    default void onEvent(GeneralEvent event) {}

    default void onChatInput(ChatInputEvent event) {}

    default void onStepBegin(StepBeginEvent event) {}

    default void onStepEnd(StepEndEvent event) {}

    default void onChatEnd(ChatEndEvent event) {}

    default void onCallToolBegin(CallToolBeginEvent event) {}

    default void onCallToolEnd(CallToolEndEvent event) {}

    default void onError(ErrorEvent event) {}

}
