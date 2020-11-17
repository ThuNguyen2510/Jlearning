package jlearning.service;

import java.util.List;

import jlearning.bean.LessonInfo;
import jlearning.model.Course;

public interface CourseService extends BaseService<Integer,Course> {
	List<Course> loadCourses();
	List<Course> LatestCourses();
	Course findByLevel(int level);
	Course deleteCourse(int level);
	void create(List<LessonInfo> lessons, Course course);
	
}
