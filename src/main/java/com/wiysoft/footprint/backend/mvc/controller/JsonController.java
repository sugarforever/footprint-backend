package com.wiysoft.footprint.backend.mvc.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wiysoft.footprint.backend.model.Footprint;
import com.wiysoft.footprint.backend.mvc.model.Status;
import com.wiysoft.footprint.backend.repositories.FootprintRepository;

@RequestMapping("/footprint")
@RestController
public class JsonController {

	private static final Logger logger = Logger.getLogger(JsonController.class);

	@Autowired
	private FootprintRepository footprintRepository;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAll")
	public @ResponseBody List getAllFootprints() {
		return footprintRepository.findAll();
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Status addFootprint(@RequestBody Footprint footprint) {
		logger.debug(footprint);
		footprintRepository.save(footprint);
		return new Status(0, "");
	}
}
