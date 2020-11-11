package jlearning.dao.impl;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import jlearning.dao.CommentDAO;
import jlearning.dao.GenericDAO;

import jlearning.model.Comment;

public class CommentDAOImpl extends GenericDAO<Integer, Comment> implements CommentDAO {
	Logger logger = Logger.getLogger(CommentDAOImpl.class);

	public CommentDAOImpl() {
		super(Comment.class);
	}

	public CommentDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

}
