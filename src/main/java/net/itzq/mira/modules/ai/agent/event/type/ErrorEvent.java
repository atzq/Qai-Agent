package net.itzq.mira.modules.ai.agent.event.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 错误事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEvent extends BaseEvent {
    private Throwable throwable;
}
