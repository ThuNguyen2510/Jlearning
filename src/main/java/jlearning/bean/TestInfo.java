package jlearning.bean;

import java.util.List;

public class TestInfo {
	private String name;
	private String type;
	private List<QuestionInfo> questions;
	private int level;
	private int time;
	private int lessonId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<QuestionInfo> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionInfo> questions) {
		this.questions = questions;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getLessonId() {
		return lessonId;
	}
	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}
	

}
