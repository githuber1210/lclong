package com.example.common.result;

import com.example.common.constans.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int code;
    private String msg;
    private Object data;

    public static Result success(Object data) {
        return buildResult(ResultCode.SUCCESS, Constants.SUCCESS_MSG, data);
    }

    public static Result success() {
        return buildResult(ResultCode.SUCCESS, Constants.SUCCESS_MSG, null);
    }

    public static Result fail(String msg) {
        return buildResult(ResultCode.FAIL, msg, null);
    }

    public static Object forbidden(String msg) {
        return buildResult(ResultCode.NOT_FOUND, msg, null);
    }

    public static Object unauthorized(String msg) {
        return buildResult(ResultCode.UNAUTHORIZED,msg,null);
    }

    public static Result buildResult(ResultCode resultCode, String msg, Object data) {
        return buildResult(resultCode.code, msg, data);
    }

    public static Result buildResult(int resultCode, String msg, Object data) {
        return new Result(resultCode, msg, data);
    }



}
