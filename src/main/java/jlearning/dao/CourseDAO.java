package jlearning.dao;

import java.util.List;

import jlearning.model.Course;

public interface CourseDAO extends BaseDAO<Integer,Course> {
	
	List<Course> loadCourses();
	List<Course> LatestCourses();
}
