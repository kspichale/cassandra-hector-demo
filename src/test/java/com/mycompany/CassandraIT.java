package com.mycompany;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.example.DaoHelper;
import com.example.User;
import com.example.UserDao;

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
		for (int i = 0; i < 10; i++) {
			final User user = TestUtil.getRandomUser();
			dao.save(user);
		}
		final List<User> findByCompany = dao.findAllUsers();
		assertThat(findByCompany).hasSize(10);
	}

}
