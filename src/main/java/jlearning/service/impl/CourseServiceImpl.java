package jlearning.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.bean.LessonInfo;
import jlearning.bean.VocabInfo;
import jlearning.model.Course;
import jlearning.model.Lesson;
import jlearning.model.User;
import jlearning.model.Vocabulary;
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

	@Override
	public void create(List<LessonInfo> lessons, Course course) {
		getCourseDAO().saveOrUpdate(course);
		for (int i = 0; i < lessons.size(); i++) {
			/*
			 * Lesson lesson = new Lesson(); lesson.setName(lessons.get(i).getName());
			 * lesson.setDescription(lessons.get(i).getDescription());
			 * lesson.setCourse(course); getLessonDAO().saveOrUpdate(lesson);
			 */
			LessonInfo info = lessons.get(i);
			Lesson lesson = createLessonWithListContent(info, course.getId());
			if (info.getVocabs() != null && info.getVocabs().size() > 0)
				for (int j = 0; j < info.getVocabs().size(); j++) {
					createVocab(info.getVocabs().get(j), lesson.getId());
				}

		}

	}

	public Lesson createLessonWithListContent(LessonInfo lesson, int courseId) {

		Lesson newLesson = new Lesson();
		Course course = getCourseDAO().findById(courseId);
		newLesson.setCourse(course);
		newLesson.setDescription(lesson.getDescription());
		newLesson.setName(lesson.getName());
		getLessonDAO().saveOrUpdate(newLesson);
		if (lesson.getVocabs() != null && lesson.getVocabs().size() > 0)
			for (int i = 0; i < lesson.getVocabs().size(); i++) {
				createVocab(lesson.getVocabs().get(i), newLesson.getId());
			}
		/*
		 * if (lesson.getGrams() != null && lesson.getGrams().size() > 0) for (int i =
		 * 0; i < lesson.getGrams().size(); i++) { createGram(lesson.getGrams().get(i),
		 * newLesson.getId()); } if (lesson.getListens() != null &&
		 * lesson.getListens().size() > 0) for (int i = 0; i <
		 * lesson.getListens().size(); i++) { createListen(lesson.getListens().get(i),
		 * newLesson.getId()); }
		 */

		return getLessonDAO().saveOrUpdate(newLesson);

	}

	public void createVocab(VocabInfo vocab, int lessonId) {
		Vocabulary vocabulary = new Vocabulary();
		Lesson lesson = getLessonDAO().findById(lessonId);
		vocabulary.setAudio(vocab.getAudio());
		vocabulary.setContent(vocab.getContent());
		vocabulary.setKanji(vocab.getKanji());
		vocabulary.setMeans(vocab.getMeans());
		vocabulary.setLesson(lesson);
		getVocabularyDAO().saveOrUpdate(vocabulary);

	}

}
