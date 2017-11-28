package com.example.demo;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

@Component
public class ObjectMapper {
	private Map<String, Object> map = new HashMap<>();
	
	public ObjectMapper() {
		super();
	}
	
	public <T> ObjectMapper(T entry) {
		toJson(entry);
	}
	
	public <T> String getJson(T entry) {
		toJson(entry);
		return getJson();
	}
	
	public String getJson() {
		return map.toString().toString().replaceAll("=", ":").
				replaceAll("\\[(.*?)\\]", "\\{($1)\\}").
				replace(", ", ",");
	}
	
	private <T> void toJson(T entry) {
		map.clear();
		Class<?> c = entry.getClass();
		Field[] fields = c.getDeclaredFields();
		String[] names = Arrays.stream(fields).map(Field::getName).toArray(String[]::new);
		Object[] args = getValue(entry, names);
		for(int i = 0; i < args.length; i++) {
			map.put("\"" + names[i] + "\"","\"" + args[i] + "\"");
		}
	}
	
	private <T> Object[] getValue(T entry, String[] names) {
		Class<?> c = entry.getClass();
		return Arrays.stream(names).map(name -> {
			try {
				return c.getMethod(getDataName(name, "get")).invoke(entry);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}).toArray();
	}
	
	private static String getDataName(String name, String prefix) {
		StringBuffer sb = new StringBuffer(name);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.insert(0, prefix).toString();
	}
	
	
	
	public <T> T getPojo(Class<T> c, String json) throws InstantiationException, IllegalAccessException {
		T entry = c.newInstance();
		toPojo(json);
		map.keySet().forEach(s -> {
			try {
				Class<?> t = getParamType(c, s);
				c.getMethod(getDataName(s, "set"), t).invoke(entry, transFor(t).apply(map.get(s).toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});
		return entry;
	}
	
	private void toPojo(String json) {
		map.clear();
		String[] split = json.replaceAll("\\{(.*?)\\}", "$1").
				replace("\"", "").split(",");
		Arrays.stream(split).forEach(s -> {
			String[] sp = s.split(":");
			map.put(sp[0], sp[1]);
		});
	}
	
	private<T> Class<?> getParamType(Class<T> c, String name){
		Field f = null;
		try {
			f = c.getDeclaredField(name);
			f.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return f.getType();
	}
	
	public Function<String, Object> transFor(Class<?> type) {
	    if(type.getTypeName().equals("byte")) {
	    	return Byte::parseByte;
	    } else if(type.getTypeName().equals("short")) {
	    	return Short::parseShort;
	    } else if(type.getTypeName().equals("int")) {
			return Integer::parseInt;
	    } else if(type.getTypeName().equals("long")) {
	    	return Long::parseLong;
		} else if(type.getTypeName().equals("double")) {
			return Double::parseDouble;
		} else if(type.getTypeName().equals("boolean")) {
			return Boolean::parseBoolean;
		} else if(type.getTypeName().equals("char")) {
			return s -> s.charAt(0);
		} else {
			return a -> a;
		}
	}
	
}
