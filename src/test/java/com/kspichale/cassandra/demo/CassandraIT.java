package com.kspichale.cassandra.demo;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.kspichale.cassandra.demo.DaoHelper;
import com.kspichale.cassandra.demo.User;
import com.kspichale.cassandra.demo.UserDao;

public class CassandraIT {

	@Test
	public void saveAndLoadUserTest() {

		final User user = TestUtil.getRandomUser();
		final UserDao dao = new UserDao(new DaoHelper());
		dao.save(user);
		final User loadedUser = dao.getById(user.getId());
		assertThat(loadedUser).isEqualTo(user);
	}

	@Test
	public void deleteUserTest() {

		final User user = TestUtil.getRandomUser();
		final UserDao dao = new UserDao(new DaoHelper());
		dao.save(user);
		dao.deleteUserById(user.getId());
		final User loadedUser = dao.getById(user.getId());
		assertThat(loadedUser).isNull();
	}

	@Test
	public void findByCompanyTest() {
		final User user = TestUtil.getRandomUser();
		final UserDao dao = new UserDao(new DaoHelper());
		dao.save(user);
		final List<User> findByCompany = dao.findByCompany(user.getCompany());
		assertThat(findByCompany).containsExactly(user);
	}

	@Test
	public void findAllUsers() {
		final UserDao dao = new UserDao(new DaoHelper());
		final List<User> findByCompanyBefore = dao.findAllUsers();
		final User user = TestUtil.getRandomUser();
		dao.save(user);
		final List<User> findByCompanyAfter = dao.findAllUsers();
		assertThat(findByCompanyAfter).hasSize(findByCompanyBefore.size() + 1);
	}

}
