package jlearning.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.model.Course;
import jlearning.service.CourseService;

public class CourseServiceImpl extends BaseServiceImpl implements CourseService {

	private static final Logger logger = Logger.getLogger(BlogServiceImpl.class);

	@Override
	public List<Course> loadCourses() {
		return getCourseDAO().loadCourses();
	}

	@Override
	public Course findById(Serializable key) {
		try {
			return getCourseDAO().findById(key);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public Course saveOrUpdate(Course entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Course entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Course> LatestCourses() {
		
		return getCourseDAO().LatestCourses();
	}

}
