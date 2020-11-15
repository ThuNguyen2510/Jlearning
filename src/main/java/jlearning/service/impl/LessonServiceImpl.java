package jlearning.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.weaver.patterns.TypePatternQuestions.Question;

import jlearning.model.Answer;
import jlearning.model.Grammar;
import jlearning.model.Lesson;
import jlearning.model.Listening;
import jlearning.model.Vocabulary;
import jlearning.model.Test;

import jlearning.service.LessonService;

public class LessonServiceImpl extends BaseServiceImpl implements LessonService {

	private static final Logger logger = Logger.getLogger(LessonServiceImpl.class);

	@Override
	public Lesson findById(Serializable key) {
		try {
			Lesson lesson = getLessonDAO().findById(key);
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

}
