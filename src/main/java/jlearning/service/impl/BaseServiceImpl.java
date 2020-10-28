package jlearning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import jlearning.dao.AlphabetDAO;
import jlearning.dao.BlogDAO;
import jlearning.dao.CourseDAO;
import jlearning.dao.GrammarDAO;
import jlearning.dao.LessonDAO;
import jlearning.dao.ListeningDAO;
import jlearning.dao.UserDAO;
import jlearning.dao.VocabularyDAO;

public class BaseServiceImpl {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private BlogDAO blogDAO;

	@Autowired
	private CourseDAO courseDAO;
	
	@Autowired
	private LessonDAO lessonDAO;
	
	@Autowired
	private AlphabetDAO alphabetDAO;
	
	@Autowired
	private VocabularyDAO vocabularyDAO;
	
	@Autowired
	private GrammarDAO grammarDAO;
	
	@Autowired
	private ListeningDAO listeningDAO;
	
	public AlphabetDAO getAlphabetDAO() {
		return alphabetDAO;
	}

	public void setAlphabetDAO(AlphabetDAO alphabetDAO) {
		this.alphabetDAO = alphabetDAO;
	}

	public VocabularyDAO getVocabularyDAO() {
		return vocabularyDAO;
	}

	public void setVocabularyDAO(VocabularyDAO vocabularyDAO) {
		this.vocabularyDAO = vocabularyDAO;
	}

	public GrammarDAO getGrammarDAO() {
		return grammarDAO;
	}

	public void setGrammarDAO(GrammarDAO grammarDAO) {
		this.grammarDAO = grammarDAO;
	}

	public ListeningDAO getListeningDAO() {
		return listeningDAO;
	}

	public void setListeningDAO(ListeningDAO listeningDAO) {
		this.listeningDAO = listeningDAO;
	}

	public CourseDAO getCourseDAO() {
		return courseDAO;
	}

	public void setCourseDAO(CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
	}

	public BlogDAO getBlogDAO() {
		return blogDAO;
	}

	public void setBlogDAO(BlogDAO blogDAO) {
		this.blogDAO = blogDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public LessonDAO getLessonDAO() {
		return lessonDAO;
	}

	public void setLessonDAO(LessonDAO lessonDAO) {
		this.lessonDAO = lessonDAO;
	}
}
