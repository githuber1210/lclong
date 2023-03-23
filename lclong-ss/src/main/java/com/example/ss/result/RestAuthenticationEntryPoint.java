package com.example.ss.result;

import cn.hutool.json.JSONUtil;
import com.example.common.result.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无token或token失效
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //设置响应的字符编码和内容类型。
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        //自定义返回结果
        response.getWriter().println(JSONUtil.parse(Result.unauthorized("请先登录!")));
        response.getWriter().flush();
    }
}
