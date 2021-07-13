package org.lsp.service.user;

import org.junit.jupiter.api.Test;
import org.lsp.dao.BaseDao;
import org.lsp.dao.user.UserDao;
import org.lsp.dao.user.UserDaoImpl;
import org.lsp.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;


        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        if (user != null) {
            System.out.println(user.getUserPassword());
            if (!password.equals(user.getUserPassword())) {
                user = null;
            }
        }
        return user;
    }

    @Override
    public boolean updatePwd(int id, String pwd) {
        Connection connection = null;
        boolean flag = false;

        connection = BaseDao.getConnection();
        try {
            if (userDao.updatePwd(connection, id, pwd) > 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    public int getUserCount(String username, int userRole) {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, username, userRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return count;

    }

    public List<User> getUserList(String username, int queryUserRole, int currentPage, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        connection = BaseDao.getConnection();
        try {
            userList = userDao.getUserList(connection, username, queryUserRole, currentPage, pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return userList;
    }

    @Test
    public void test() {
        UserServiceImpl userService = new UserServiceImpl();
        User admin = userService.login("admin", "1234567");
        System.out.println(admin.getCreationDate());
    }

    @Test
    public void testGetUserCount() {
        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount(null, 3);
        System.out.println(userCount);
    }

}
