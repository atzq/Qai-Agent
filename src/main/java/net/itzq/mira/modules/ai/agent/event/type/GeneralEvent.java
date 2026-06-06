package net.itzq.mira.modules.ai.agent.event.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.itzq.mira.modules.ai.client.handle.StreamEventHandler;

/**
 * 通用事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralEvent extends BaseEvent {
    private String eventType;
    private String data;
    private String id;
    private StreamEventHandler handler;
}
