package jlearning.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import jlearning.dao.CourseDAO;
import jlearning.dao.GenericDAO;
import jlearning.model.Course;

public class CourseDAOImpl extends GenericDAO<Integer, Course> implements CourseDAO {

	Logger logger = Logger.getLogger(CourseDAOImpl.class);

	public CourseDAOImpl() {
		super(Course.class);
	}

	public CourseDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public List<Course> loadCourses() {

		return getSession().createQuery("from Course").getResultList();
	}

	@Override
	public List<Course> LatestCourses() {
		// TODO Auto-generated method stub
		return getSession().createQuery("from Course b ORDER BY b.create_time DESC ").getResultList();
	}

}
