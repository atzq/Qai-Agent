package net.itzq.mira.modules.ai.client.handle;

import net.itzq.mira.modules.ai.client.openai.tool.ToolCall;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @discription
 *
 * @created 2026/3/7 18:06
 */
public abstract class StreamEventHandler implements HttpStreamEventInterface {

    public abstract CountDownLatch getCountDownLatch();

    public abstract boolean needToolCall();

    public abstract String getCurrStr();

    public abstract String getCurrData();

    public abstract boolean isReasoning();

    public abstract boolean isChatAnswer();

    public abstract StringBuilder getReasoningOutput();

    public abstract StringBuilder getAnswerOutput();

    public abstract List<ToolCall> getToolCalls();
}
