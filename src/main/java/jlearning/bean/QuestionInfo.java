package jlearning.bean;

import java.util.List;

public class QuestionInfo {
	private int Id;
	private String content;
	private List<AnswerInfo_> ansList;
	private int part;
	private int level;
	public QuestionInfo() {
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<AnswerInfo_> getAnsList() {
		return ansList;
	}

	public void setAnsList(List<AnswerInfo_> ansList) {
		this.ansList = ansList;
	}

	public int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
