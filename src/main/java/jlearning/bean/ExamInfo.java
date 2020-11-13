package jlearning.bean;

public class ExamInfo {
	private int Id;
	private String name;
	private int score;
	private int time;
	private int partCount;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getPartCount() {
		return partCount;
	}
	public void setPartCount(int partCount) {
		this.partCount = partCount;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}

}
