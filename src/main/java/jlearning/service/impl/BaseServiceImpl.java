package jlearning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import jlearning.dao.UserDAO;

public class BaseServiceImpl {
	
	@Autowired
	private UserDAO userDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
}
