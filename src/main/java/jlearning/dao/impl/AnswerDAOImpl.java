package jlearning.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import jlearning.dao.AnswerDAO;
import jlearning.dao.GenericDAO;
import jlearning.model.Answer;

public class AnswerDAOImpl extends GenericDAO<Integer, Answer> implements AnswerDAO {
	Logger logger = Logger.getLogger(AnswerDAOImpl.class);

	public AnswerDAOImpl() {
		super(Answer.class);
	}

	public AnswerDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
