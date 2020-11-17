package jlearning.service;

import java.io.Serializable;

import jlearning.bean.LessonInfo;
import jlearning.bean.VocabInfo;
import jlearning.model.Lesson;

public interface BaseService<PK, T> {
	public T findById(Serializable key);

	public T saveOrUpdate(T entity);

	public boolean delete(T entity);
	
}