
package jlearning.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import jlearning.dao.GenericDAO;
import jlearning.dao.UserDAO;
import jlearning.dao.impl.UserDAOImpl;
import jlearning.model.User;
import jlearning.model.User.Role;

public class UserDAOImpl extends GenericDAO<Integer, User> implements UserDAO {
	Logger logger = Logger.getLogger(UserDAOImpl.class);

	public UserDAOImpl() {
		super(User.class);
	}

	public UserDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> loadUsers() {
		return getSession().createQuery("from User").getResultList();
	}

	@Override
	public User findByEmail(String email) {
		return (User) getSession().createQuery("from User where email=: email").setParameter("email", email)
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkEmailExist(String email) {
		User user = (User) getSession().createQuery("from User where email=: email").setParameter("email", email)
				.uniqueResult();
		if (user != null)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> loadUsers(Role role) {
		return getSession().createQuery("from User where role=: role").setParameter("role", role).getResultList();
	}

}