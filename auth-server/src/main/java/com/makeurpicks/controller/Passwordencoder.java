package com.makeurpicks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/passwordutil")
public class Passwordencoder {
	

		@Autowired
		private PasswordEncoder passwordEncoder;
		
		
		@RequestMapping(method=RequestMethod.GET, value="/encode/{value}")
		public @ResponseBody String encode() {
			
			return passwordEncoder.encode("dinesh");
			
		}
		
		@RequestMapping(method=RequestMethod.GET, value="/decode/{value}")
		public @ResponseBody String decode(@PathVariable(name="value")String value) {
			//need to change
			return passwordEncoder.encode(value);
		}
		
}
