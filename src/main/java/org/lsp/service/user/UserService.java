package org.lsp.service.user;

import org.lsp.pojo.User;

public interface UserService {
    public User login(String userCode, String password);


    boolean updatePwd(int id, String pwd);
}
