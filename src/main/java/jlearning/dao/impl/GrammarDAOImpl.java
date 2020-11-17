package jlearning.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import jlearning.dao.GenericDAO;
import jlearning.dao.GrammarDAO;
import jlearning.model.Course;
import jlearning.model.Grammar;

public class GrammarDAOImpl extends GenericDAO<Integer, Grammar> implements GrammarDAO {

	Logger logger = Logger.getLogger(GrammarDAOImpl.class);

	public GrammarDAOImpl() {
		super(Grammar.class);
	}

	public GrammarDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public List<Grammar> list(int lessonId) {
		// TODO Auto-generated method stub
		return null;
	}

}
