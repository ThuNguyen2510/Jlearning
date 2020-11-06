package jlearning.service;

import java.util.List;

import jlearning.model.History;
import jlearning.model.User;
import jlearning.model.User.Role;

public interface UserService extends BaseService<Integer, User> {

	List<User> loadUsers();

	User findByEmailAndPassword(String usermail, String password);

	boolean checkEmailExist(String email);

	boolean createUser(User user);

	boolean deleteUser(Integer id);
	
	List<User> loadUsers(Role role);
	
	List<History> loadHistory(int userId,int type);
}
