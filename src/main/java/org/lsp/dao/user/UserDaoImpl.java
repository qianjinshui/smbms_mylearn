package org.lsp.dao.user;

import com.mysql.jdbc.StringUtils;
import org.lsp.dao.BaseDao;
import org.lsp.pojo.Role;
import org.lsp.pojo.User;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {

        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;

        if (connection != null) {

            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};

            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreateBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));

            }
            BaseDao.closeResource(null, pstm, rs);

        }
        return user;
    }

    @Override
    public int updatePwd(Connection connection, int id, String password) throws SQLException {
        PreparedStatement pstm = null;

        int execute = 0;
        if (connection != null) {
            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object param[] = {password, id};
            execute = BaseDao.execute(connection, pstm, sql, param);
            BaseDao.closeResource(null, pstm, null);
        }
        return execute;
    }

    @Override
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<Object> list = new ArrayList<Object>();
        int count = 0;
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id ");

            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append("and u.userName like ?");
                list.add("%" + username + "%");
            }
            if (userRole > 0) {
                sql.append("and u.userRole = ?");
                list.add(userRole);
            }

            Object[] objects = list.toArray();
            System.out.println("UserDaoImpl->getUserCount:" + sql.toString());

            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), objects);

            if (rs.next()) {
                count = rs.getInt("count");
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return count;
    }

    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPage, int pageSize) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<User>();

        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id ");
            List<Object> list = new ArrayList<>();

            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append("and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append("and u.userRole = ?");
                list.add(userRole);
            }
            sql.append("order by creationDate DESC limit ?,?");
            currentPage = (currentPage - 1) * pageSize;
            list.add(currentPage);
            list.add(pageSize);
            Object[] params = list.toArray();
            System.out.println("sql" + sql.toString());
            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
            while (rs.next()) {
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setUserPassword(rs.getString("userPassword"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setAddress(rs.getString("address"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setCreateBy(rs.getInt("createdBy"));
                _user.setCreationDate(rs.getTimestamp("creationDate"));
                _user.setModifyBy(rs.getInt("modifyBy"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                _user.setModifyDate(rs.getTimestamp("modifyDate"));
                userList.add(_user);
                System.out.println(_user.getUserRole());
            }
        }
        return userList;
    }


}
