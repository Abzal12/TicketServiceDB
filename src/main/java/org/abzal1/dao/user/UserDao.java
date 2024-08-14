package org.abzal1.dao.user;

import java.util.Optional;

public interface UserDao<T> {
    void saveUser(T t);
    Optional<T> fetchUserById(int id);
    void deleteUserbyId(int id);

}
