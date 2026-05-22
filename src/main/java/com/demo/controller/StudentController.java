package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.demo.model.Student;
import com.demo.service.StudentService;

@Controller
public class StudentController {
	@Autowired
	private StudentService service;
	
	@PostMapping("/add")
	public String addStudent(String name,String email,String password)
	{
		Student s=new Student();
		s.setName(name);
		s.setEmail(email);
		s.setPassword(password);
	   service.addStudent(s);	
	   
	   
	   return "success";
	}

}
