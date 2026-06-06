package net.itzq.mira.modules.ai.client.handle;

/**
 *
 * @discription
 *
 * @created 2026/3/7 17:37
 */
public interface HttpStreamEventInterface {

    void onEvent(String eventType, String data, String id);

    void onComment(String comment);

    void onComplete();

    void onError(Throwable t);
}
