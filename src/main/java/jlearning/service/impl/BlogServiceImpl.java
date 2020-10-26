package jlearning.service.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.model.Blog;
import jlearning.model.Blog.Type;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Blog entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Blog> loadBlogs() {

		return getBlogDAO().loadBlogs();
	}

	@Override
	public List<Blog> loadBlogsByType(Blog blog) {

		List<Blog> list = getBlogDAO().loadBlogsByType(blog);
		for(Blog a: list){
			if(a.getId()==blog.getId()) {
				list.remove(a);
				break;
			}
				
		}
		return list;
	}

	@Override
	public List<Blog> loadNewBlogs() {
		return getBlogDAO().loadNewBlogs();
	}

	@Override
	public List<Blog> loadPopularBlogs() {
		// TODO Auto-generated method stub
		return null;
	}

}
