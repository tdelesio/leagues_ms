package com.makeurpicks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PicksApplication.class)
@WebAppConfiguration
public class PicksApplicationTests {

	@Test
	public void contextLoads() {
		assertTrue(true);
	}

}
