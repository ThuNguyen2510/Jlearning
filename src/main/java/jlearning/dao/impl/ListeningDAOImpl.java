package jlearning.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import jlearning.dao.GenericDAO;
import jlearning.dao.ListeningDAO;
import jlearning.model.Listening;

public class ListeningDAOImpl extends GenericDAO<Integer, Listening> implements ListeningDAO{
	Logger logger = Logger.getLogger(ListeningDAOImpl.class);

	public ListeningDAOImpl() {
		super(Listening.class);
	}

	public ListeningDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	@Override
	public List<Listening> list(int lessonId) {
		// TODO Auto-generated method stub
		return null;
	}

}
