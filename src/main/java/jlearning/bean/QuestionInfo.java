package jlearning.bean;

import java.util.List;

public class QuestionInfo {
	private int Id;
	private String content;
	private List<AnswerInfo> ansList;

	public QuestionInfo() {
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<AnswerInfo> getAnsList() {
		return ansList;
	}

	public void setAnsList(List<AnswerInfo> ansList) {
		this.ansList = ansList;
	}

}
