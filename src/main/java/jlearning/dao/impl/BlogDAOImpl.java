package jlearning.dao.impl;

import java.util.ArrayList;
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
	public List<Blog> searchWithPaging(int t, String k, int page) {

		List<Blog> list = new ArrayList<Blog>();
		if (t != 0 && k == "") {
			Type type = null;
			if(t==1) type=Type.culture;
			if(t==2) type=Type.experience;
			if(t==3) type=Type.general;
			list = getSession().createQuery("from Blog where type=: type").setParameter("type", type)
					.setFirstResult((page - 1) * 6).setMaxResults(6).getResultList();
		}

		if (k != "") {
			list = getSession().createQuery("from Blog where title LIKE :k").setParameter("k", "%" + k + "%")
					.setFirstResult((page - 1) * 6).setMaxResults(6).getResultList();
		}
		return list;

	}

}
