package com.eugene.webchatapp.Filters;


import com.eugene.webchatapp.storage.StaticKeyStorage;
import com.sun.deploy.net.HttpRequest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(value = "/homepage.jsp")
public class Filter implements javax.servlet.Filter {


    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String uidParam = servletRequest.getParameter("uid");
        if (uidParam == null && servletRequest instanceof HttpServletRequest ) {
            Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("uid")) {

                    uidParam = cookie.getValue();
                }
            }
        }
        boolean authenticated = checkAuthenticated(uidParam);

        if (authenticated) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (servletResponse instanceof HttpServletResponse) {
            ((HttpServletResponse) servletResponse).sendRedirect("/");
        } else {
            servletResponse.getOutputStream().println("403, Forbidden");
        }

    }

    private boolean checkAuthenticated(String uid){
        return StaticKeyStorage.getUserByUid(uid) != null;
    }

    public void destroy() {

    }
}
