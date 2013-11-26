package com.kongur.monolith.web.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kongur.monolith.biz.domain.user.UserDO;

@Controller
public class LoginoutAction {

    public static final String USER_NAME_KEY = "userName";

    @RequestMapping(value = "login.htm", method = RequestMethod.GET)
    public String login(@ModelAttribute("user")
    UserDO user) {

        return "login";
    }

    /**
     * µÇÂ¼
     * 
     * @param user
     * @param req
     * @return
     */
    @RequestMapping(value = "login.htm", method = RequestMethod.POST)
    public String doLogin(@ModelAttribute("user")
    UserDO user, HttpServletRequest req) {

        if ("admin".equals(user.getUserName()) && "123456".equals(user.getPassword())) {
            req.getSession().setAttribute(USER_NAME_KEY, user.getUserName());

            return "success";
        }

        return "error";
    }

    /**
     * ÍË³ö
     * 
     * @param req
     * @return
     */
    @RequestMapping(value = "logout.htm")
    public String logout(HttpServletRequest req) {
        req.getSession().removeAttribute(USER_NAME_KEY);

        return "success";
    }
}
