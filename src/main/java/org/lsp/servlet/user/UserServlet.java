package org.lsp.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.lsp.pojo.Role;
import org.lsp.pojo.User;
import org.lsp.service.role.RoleServiceImpl;
import org.lsp.service.user.UserService;
import org.lsp.service.user.UserServiceImpl;
import org.lsp.utils.Constants;
import org.lsp.utils.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method != null && method.equals("savepwd")){
            this.updatePwd(req, resp);
        } else if (method != null && method.equals("pwdmodify")) {
            this.pwdModify(req, resp);
        } else if (method != null && method.equals("query")) {
            query(req,resp);
        }



    }

    private void query(HttpServletRequest req, HttpServletResponse resp) {
        String queryUserName = req.getParameter("queryname");
        String queryRole = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");

        UserServiceImpl userService = new UserServiceImpl();
        int pageSize = 5;
        int currentPageNo = 1;



        int queryUserRole = 0;
        if (queryUserName == null) {
            queryUserName = "";
        }
        if (queryRole != null && queryRole.equals("")) {
            queryUserRole = Integer.parseInt(queryRole);

        }
        if (pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);
        }

        int total = userService.getUserCount(queryUserName, queryUserRole);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(total);

        int totalPageCount = pageSupport.getTotalPageCount();
        if (totalPageCount < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        List<User> userList = null;
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);
        req.setAttribute("totalCount", total);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalPageCount", totalPageCount);

        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpwd = req.getParameter("oldpassword");
        boolean flag = false;

        Map<String, String> resultMap = new HashMap<String, String>();
        if (o == null) {
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpwd)) {
            resultMap.put("result", "error");
        } else {
            String usrPwd = ((User)o).getUserPassword();
            if (oldpwd.equals(usrPwd)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }
        resp.setContentType("application/json");
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        System.out.println("here");
        String newpwd = req.getParameter("newpassword");
        boolean flag = false;
        if (o!=null && !StringUtils.isNullOrEmpty(newpwd)) {
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User)o).getId(), newpwd);
            System.out.println(newpwd);
            if (flag) {
                req.setAttribute("message", "密码修改成功，请重新登录");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                req.setAttribute("message", "密码修改失败");
            }
        } else {
            req.setAttribute("message", "新密码有问题");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }


}
