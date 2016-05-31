package com.eugene.webchatapp.servlets;

import com.eugene.webchatapp.encryption.HashCode;
import com.eugene.webchatapp.service.UserDAOService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by eugene on 19.05.16.
 */

@WebServlet(value = "/signup")
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/signup.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = null;
        String passwordConfirmation = null;

        try {
            password = HashCode.encryptPassword(req.getParameter("password"));
            passwordConfirmation = HashCode.encryptPassword(req.getParameter("password_confirmation"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(username == null || username.isEmpty()){
            resp.sendRedirect("/signup.html");
            return;
        }

        if(password == null || password.isEmpty()){
            resp.sendRedirect("/signup.html");
            return;
        }

        if(passwordConfirmation == null || passwordConfirmation.isEmpty()){
            resp.sendRedirect("/signup.html");
            return;
        }

        UserDAOService userDAOService = new UserDAOService();

        if(userDAOService.isUser(username)){
            resp.sendRedirect("/signup.html");
            return;
        }

        if(!password.equals(passwordConfirmation)){
            resp.sendRedirect("/signup.html");
            return;
        }

        if(userDAOService.addUser(username, password)){
            resp.sendRedirect("/homepage.jsp");
        }else{
            resp.sendRedirect("/signup.html");
        }

    }
}
