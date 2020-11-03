package jlearning.bean;

public class AnswerInfo {
	private int Id;
	
	private String content;

	public AnswerInfo(){

	}

	public AnswerInfo(String content) {
		this.setContent(content);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
