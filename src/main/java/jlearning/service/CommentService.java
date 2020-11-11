package jlearning.service;

import jlearning.bean.CommentInfo;
import jlearning.model.Comment;

public interface CommentService extends BaseService<Integer, Comment>{
	Comment createComment(CommentInfo cmt);
}
