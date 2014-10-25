package com.runssnail.monolith.villadom.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexAction {

    @RequestMapping("index.htm")
    public String index(HttpServletRequest req, HttpServletResponse res, HttpSession session) {

        return "index";
    }

}
