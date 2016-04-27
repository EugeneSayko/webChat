package com.eugene.webchatapp.servlets;

import com.eugene.webchatapp.encryption.HashCode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.NoSuchAlgorithmException;

/**
 * Created by eugene on 25.04.16.
 */

@WebServlet(value = "/vars")
public class VarsServlet extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final String[] vars = {"JAVA_HOME", "M2_HOME"};

        for(String var : vars){
            resp.getOutputStream().println(String.format("%s=%s", var, System.getenv(var)));
        }

    }

}
