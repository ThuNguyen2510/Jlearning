package jlearning.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import jlearning.dao.BlogDAO;
import jlearning.dao.GenericDAO;
import jlearning.model.Blog;
import jlearning.model.Blog.Type;

public class BlogDAOImpl extends GenericDAO<Integer, Blog> implements BlogDAO {
	Logger logger = Logger.getLogger(BlogDAOImpl.class);

	public BlogDAOImpl() {
		super(Blog.class);
	}

	public BlogDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Blog> loadBlogs() {
		return getSession().createQuery("from Blog").getResultList();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Blog> loadBlogsByType(Blog blog) {
		return getSession().createQuery("from Blog where type=: type").setParameter("type", blog.getType()).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Blog> loadNewBlogs() {
		return getSession().createQuery("from Blog b ORDER BY b.create_time DESC ").getResultList();
	}


}
