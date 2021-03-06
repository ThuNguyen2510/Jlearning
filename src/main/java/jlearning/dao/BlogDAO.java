package jlearning.dao;

import java.util.List;

import jlearning.model.Blog;
import jlearning.model.Blog.Type;
public interface BlogDAO extends BaseDAO<Integer,Blog> {
	
	List<Blog> loadBlogs();
	List<Blog> loadBlogsByType(Type type);
	List<Blog> loadNewBlogs();
	List<Blog> search(String k);
	List<Blog> searchWithPaging(int type, String k, int page);
}
