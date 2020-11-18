package jlearning.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.bean.GramInfo;
import jlearning.bean.LessonInfo;
import jlearning.bean.ListenInfo;
import jlearning.bean.VocabInfo;
import jlearning.model.Course;
import jlearning.model.Grammar;
import jlearning.model.Lesson;
import jlearning.model.Listening;
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
		if (lesson.getGrams() != null && lesson.getGrams().size() > 0) {
			if (lesson.getGrams().size() <= 6)
				for (int i = 0; i < lesson.getGrams().size(); i++) {
					createGram(lesson.getGrams().get(i), newLesson.getId());
				}
			else {
				for (int i = 0; i < 6; i++) {
					createGram(lesson.getGrams().get(i), newLesson.getId());
				}
			}
		}

		if (lesson.getListens() != null && lesson.getListens().size() > 0) {
			if(lesson.getListens().size()<=6) {
				for (int i = 0; i < lesson.getListens().size(); i++) {
					createListen(lesson.getListens().get(i), newLesson.getId());
				}
			}else {
				for (int i = 0; i < 6; i++) {
					createListen(lesson.getListens().get(i), newLesson.getId());
				}
			}
		}
			

		return getLessonDAO().saveOrUpdate(newLesson);

	}

	public void createGram(GramInfo gram, int lessonId) {
		Grammar gramNew = new Grammar();
		gramNew.setContent(gram.getContent());
		gramNew.setName(gram.getName());
		gramNew.setDescription(gram.getDescription());
		Lesson lesson = getLessonDAO().findById(lessonId);
		gramNew.setLesson(lesson);
		getGrammarDAO().saveOrUpdate(gramNew);

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

	public void createListen(ListenInfo listen, int lessonId) {
		Listening listenNew = new Listening();
		Lesson lesson = getLessonDAO().findById(lessonId);
		listenNew.setAudio(listen.getAudio());
		listenNew.setImage(listen.getImage());
		if (listen.getContent1() != null)
			listenNew.setContent1(listen.getContent1());
		if (listen.getContent2() != null)
			listenNew.setContent2(listen.getContent2());
		if (listen.getContent3() != null)
			listenNew.setContent3(listen.getContent3());
		if (listen.getContent4() != null)
			listenNew.setContent4(listen.getContent4());
		if (listen.getContent5() != null)
			listenNew.setContent5(listen.getContent5());
		if (listen.getContent6() != null)
			listenNew.setContent6(listen.getContent6());
		listenNew.setLesson(lesson);
		getListeningDAO().saveOrUpdate(listenNew);

	}

}
