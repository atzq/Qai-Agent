package net.itzq.mira.modules.ai.agent.event.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 步骤开始事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepBeginEvent extends BaseEvent {
    private String name;
    private int deep;
}
