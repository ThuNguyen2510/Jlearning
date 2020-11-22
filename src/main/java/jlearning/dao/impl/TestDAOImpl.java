package jlearning.dao.impl;

import java.util.List;

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
	@SuppressWarnings("unchecked")
	@Override
	public List<Test> findByLevel(int level) {
		Type type=Type.EXAM;
		return  getSession().createQuery("from Test where type=: type AND level=: level").setParameter("type", type).setParameter("level", level).getResultList();
	}
	@Override
	public List<Test> loadAllTest() {
		// TODO Auto-generated method stub
		return  getSession().createQuery("from Test ").getResultList();
	}

}
