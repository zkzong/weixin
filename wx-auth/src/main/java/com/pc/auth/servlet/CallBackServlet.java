package com.pc.auth.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.pc.auth.util.AuthUtil;

public class CallBackServlet extends HttpServlet {
    private String dbUrl;
    private String driverName;
    private String userName;
    private String passWord;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            this.dbUrl = config.getInitParameter("dbUrl");
            this.driverName = config.getInitParameter("driverName");
            this.userName = config.getInitParameter("userName");
            this.passWord = config.getInitParameter("passWord");
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String code = req.getParameter("code");
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + AuthUtil.APPID
                + "&secret=" + AuthUtil.APPSECRET
                + "&code=" + code
                + "&grant_type=authorization_code";
        JSONObject jsonObject = AuthUtil.doGetJson(url);
        String openid = jsonObject.getString("openid");
        String token = jsonObject.getString("access_token");
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token
                + "&openid=" + openid
                + "&lang=zh_CN";
        JSONObject userInfo = AuthUtil.doGetJson(infoUrl);
        System.out.println(userInfo);
        String unionid = userInfo.getString("unionid");

        //1��ʹ��΢���û���Ϣֱ�ӵ�¼������ע��Ͱ�
        //req.setAttribute("info", userInfo);
        //req.getRequestDispatcher("/index1.jsp").forward(req, resp);

        //2����΢���뵱ǰϵͳ���˺Ž��а�
        try {
            String nickName = getNickName(unionid);
            if (!"".equals(nickName)) {
                //�󶨳ɹ�
                req.setAttribute("nickName", nickName);
                req.getRequestDispatcher("/index2.jsp").forward(req, resp);
            } else {
                //δ��
                req.setAttribute("unionid", unionid);
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getNickName(String unionid) throws SQLException {
        String nickName = "";
        conn = DriverManager.getConnection(dbUrl, userName, passWord);
        String sql = "select nickname from user where unionid=?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, unionid);
        rs = ps.executeQuery();
        while (rs.next()) {
            nickName = rs.getString("NICKNAME");
        }
        rs.close();
        ps.close();
        conn.close();
        return nickName;
    }

    public int updUser(String unionid, String account, String password) throws SQLException {
        conn = DriverManager.getConnection(dbUrl, userName, passWord);
        String sql = "update user set unionid=? where account=? and password=?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, unionid);
        ps.setString(2, account);
        ps.setString(3, password);
        int temp = ps.executeUpdate();

        rs.close();
        ps.close();
        conn.close();
        return temp;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String account = req.getParameter("account");
        String password = req.getParameter("password");
        String unionid = req.getParameter("unionid");
        try {
            int temp = updUser(unionid, account, password);
            if (temp > 0) {
                System.out.println("�˺Ű󶨳ɹ�");
            } else {
                System.out.println("�˺Ű�ʧ��");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
