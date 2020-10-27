package jlearning.service;

import java.util.List;

import jlearning.model.Course;

public interface CourseService extends BaseService<Integer,Course> {
	List<Course> loadCourses();
	List<Course> LatestCourses();
}
