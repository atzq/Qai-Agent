package net.itzq.mira.modules.ai.client.sse;

/**
 *  SseException
 *
 *  @author tangzq
 */
public class SseException extends RuntimeException {

    private String errCode;
    private String errMsg;
    private String errData;
    private Throwable exception;

    /** HTTP 状态码（用于错误分类，0 表示未知） */
    private int statusCode;

    public SseException() {
        this.errCode = "";
        this.errMsg = "";
        this.errData = "";
        this.statusCode = 0;
    }

    public SseException(Exception ex) {
        super(ex);
        this.errCode = "999999";
        this.errMsg = "请求失败!";
        this.errData = "";
        this.exception = ex;
        this.statusCode = 0;
    }

    public SseException(String errCode, Throwable ex) {
        super(ex);
        this.errCode = errCode;
        this.errMsg = "请求失败!";
        this.errData = "";
        this.exception = ex;
        this.statusCode = 0;
    }

    public SseException(String errMsg) {
        super(errMsg);
        this.errCode = "";
        this.errMsg = errMsg;
        this.errData = "";
        this.statusCode = 0;
    }

    public SseException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.errData = "";
        this.statusCode = 0;
    }

    public SseException(String errCode, String errMsg, String errData) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.errData = errData;
        this.statusCode = 0;
    }

    /**
     * 构造带 HTTP 状态码的 SseException
     *
     * @param statusCode HTTP 状态码
     * @param errCode    错误码
     * @param errMsg     错误消息
     * @param errData    错误数据
     */
    public SseException(int statusCode, String errCode, String errMsg, String errData) {
        super(errMsg);
        this.statusCode = statusCode;
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.errData = errData;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public String getErrData() {
        return errData;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
