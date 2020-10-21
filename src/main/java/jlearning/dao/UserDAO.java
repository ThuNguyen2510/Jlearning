package jlearning.dao;

import java.util.List;

import jlearning.model.User;
import jlearning.model.User.Role;

public interface UserDAO extends BaseDAO<Integer, User> {

	List<User> loadUsers();

	User findByEmail(String email);

	boolean checkEmailExist(String email);
	
	List<User> loadUsers(Role role);
	
}