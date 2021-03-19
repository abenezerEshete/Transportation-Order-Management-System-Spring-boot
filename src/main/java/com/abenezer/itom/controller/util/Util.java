package com.abenezer.itom.controller.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class Util {


	/**
	 * response body builder method
	 * */
	static public Map<String, Object> buildResponse (Object customerOrders) {
		Map<String, Object> map = new HashMap<> ();
		map.put ("data", customerOrders);
		map.put ("hasErrors", false);
		return map;
	}
}
