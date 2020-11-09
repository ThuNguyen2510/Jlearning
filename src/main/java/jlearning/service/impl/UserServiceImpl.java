package jlearning.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.model.Blog;
import jlearning.model.History;
import jlearning.model.Result;
import jlearning.model.User;
import jlearning.model.User.Role;
import jlearning.service.UserService;
import jlearning.service.impl.UserServiceImpl;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Override
	public User findById(Serializable key) {
		try {
			User user = getUserDAO().findById(key);
			List<Result> results = user.getResults();
			results.size();
			return user;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public User saveOrUpdate(User entity) {
		try {
			return getUserDAO().saveOrUpdate(entity);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public boolean delete(User entity) {
		try {
			getUserDAO().delete(entity);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<User> loadUsers() {
		return getUserDAO().loadUsers();
	}

	@Override
	public User findByEmailAndPassword(String usermail, String password) {
		try {
			User user = getUserDAO().findByEmail(usermail.trim());
			logger.info(user);
			if (password.equals(user.getPassword())) {
				return user;
			}
			return null;

		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public boolean checkEmailExist(String email) {
		try {
			// true: exist
			return getUserDAO().checkEmailExist(email);
		} catch (Exception e) {

			logger.error(e);

		}
		return false;
	}

	@Override
	public boolean createUser(User user) {
		try {
			if (checkEmailExist(user.getEmail()))
				return false;
			user.setRole(Role.USER);
			getUserDAO().saveOrUpdate(user);
			return true;

		} catch (Exception ex) {
			logger.error("Error in saveUserAfterRegister: " + ex.getMessage());
			throw ex;
		}
	}

	@Override
	public boolean deleteUser(Integer id) {
		try {
			User user = getUserDAO().findById(id);
			user.setDelete_time(new Date());
			saveOrUpdate(user);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<User> loadUsers(Role role) {
		return getUserDAO().loadUsers(role);
	}

	@Override
	public List<History> loadHistory(int userId,int type) {
		User user= getUserDAO().findById(userId);
		List<History> his= new ArrayList<History>();
		int j=0;
		while(j<user.getHistories().size()) {
			if((user.getHistories().get(j).getActivityType())==type)
				his.add(user.getHistories().get(j));
			j++;
		}
		
		return his;
	}

	@Override
	public List<Blog> loadBlogs(int userId) {
		List<Blog> list = getUserDAO().findById(userId).getBlogs();
		list.size();
		return list;
	}

}