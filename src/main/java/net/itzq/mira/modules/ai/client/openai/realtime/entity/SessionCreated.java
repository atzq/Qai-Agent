package net.itzq.mira.modules.ai.client.openai.realtime.entity;

/**
 * SessionCreated
 */
public class SessionCreated {
    private String event_id;
    private String type = "session.created";
    private Session session;
}
