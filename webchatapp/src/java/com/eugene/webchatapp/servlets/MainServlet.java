package com.eugene.webchatapp.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by eugene on 27.04.16.
 */

@WebServlet(value = "/chat")
public class MainServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String logged = (String)req.getSession().getAttribute("isLogged");

        if(logged == null){
            req.getRequestDispatcher("/").forward(req, resp);
            return;
        }

        if(logged.equals("true")){
            req.getRequestDispatcher("homepage.jsp").forward(req, resp);
        }else{
            req.getRequestDispatcher("/").forward(req, resp);
        }

    }


}
