package com.wiysoft.footprint.backend.mvc.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wiysoft.footprint.backend.common.PropertiesHandler;

@RequestMapping("image")
@Controller
public class ImageController {

	private static final Logger logger = Logger.getLogger(ImageController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<byte[]> view(@RequestParam("id") String imageId) {
		File absImagePath = new File(PropertiesHandler.getAssetsRoot() + imageId);
		logger.debug("Image requested: " + absImagePath.getAbsolutePath());

		if (!absImagePath.exists()) {
			return new ResponseEntity<byte[]>(new byte[0], HttpStatus.NOT_FOUND);
		}

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);

		try {
			InputStream in = new BufferedInputStream(new FileInputStream(absImagePath));
			return new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.OK);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<byte[]>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String[] upload(@RequestParam("files") MultipartFile[] files) {
		String[] uploadedAssetsURL = new String[0];
		if (files != null) {
			uploadedAssetsURL = new String[files.length];
			for (int i = 0; i < files.length; ++i) {
				MultipartFile file = files[i];
				if (!file.isEmpty()) {
					try {
						String fileName = file.getOriginalFilename();
						byte[] bytes = file.getBytes();
						BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(
								PropertiesHandler.getAssetsRoot() + fileName)));
						buffStream.write(bytes);
						buffStream.close();

						uploadedAssetsURL[i] = fileName;
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}

		return uploadedAssetsURL;
	}

}
