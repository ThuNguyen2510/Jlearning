package jlearning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import jlearning.bean.LessonInfo;
import jlearning.bean.VocabInfo;
import jlearning.dao.AlphabetDAO;
import jlearning.dao.AnswerDAO;
import jlearning.dao.BlogDAO;
import jlearning.dao.CommentDAO;
import jlearning.dao.CourseDAO;
import jlearning.dao.GrammarDAO;
import jlearning.dao.HistoryDAO;
import jlearning.dao.LessonDAO;
import jlearning.dao.ListeningDAO;
import jlearning.dao.QuestionDAO;
import jlearning.dao.ResultDAO;
import jlearning.dao.TestDAO;
import jlearning.dao.UserDAO;
import jlearning.dao.VocabularyDAO;
import jlearning.model.Course;
import jlearning.model.Lesson;
import jlearning.model.Vocabulary;

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

	@Autowired
	private TestDAO testDAO;

	@Autowired
	private ResultDAO resultDAO;

	@Autowired
	private QuestionDAO questionDAO;
	@Autowired
	private AnswerDAO answerDAO;

	public AnswerDAO getAnswerDAO() {
		return answerDAO;
	}

	public void setAnswerDAO(AnswerDAO answerDAO) {
		this.answerDAO = answerDAO;
	}

	@Autowired
	private HistoryDAO historyDAO;

	@Autowired
	private CommentDAO commentDAO;

	public TestDAO getTestDAO() {
		return testDAO;
	}

	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}

	public ResultDAO getResultDAO() {
		return resultDAO;
	}

	public void setResultDAO(ResultDAO resultDAO) {
		this.resultDAO = resultDAO;
	}

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

	public QuestionDAO getQuestionDAO() {
		return questionDAO;
	}

	public void setQuestionDAO(QuestionDAO questionDAO) {
		this.questionDAO = questionDAO;
	}

	public HistoryDAO getHistoryDAO() {
		return historyDAO;
	}

	public void setHistoryDAO(HistoryDAO historyDAO) {
		this.historyDAO = historyDAO;
	}

	public CommentDAO getCommentDAO() {
		return commentDAO;
	}

	public void setCommentDAO(CommentDAO commentDAO) {
		this.commentDAO = commentDAO;
	}

}
