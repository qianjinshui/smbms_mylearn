package org.lsp.dao.user;

import org.lsp.pojo.Role;
import org.lsp.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    public int updatePwd(Connection connection, int id, String password) throws SQLException;


    int getUserCount(Connection connection, String username, int userRole) throws SQLException;

    List<User> getUserList(Connection connection, String userName, int userRole, int currentPage, int pageSize) throws SQLException;

}
