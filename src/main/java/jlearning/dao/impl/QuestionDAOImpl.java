package jlearning.dao.impl;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import jlearning.dao.GenericDAO;
import jlearning.dao.QuestionDAO;
import jlearning.model.Question;


public class QuestionDAOImpl extends GenericDAO<Integer, Question> implements QuestionDAO {
	Logger logger = Logger.getLogger(QuestionDAOImpl.class);

	public QuestionDAOImpl() {
		super(Question.class);
	}

	public QuestionDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
