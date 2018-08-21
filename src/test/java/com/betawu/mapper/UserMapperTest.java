package com.betawu.mapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betawu.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-dao.xml","classpath:spring-service.xml"})
public class UserMapperTest {

	@Autowired
	private UserMapper userMapper;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() {
		int i = userMapper.selectCount();
		System.out.println(i);
		
		User user = userMapper.selectByPrimaryKey(1);
		System.out.println(user);
	}

}
