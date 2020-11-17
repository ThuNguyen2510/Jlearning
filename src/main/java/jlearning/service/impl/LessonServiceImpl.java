package jlearning.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.weaver.patterns.TypePatternQuestions.Question;

import jlearning.model.Answer;
import jlearning.model.Course;
import jlearning.model.Grammar;
import jlearning.model.Lesson;
import jlearning.model.Listening;
import jlearning.model.Vocabulary;
import jlearning.model.Test;

import jlearning.service.LessonService;
import jlearning.bean.GramInfo;
import jlearning.bean.LessonInfo;
import jlearning.bean.ListenInfo;
import jlearning.bean.VocabInfo;

public class LessonServiceImpl extends BaseServiceImpl implements LessonService {

	private static final Logger logger = Logger.getLogger(LessonServiceImpl.class);

	@Override
	public Lesson findById(Serializable key) {
		try {
			Lesson lesson = getLessonDAO().findById(key);
			if (lesson != null) {
				List<Vocabulary> listVocab = lesson.getVocabularies();
				List<Grammar> listGramr = lesson.getGrammars();
				List<Listening> listListening = lesson.getListenings();
				List<Test> test = lesson.getTests();
				for (int i = 0; i < test.size(); i++) {
					List<jlearning.model.Question> ques = test.get(i).getQuestions();
					ques.size();
					for (int j = 0; j < ques.size(); j++) {
						List<Answer> anss = ques.get(j).getAnswers();
						anss.size();
					}
				}
				listGramr.size();
				listListening.size();
				listVocab.size();
				test.size();
			}

			return lesson;

		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public Lesson saveOrUpdate(Lesson entity) {

		return getLessonDAO().saveOrUpdate(entity);
	}

	@Override
	public boolean delete(Lesson entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Lesson> loadAllLessons() {

		return getLessonDAO().loadAllLessons();
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

	@Override
	public boolean deleteGram(int id) {
		try {
			
			Grammar entity = getGrammarDAO().findById(id);
			Lesson lesson = getLessonDAO().findById(entity.getLesson().getId());
			lesson.getGrammars().remove(entity);
			logger.info("SIZE "+lesson.getGrammars().size());
			getLessonDAO().saveOrUpdate(lesson);
			logger.info("SIZE "+lesson.getGrammars().size());
			getGrammarDAO().delete(entity);
			return true;
		} catch (Exception e) {

		}

		return false;
	}

	@Override
	public void createGram(GramInfo gram, int lessonId) {
		Grammar gramNew = new Grammar();
		gramNew.setContent(gram.getContent());
		gramNew.setName(gram.getName());
		gramNew.setDescription(gram.getDescription());
		Lesson lesson = getLessonDAO().findById(lessonId);
		gramNew.setLesson(lesson);
		getGrammarDAO().saveOrUpdate(gramNew);
		
	}

	@Override
	public void createListen(ListenInfo vocab, int lessonId) {
		// TODO Auto-generated method stub
		
	}

}
