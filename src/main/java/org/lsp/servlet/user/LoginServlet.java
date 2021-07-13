package org.lsp.servlet.user;


import org.lsp.pojo.User;
import org.lsp.service.user.UserService;
import org.lsp.service.user.UserServiceImpl;
import org.lsp.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet--start ....");

        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        System.out.println(userCode+" "+userPassword);
        UserService userService = new UserServiceImpl();

        User user = userService.login(userCode, userPassword);
        if (user != null) {
            req.getSession().setAttribute(Constants.USER_SESSION, user);
            resp.sendRedirect("jsp/frame.jsp");
        }else{
            req.setAttribute("error", "用户名或者密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
