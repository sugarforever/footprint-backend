package com.wiysoft.footprint.backend.mvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wiysoft.footprint.backend.repositories.FootprintRepository;

@Controller
public class JsonController {

	@Autowired
	private FootprintRepository footprintRepository;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/emp/{name}")
	public @ResponseBody
	List getAllFootprints(@PathVariable String name) {
		return footprintRepository.findAll();
	}

}
