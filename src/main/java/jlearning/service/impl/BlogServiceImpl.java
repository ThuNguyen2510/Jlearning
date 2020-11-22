package jlearning.service.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.model.Blog;
import jlearning.model.Blog.Type;
import jlearning.model.User;
import jlearning.service.BlogService;

public class BlogServiceImpl extends BaseServiceImpl implements BlogService {

	private static final Logger logger = Logger.getLogger(BlogServiceImpl.class);

	@Override
	public Blog findById(Serializable key) {
		try {

			return getBlogDAO().findById(key);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public Blog saveOrUpdate(Blog entity) {
		Blog b= getBlogDAO().findById(entity.getId());
		b.setUser(entity.getUser());
		b.setContent(entity.getContent());
		b.setComments(entity.getComments());
		b.setDes(entity.getDes());
		b.setImage(entity.getImage());
		b.setTitle(entity.getTitle());
		b.setType(entity.getType());
		return getBlogDAO().saveOrUpdate(b);
	}

	@Override
	public boolean delete(Blog entity) {
		for(int i=0;i<entity.getComments().size();i++) {
			getCommentDAO().delete(entity.getComments().get(i));
		}
		getBlogDAO().delete(entity);
		return true;
	}

	@Override
	public List<Blog> loadBlogs() {

		return getBlogDAO().loadBlogs();
	}

	@Override
	public List<Blog> loadBlogsByType(Type type) {

		return getBlogDAO().loadBlogsByType(type);
	}

	@Override
	public List<Blog> loadNewBlogs() {
		return getBlogDAO().loadNewBlogs();
	}

	@Override
	public List<Blog> search(String k) {
		try {

			return getBlogDAO().search(k);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}

	}

	@Override
	public List<Blog> searchByPaging(int type, String k, int page) {
		try {
			return getBlogDAO().searchWithPaging(type, k, page);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	@Override
	public void createBlog(Blog blog, int userId) {
		User user = getUserDAO().findById(userId);
		blog.setUser(user);
		getBlogDAO().saveOrUpdate(blog);
		
	}

}
