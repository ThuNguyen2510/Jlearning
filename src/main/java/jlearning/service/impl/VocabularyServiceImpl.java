package jlearning.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.model.Vocabulary;
import jlearning.service.VocabularyService;

public class VocabularyServiceImpl extends BaseServiceImpl implements VocabularyService {

	private static final Logger logger = Logger.getLogger(VocabularyServiceImpl.class);

	@Override
	public Vocabulary findById(Serializable key) {

		return getVocabularyDAO().findById(key);
	}

	@Override
	public Vocabulary saveOrUpdate(Vocabulary entity) {
		
		return getVocabularyDAO().saveOrUpdate(entity);
	}

	@Override
	public boolean delete(Vocabulary entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Vocabulary> listVocab(int lessonId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vocabulary find(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
