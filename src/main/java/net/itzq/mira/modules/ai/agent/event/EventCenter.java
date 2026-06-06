package net.itzq.mira.modules.ai.agent.event;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.itzq.mira.modules.ai.agent.event.type.*;
import net.itzq.mira.modules.ai.client.sse.SseException;

import java.util.concurrent.CopyOnWriteArrayList;

@Data
@Slf4j
public class EventCenter {

    private final CopyOnWriteArrayList<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void register(EventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void unregister(EventListener listener) {
        listeners.remove(listener);
    }

    // ---------------------- 事件触发方法（无参构造 + setter） ----------------------

    public void fireEvent(GeneralEvent event) {

        if (event.isIgnore()) {
            return;
        }

        for (EventListener listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    public void fireChatInput(ChatInputEvent event) {

        if (event.isIgnore()) {
            return;
        }

        for (EventListener listener : listeners) {
            try {
                listener.onChatInput(event);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    public void fireStepBegin(StepBeginEvent event) {

        if (event.isIgnore()) {
            return;
        }

        for (EventListener listener : listeners) {
            try {
                listener.onStepBegin(event);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    public void fireStepEnd(StepEndEvent event) {

        if (event.isIgnore()) {
            return;
        }

        for (EventListener listener : listeners) {
            try {
                listener.onStepEnd(event);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    public void fireChatEnd(ChatEndEvent event) {

        if (event.isIgnore()) {
            return;
        }

        for (EventListener listener : listeners) {
            try {
                listener.onChatEnd(event);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    public void fireCallToolBegin(CallToolBeginEvent event) {

        if (event.isIgnore()) {
            return;
        }

        for (EventListener listener : listeners) {
            try {
                listener.onCallToolBegin(event);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    public void fireCallToolEnd(CallToolEndEvent event) {

        if (event.isIgnore()) {
            return;
        }

        for (EventListener listener : listeners) {
            try {
                listener.onCallToolEnd(event);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    public void fireError(ErrorEvent event) {

        if (event.isIgnore()) {
            return;
        }

        for (EventListener listener : listeners) {
            try {
                listener.onError(event);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private void handleException(Exception e) {
        if (e instanceof SseException) {
            throw (SseException) e;
        }
        log.error("Event listener execution error", e);
    }
}
