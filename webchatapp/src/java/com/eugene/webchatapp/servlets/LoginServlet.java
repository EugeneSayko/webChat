package com.eugene.webchatapp.servlets;

import com.eugene.webchatapp.encryption.HashCode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet{

    private Map<String, String> users = new HashMap<String, String>();

    {
        users.put("Eugene", "7110eda4d09e062aa5e4a390b0a572ac0d2c0220");
        users.put("user1", "b3daa77b4c04a9551b8781d03191fe098f325e67");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String username = req.getParameter("username");
            String password = HashCode.encryptPassword(req.getParameter("password"));

            for(Map.Entry<String, String> user : users.entrySet()){
                if(user.getKey().equals(username) && user.getValue().equals(password)){

                    req.getSession().setAttribute("isLogged", "true");
                    resp.sendRedirect("/chat");
                    return;
                }
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        req.setAttribute("errorLogged", "true");
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
