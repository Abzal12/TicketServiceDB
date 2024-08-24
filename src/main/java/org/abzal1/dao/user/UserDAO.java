package org.abzal1.dao.user;

import org.abzal1.model.user.User;

public interface UserDAO {

    void saveUser(User user);
    User fetchUserById(Long userId);
    void deleteUserById(Long userId);
    void updateUserNameById(long id, String name);
}
