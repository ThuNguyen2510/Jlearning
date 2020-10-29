package jlearning.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "tests")
public class Test {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "time")
	private int time;
	
	@Column(name = "questionQuantity")
	private int questionQuantity;
	
	public enum Type {
		LEVEL, LESSON,EXAM
	}

	@Column(columnDefinition = "enum('LEVEL','LESSON','EXAM')", nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type;
	
	@ManyToOne
	@JoinColumn(name="lesson_id")
	private Lesson lesson;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "test")
	private List<Question> questions;
	
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	@Column(name = "create_time", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date create_time;

	@Column(name = "update_time")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date update_time;

	@Column(name = "delete_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date delete_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getQuestionQuantity() {
		return questionQuantity;
	}

	public void setQuestionQuantity(int questionQuantity) {
		this.questionQuantity = questionQuantity;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public Date getDelete_time() {
		return delete_time;
	}

	public void setDelete_time(Date delete_time) {
		this.delete_time = delete_time;
	}
	
}
