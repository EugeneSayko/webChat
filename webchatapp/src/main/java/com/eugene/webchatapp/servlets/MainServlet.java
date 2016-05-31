package com.eugene.webchatapp.servlets;

import com.eugene.webchatapp.encryption.HashCode;
import com.eugene.webchatapp.service.UserDAOService;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by eugene on 12.05.16.
 */

@WebServlet(value = "/")
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserDAOService userDAOService = new UserDAOService();
        Cookie[] cookies = req.getCookies();

        String uidParam = req.getParameter("uid");
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("uid")){
                uidParam = cookie.getValue();
            }
        }

        if(userDAOService.getUserByUid(uidParam) != null){
            resp.sendRedirect("/homepage.jsp");
            return;
        }

        req.getRequestDispatcher("login.jsp");
    }

}
