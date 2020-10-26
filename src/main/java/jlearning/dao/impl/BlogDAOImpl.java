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
	public List<Blog> loadBlogsByType(Type type) {
		return getSession().createQuery("from Blog where type=: type").setParameter("type", type).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Blog> loadNewBlogs() {
		return getSession().createQuery("from Blog b ORDER BY b.create_time DESC ").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Blog> search(String k) {
		return getSession().createQuery("from Blog where title LIKE :k").setParameter("k", "%" + k + "%")
				.getResultList();
	}

	@Override
	public List<Blog> searchWithPaging(int type, String k) {
		/*
		 * if(type!=0 && k ==""){ if(type==1) {
		 * 
		 * } return null;
		 * 
		 * }else(k!-""){ return
		 * getSession().createQuery("from Blog where title LIKE :k").setParameter("k",
		 * "%" + k + "%")..setFirstResult((page - 1)*6) .setMaxResults(6)
		 * .getResultList(); }
		 */
		return null;
		
	}

}
