package jlearning.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.model.Course;
import jlearning.model.User;
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

		return getCourseDAO().saveOrUpdate(entity);
	}

	@Override
	public boolean delete(Course entity) {
		try {
			getCourseDAO().delete(entity);
			return true;
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public List<Course> LatestCourses() {

		return getCourseDAO().LatestCourses();
	}

	@Override
	public Course findByLevel(int level) {

		return getCourseDAO().findByLevel(level);
	}

	@Override
	public Course deleteCourse(int id) {
		try {
			Course course = getCourseDAO().findById(id);
			course.setDelete_time(new Date());
			saveOrUpdate(course);
			return saveOrUpdate(course);
		} catch (Exception e) {
			throw e;
		}
	}

}
