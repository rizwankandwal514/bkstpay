package com.bsktpay.dfs.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bsktpay.dfs.dto.FileMessageRequest;
import com.bsktpay.dfs.dto.ResponseDTO;
import com.bsktpay.dfs.serviceimp.Nodes;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/dfs")
public class DfsController {

	private static final Logger logger = LoggerFactory.getLogger(DfsController.class);

	@Autowired
	private Nodes nodes;

	@GetMapping("/retrieve-content/{fileName}")
	//@ApiOperation(value = "Retrieve file content", notes = "Provide an ID to look up specific content from the shared file")
	@Operation(summary = "Retrieve file content", description = "Provide a fileName to look up specific content")
	public ResponseEntity<ResponseDTO> retrieveFileContent(@PathVariable String fileName) {
		try {
			String content = nodes.getFileContent(fileName);
			logger.info("DFS : Content retrieved successfully");
			return ResponseEntity.ok(new ResponseDTO("Content retrieved successfully", HttpStatus.OK.value(), content));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO("Error Getting Content", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
		}
	}

	@PostMapping("/upload")
	@Operation(summary = "Upload file content", description = "upload file in different parts using different nodes")
	public ResponseEntity<ResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(new ResponseDTO("Cannot upload an empty file", HttpStatus.BAD_REQUEST.value(), null));
		}

		try {
			nodes.saveFile(file);
			logger.info("DFS : File uploaded successfully");
			return ResponseEntity.ok(new ResponseDTO("File uploaded successfully: " + file.getOriginalFilename(),
					HttpStatus.OK.value(), null));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(
					"Could not upload the file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
		}
	}

	@PostMapping("/update-file")
	@Operation(summary = "Upload file content", description = "Update the contnet of the file")
	public ResponseEntity<ResponseDTO> handleFileMessage(@RequestBody FileMessageRequest request) {
		try {
			Boolean updated = nodes.updateFile(request);
			if (updated) {
				logger.info("DFS : File updated successfully");
				return ResponseEntity.ok(new ResponseDTO("File Updated successfully", HttpStatus.OK.value(), null));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseDTO("No Record Found", HttpStatus.NOT_FOUND.value(), null));
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(
					"Error updating file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
		}
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDTO> handleException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(
				"An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
	}
}
