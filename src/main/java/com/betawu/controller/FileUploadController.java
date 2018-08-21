package com.betawu.controller;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {
	
	@RequestMapping(value="upload",method=RequestMethod.POST)
	public String upload(MultipartFile file) throws Exception {
		FileUtils.writeByteArrayToFile(new File("C:\\Users\\Beta\\test.jpg"), file.getBytes());
		return "hello";
	}
}
