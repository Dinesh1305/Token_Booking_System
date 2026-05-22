package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Student;
import com.demo.repo.StudentRepo;
@Service
public class StudentService {
	@Autowired
	private StudentRepo studentrepo;
	
	public void addStudent(Student student)
	{
		studentrepo.save(student);
	}
	
	
	
	
}
