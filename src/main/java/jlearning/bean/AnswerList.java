package jlearning.bean;

import java.util.List;

public class AnswerList {
	private List<AnswerInfo> answers;
	
	public List<AnswerInfo> getAnswers() {
		return answers;
	}
	public void setAnswers(List<AnswerInfo> answers) {
		this.answers = answers;
	}
	public void addAnswer(AnswerInfo ans) {
		this.answers.add(ans);
	}
	
	

}
