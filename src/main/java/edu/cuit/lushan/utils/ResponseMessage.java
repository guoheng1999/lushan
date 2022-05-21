package edu.cuit.lushan.utils;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage implements Serializable {
    // 结果标记(true:执行成功 false:执行失败)
    private Boolean flag;
    // 消息状态码
    private Integer code;
    // 消息
    private String msg;
    // 返回数据
    private Object data;
    // 令牌
    private String token;

    public ResponseMessage(boolean flag, int code, String msg, Object data) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    /**
     * 响应成功(带返回数据)
     *
     * @param data 返回数据
     * @return Result
     */
    public static ResponseMessage success(Object data) {
        return new ResponseMessage(true, 2000, "操作成功！", data);
    }

    /**
     * 响应成功
     *
     * @return Result
     */
    public static ResponseMessage success() {
        return new ResponseMessage(true, 2000, "操作成功！", null);
    }

    /**
     * 响应成功(带错误码,消息提醒,数据)
     *
     * @return
     */
    public static ResponseMessage successCodeMsgData(Integer code, String msg, Object data) {
        return new ResponseMessage(true, code, msg, data);
    }

    /**
     * 响应成功(带错误码,消息提醒,数据)
     *
     * @return
     */
    public static ResponseMessage successCodeMsg(Integer code, String msg) {
        return new ResponseMessage(true, code, msg, null);
    }

    /**
     * 响应错误(不带状态码,带消息)
     *
     * @return Result
     */
    public static ResponseMessage error(String msg) {
        return new ResponseMessage(false, 2400, msg, null);
    }

    /**
     * 响应错误(带错误码,消息提醒)
     *
     * @return
     */
    public static ResponseMessage errorMsg(Integer code, String msg) {
        return new ResponseMessage(false, code, msg, null);
    }

    /**
     * 响应错误(带错误码,消息提醒,数据)
     *
     * @return
     */
    public static ResponseMessage errorMsg(Integer code, String msg, Object data) {
        return new ResponseMessage(false, code, msg, data);
    }

    /**
     * 系统未知错误响应错误(数据)
     *
     * @return
     */
    public static ResponseMessage serverError(Object data) {
        return new ResponseMessage(false, 2500, "系统未知错误，请重启浏览器或与管理员联系。", data);
    }

    /**
     * NotFound(数据)
     *
     * @return
     */
    public static ResponseMessage notFound(Object data) {
        return new ResponseMessage(false, 2404, "系统未找到该记录，请重启浏览器或与管理员联系。", data);
    }

    /**
     * Null Error(数据)
     *
     * @return
     */
    public static ResponseMessage nullError() {
        return nullError(null);
    }

    /**
     * Null Error(数据)
     *
     * @return
     */
    public static ResponseMessage nullError(Object data) {
        return new ResponseMessage(false, 2404, "请求数据不可为空。", data);
    }

    /**
     * Exists Error(数据)
     *
     * @return
     */
    public static ResponseMessage existsError(Object data) {
        return new ResponseMessage(false, 2500, "系统未知错误，请重启浏览器或与管理员联系。", data);
    }


}