package jlearning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import jlearning.dao.BlogDAO;
import jlearning.dao.UserDAO;

public class BaseServiceImpl {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private BlogDAO blogDAO;

	public BlogDAO getBlogDAO() {
		return blogDAO;
	}

	public void setBlogDAO(BlogDAO blogDAO) {
		this.blogDAO = blogDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
}
