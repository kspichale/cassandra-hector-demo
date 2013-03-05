package com.kspichale.cassandra.demo;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.exceptions.HectorException;

public class UserDao {

	private static final String LASTNAME = "lastname";
	private static final String FIRSTNAME = "firstname";
	private static final String COMPANY = "company";

	private final String COLUMN_FAMILY = "user";
	private final StringSerializer se = StringSerializer.get();
	private final DaoHelper helper;
	private ColumnFamilyTemplate<String, String> template = null;

	public UserDao(DaoHelper helper) {
		this.helper = helper;
	}

	ColumnFamilyTemplate<String, String> getTemplate() {

		if (template == null) {
			final Keyspace ksp = helper.getKeyspace();

			template = new ThriftColumnFamilyTemplate<String, String>(ksp,
					COLUMN_FAMILY, se, se);
		}

		return template;
	}

	public void save(final User user) {

		final ColumnFamilyTemplate<String, String> template = getTemplate();

		if (user.getId() == null) {
			final String id = helper.createId();
			user.setId(id);
		}

		final ColumnFamilyUpdater<String, String> updater = template
				.createUpdater(user.getId());

		updater.setString(FIRSTNAME, user.getFirstname());
		updater.setString(LASTNAME, user.getLastname());
		updater.setString(COMPANY, user.getCompany());

		try {
			template.update(updater);
		} catch (HectorException e) {
			throw new RuntimeException(e);
		}
	}

	public User getById(final String id) {
		try {
			final ColumnFamilyTemplate<String, String> template = getTemplate();
			final ColumnFamilyResult<String, String> res = template
					.queryColumns(id);
			if (res.hasResults()) {
				final User user = new User();
				user.setId(id);
				user.setFirstname(res.getString(FIRSTNAME));
				user.setLastname(res.getString(LASTNAME));
				user.setCompany(res.getString(COMPANY));
				return user;
			} else {
				return null;
			}

		} catch (HectorException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteUserById(final String id) {
		try {
			template.deleteRow(id);
		} catch (HectorException e) {
			throw new RuntimeException(e);
		}
	}

	public List<User> findByCompany(final String company) {

		final Keyspace ksp = helper.getKeyspace();

		final CqlQuery<String, String, String> cqlQuery = new CqlQuery<String, String, String>(
				ksp, se, se, se);

		cqlQuery.setQuery("SELECT firstname, lastname, company FROM user WHERE company = '"
				+ company + "'");

		final List<Row<String, String, String>> rows = cqlQuery.execute().get()
				.getList();

		final List<User> users = createUserList(rows);
		return users;
	}

	public List<User> findAllUsers() {
		final Keyspace ksp = helper.getKeyspace();

		final CqlQuery<String, String, String> cqlQuery = new CqlQuery<String, String, String>(
				ksp, se, se, se);

		cqlQuery.setQuery("SELECT firstname, lastname, company FROM user");

		final List<Row<String, String, String>> rows = cqlQuery.execute().get()
				.getList();

		return createUserList(rows);
	}

	private List<User> createUserList(
			final List<Row<String, String, String>> list) {

		final List<User> users = new ArrayList<User>();

		for (final Row<String, String, String> row : list) {

			final String firstname = row.getColumnSlice()
					.getColumnByName(FIRSTNAME).getValue();

			final String lastname = row.getColumnSlice()
					.getColumnByName(LASTNAME).getValue();

			final String company = row.getColumnSlice()
					.getColumnByName(COMPANY).getValue();

			final User user = new User().withId(row.getKey())
					.withCompany(company).withFirstname(firstname)
					.withLastname(lastname);

			users.add(user);
		}
		return users;
	}
}
