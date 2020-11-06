package jlearning.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import jlearning.dao.GenericDAO;
import jlearning.dao.HistoryDAO;
import jlearning.model.History;

public class HistoryDAOImpl extends GenericDAO<Integer, History> implements HistoryDAO {

	Logger logger = Logger.getLogger(HistoryDAOImpl.class);

	public HistoryDAOImpl() {
		super(History.class);
	}
	public HistoryDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	@Override
	public History findByObjectId(int objectId) {
		
		return null;
	}


}
