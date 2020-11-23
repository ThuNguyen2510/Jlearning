package jlearning.service.impl;

import java.io.Serializable;

import org.apache.log4j.Logger;

import jlearning.model.Blog;
import jlearning.model.Comment;
import jlearning.model.User;
import jlearning.bean.CommentInfo;
import jlearning.service.CommentService;

public class CommentServiceImpl extends BaseServiceImpl implements CommentService {

	private static final Logger logger = Logger.getLogger(CommentServiceImpl.class);

	@Override
	public Comment findById(Serializable key) {
		try {
			return getCommentDAO().findById(key);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Comment saveOrUpdate(Comment entity) {
		
		return getCommentDAO().saveOrUpdate(entity);
	}

	@Override
	public boolean delete(Comment entity) {

		try {
			getCommentDAO().delete(entity);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Comment createComment(CommentInfo cmt) {
		Comment comment = new Comment();
		User u = getUserDAO().findById(cmt.getUserId());
		Blog b = getBlogDAO().findById(cmt.getBlogId());
		comment.setBlog(b);
		comment.setUser(u);
		comment.setContent(cmt.getContent());
		return getCommentDAO().saveOrUpdate(comment);
	}

}
