package com.kongur.monolith.web.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.kongur.monolith.session.MonoHttpSession;

public class MonoServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -8327812623335190919L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub

        System.out.println("in doget");

        String username = (String) req.getSession().getAttribute("username");

        String sessionId = (String) req.getSession().getAttribute(MonoHttpSession.SESSION_ID);
        
        System.out.println("sessionId=" + sessionId);
        
        if (StringUtils.isBlank(username)) {
            System.out.println("no username");
            req.getSession().setAttribute("username", "zhangsan");
        } else {
            System.out.println("username=" + username);
        }

        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub

        System.out.println("in doPost");

        super.doPost(req, resp);
    }

}
