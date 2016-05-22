package com.eugene.webchatapp.servlets;

import com.eugene.webchatapp.encryption.HashCode;
import com.eugene.webchatapp.storage.StaticKeyStorage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(value = "/login", initParams = {
        @WebInitParam(name = "cookie-live-time", value = "300")
})
public class LoginServlet extends HttpServlet{

    private int cookieLifeTime = -1;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cookieLifeTime = Integer.parseInt(config.getInitParameter("cookie-live-time"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = null;
        try {
            password = HashCode.encryptPassword(req.getParameter("password"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(username == null || username.trim().isEmpty()){
            resp.sendRedirect("/");
            return;
        }

        if(password == null || password.isEmpty()){
            resp.sendRedirect("/");
            return;

        }

        if(!StaticKeyStorage.isPassword(username, password)){
            resp.sendRedirect("/");
            return;
        }

        String userId = StaticKeyStorage.getByUsername(username);

        if(userId == null){
            resp.sendRedirect("/");
            return;
        }

        Cookie userIdCookie = new Cookie("uid", userId);
        userIdCookie.setMaxAge(cookieLifeTime);
        resp.addCookie(userIdCookie);

        resp.sendRedirect("/homepage.jsp");

    }
}
