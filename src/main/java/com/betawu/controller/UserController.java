package com.betawu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.betawu.vo.User;

@Controller
public class UserController {

	@RequestMapping(value="hello",method=RequestMethod.POST)
	public String hello() {
		System.err.println("----------------");
		return "hello";
	}
	
	@RequestMapping(value="login",method=RequestMethod.POST)
	@ResponseBody
	public User login(@RequestBody User user) {
		return user;
	}
	
}
