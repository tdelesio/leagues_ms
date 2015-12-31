package com.makeurpicks.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makeurpicks.AdminApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AdminApplication.class)
@IntegrationTest
public class GameRandonizorTest {

	@Autowired 
	private GameRandonizor gameRandomizer;
	@Test
	public void test() {

		gameRandomizer.createRandomLeague(17);
	}

}
