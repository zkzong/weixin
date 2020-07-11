package com.pc.auth.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pc.auth.util.AuthUtil;

@WebServlet("/pcLogin")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String backUrl = "http://zongzhankui.xicp.net/WxAuth/pcCallBack";
        String url = "https://open.weixin.qq.com/connect/qrconnect?appid=" + AuthUtil.APPID
                + "&redirect_uri=" + URLEncoder.encode(backUrl)
                + "&response_type=code"
                + "&scope=snsapi_login"
                + "&state=STATE#wechat_redirect";
        resp.sendRedirect(url);
    }
}
