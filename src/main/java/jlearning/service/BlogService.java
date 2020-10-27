package jlearning.service;

import java.util.List;

import jlearning.model.Blog;
import jlearning.model.Blog.Type;

public interface BlogService extends BaseService<Integer, Blog> {
	List<Blog> loadBlogs();

	List<Blog> loadBlogsByType(Type type);
	
	List<Blog> loadNewBlogs();
	
	List<Blog> search(String k);
	
	List<Blog> searchByPaging(int type,String k, int page);

}
