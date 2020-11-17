package jlearning.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import jlearning.dao.GenericDAO;
import jlearning.dao.VocabularyDAO;
import jlearning.model.Vocabulary;

public class VocabularyDAOImpl extends GenericDAO<Integer, Vocabulary> implements VocabularyDAO {
	Logger logger = Logger.getLogger(VocabularyDAOImpl.class);

	public VocabularyDAOImpl() {
		super(Vocabulary.class);
	}

	public VocabularyDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public List<Vocabulary> list(int lessonId) {
		// TODO Auto-generated method stub
		return null;
	}

}
