package net.itzq.mira.modules.ai.client.openai.realtime.entity;

/**
 * SessionUpdated
 */
public class SessionUpdated {
    private String event_id;
    private String type = "session.updated";
    private Session session;
}
