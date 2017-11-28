package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.ObjectMapper;
import com.example.pojo.Person;
import com.example.pojo.Teacher;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OmApplicationTests {

	@Test
	public void contextLoads() throws Exception{
		ObjectMapper om = new ObjectMapper();
		Person p = om.getPojo(Person.class, "{\"id\":\"3\",name:abc,age:50}");
		System.out.println(p);
		System.out.println(om.getJson(p));
		System.out.println(om.getJson(new Teacher(8, "w", 's')));
		Teacher t = om.getPojo(Teacher.class, "{id:6,name:王五,sex:女}");
		System.out.println(t.getSex());
	}

}
