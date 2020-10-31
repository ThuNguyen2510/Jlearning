package jlearning.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import jlearning.dao.GenericDAO;
import jlearning.dao.TestDAO;
import jlearning.model.Test;
import jlearning.model.Test.Type;

public class TestDAOImpl  extends GenericDAO<Integer, Test> implements TestDAO {

	Logger logger = Logger.getLogger(TestDAOImpl.class);
	
	public TestDAOImpl()
	{
		super(Test.class);
	}
	public TestDAOImpl(SessionFactory sessionFactory)
	{
		setSessionFactory(sessionFactory);
	}
	@Override
	public Test findByType(Type type) {
		return (Test) getSession().createQuery("from Test where type=: type").setParameter("type", type).getSingleResult();
	}

}