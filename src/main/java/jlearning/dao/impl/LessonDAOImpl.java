package jlearning.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import jlearning.dao.GenericDAO;
import jlearning.dao.LessonDAO;
import jlearning.model.Lesson;
import jlearning.model.Test.Type;

public class LessonDAOImpl extends GenericDAO<Integer,Lesson> implements LessonDAO{

	Logger logger = Logger.getLogger(LessonDAOImpl.class);

	public LessonDAOImpl() {
		super(Lesson.class);
	}

	public LessonDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public List<Lesson> loadAllLessons() {
		// TODO Auto-generated method stub
		return getSession().createQuery("from Lesson").getResultList();
	}

	@Override
	public List<Lesson> loadByType(Type type) {
		// TODO Auto-generated method stub
		return getSession().createQuery("from Lesson where type=: type").setParameter("type", type).getResultList();
	}

}
