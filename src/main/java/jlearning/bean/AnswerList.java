package jlearning.bean;

import java.util.List;

public class AnswerList {
	private List<AnswerInfo> answers;

	public AnswerList() {

	}
	public AnswerList(List<AnswerInfo> ans) {
		this.answers=ans;
	}
	public List<AnswerInfo> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerInfo> answers) {
		this.answers = answers;
	}

	

}
