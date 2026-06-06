package net.itzq.mira.modules.ai.entity.chat;

/**
 *  AnsType
 *
 *  @author tangzq
 */
public enum AnsType {

    ChatContent,// 普通对话返回

    ChatReasonContent,// 思考对话返回

    Complete,// 结束标志

    Reference,// 引用

    FileContent,// 文件内容

    Error,// error

    Tool,// tool

    Progress,// 进度

    Tips,// 提示

    Field,// 列定义

    StepBegin,// 步骤开始

    StepEnd,// 步骤结束

    CallToolBegin,// 工具开始

    CallToolEnd,// 工具结束

    ChatEnd,// 对话结束

    UserInput,// 用户输入


}
