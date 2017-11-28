package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.ObjectMapper;
import com.example.pojo.Person;
import com.example.pojo.Teacher;

@SpringBootApplication
public class OmApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(OmApplication.class, args);
		ObjectMapper om = new ObjectMapper();
		Person p = om.getPojo(Person.class, "{\"id\":\"3\",name:abc,age:50}");
		System.out.println(p);
		System.out.println(om.getJson(p));
		System.out.println(om.getJson(new Teacher(8, "w", 's')));
		Teacher t = om.getPojo(Teacher.class, "{id:6,name:王五,sex:女}");
		System.out.println(t.getSex());
	}
	
}
