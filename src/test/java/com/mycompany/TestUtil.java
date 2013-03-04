package com.mycompany;

import org.apache.commons.lang.RandomStringUtils;

import com.example.User;

public class TestUtil {

	public static User getRandomUser() {
		return new User().withFirstname(getRandomString())
				.withLastname(getRandomString()).withCompany(getRandomString());
	}

	private static String getRandomString() {
		return RandomStringUtils.random(10, true, false);
	}

}
