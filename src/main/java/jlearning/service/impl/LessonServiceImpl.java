package jlearning.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.model.Grammar;
import jlearning.model.Lesson;
import jlearning.model.Listening;
import jlearning.model.Vocabulary;
import jlearning.model.Test;
import jlearning.service.LessonService;

public class LessonServiceImpl  extends BaseServiceImpl implements LessonService {

	private static final Logger logger = Logger.getLogger(LessonServiceImpl.class);
	
	@Override
	public Lesson findById(Serializable key) {
		try {
			Lesson lesson = getLessonDAO().findById(key);
			List<Vocabulary> listVocab = lesson.getVocabularies();
			List<Grammar> listGramr = lesson.getGrammars();
			List<Listening> listListening = lesson.getListenings();
			List<Test> test = lesson.getTests();
			listGramr.size();
			listListening.size();
			listVocab.size();
			test.size();
			return lesson;
			
			
		}catch(Exception e)
		{
			logger.error(e);
			throw e;
		}
	}

	@Override
	public Lesson saveOrUpdate(Lesson entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Lesson entity) {
		// TODO Auto-generated method stub
		return false;
	}

}
