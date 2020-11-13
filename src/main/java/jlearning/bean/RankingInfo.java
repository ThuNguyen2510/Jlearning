package jlearning.bean;

import java.util.Date;

public class RankingInfo {
	private String userName;
	private int score;
	private Date time;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public RankingInfo() {
		
	}
	
	

}
