package net.itzq.mira.modules.ai.client.handle;

/**
 *  SseEventListener
 *
 *  @author tangzq
 */
public abstract class SseEventListener {

    public abstract void onEvent(String eventType, String data, String id, StreamEventHandler handler);

    public abstract void onComment(String comment, StreamEventHandler handler);

    public abstract void onComplete(StreamEventHandler handler);

    public abstract void onError(Throwable t, StreamEventHandler handler);

}
