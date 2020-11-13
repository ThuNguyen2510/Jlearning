package jlearning.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import jlearning.dao.GenericDAO;
import jlearning.dao.ResultDAO;
import jlearning.model.Result;

public class ResultDAOImpl extends GenericDAO<Integer, Result> implements ResultDAO {

	Logger logger = Logger.getLogger(ResultDAOImpl.class);

	public ResultDAOImpl() {
		super(Result.class);
	}

	public ResultDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public List<Result> rankingByTest(int testId) {
		List<Result> rs= getSession().createQuery("from Result b where b.test.id=: testId ORDER BY b.score DESC ")
				.setParameter("testId", testId).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).getResultList();
		return rs;
		
	}

}
