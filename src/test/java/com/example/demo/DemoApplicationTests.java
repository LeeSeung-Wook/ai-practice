package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class DemoApplicationTests {

	@Test
	public void bcrypt_test() {
		BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
		String encPassword = enc.encode("1234");
		System.out.println(encPassword);
	}

}
