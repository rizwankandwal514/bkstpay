package com.bsktpay.dfs.serviceimp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bsktpay.dfs.dto.FileMessageRequest;
import com.bsktpay.dfs.entities.SharedFile;
import com.bsktpay.dfs.service.SharedFileService;

@Service
public class Nodes {

	@Autowired
	SharedFileService sharedFileService;
	
	private static final Logger logger = LoggerFactory.getLogger(Nodes.class);

	Map<String, byte[]> filePrtions = new ConcurrentHashMap<>();

	@Async("taskExecutor")
	public CompletableFuture<Void> node1(String fileName, String filePortion) {
		logger.debug("Entering node1 with fileName: {} and filePortion: {}", fileName, filePortion);
        try {
            SharedFile sharedFile = sharedFileService.findByFileNameAndFilePortionNumber(fileName, "part_1");
            filePrtions.put("part_1", sharedFile.getFileContent());
        } catch (Exception e) {
            logger.error("Error in node1: {}", e.getMessage(), e);
            throw e;
        }
        logger.debug("Exiting node1");
        return CompletableFuture.completedFuture(null);
	}

	@Async("taskExecutor")
	public CompletableFuture<Void> node2(String fileName, String filePortion) {
		logger.debug("Entering node2 with fileName: {} and filePortion: {}", fileName, filePortion);
        try {
            SharedFile sharedFile = sharedFileService.findByFileNameAndFilePortionNumber(fileName, "part_2");
            filePrtions.put("part_2", sharedFile.getFileContent());
        } catch (Exception e) {
            logger.error("Error in node2: {}", e.getMessage(), e);
            throw e;
        }
        logger.debug("Exiting node2");
        return CompletableFuture.completedFuture(null);

	}

	@Async("taskExecutor")
	public CompletableFuture<Void> node3(String fileName, String filePortion) {
		logger.debug("Entering node3 with fileName: {} and filePortion: {}", fileName, filePortion);
        try {
            SharedFile sharedFile = sharedFileService.findByFileNameAndFilePortionNumber(fileName, "part_3");
            filePrtions.put("part_3", sharedFile.getFileContent());
        } catch (Exception e) {
            logger.error("Error in node3: {}", e.getMessage(), e);
            throw e;
        }
        logger.debug("Exiting node1");
        return CompletableFuture.completedFuture(null);

	}

	@Async("taskExecutor")
	public CompletableFuture<Void> node4(String fileName, String filePortion) {
		logger.debug("Entering node4 with fileName: {} and filePortion: {}", fileName, filePortion);
		SharedFile sharedFile = sharedFileService.findByFileNameAndFilePortionNumber(fileName, "part_4");
		filePrtions.put("part_4", sharedFile.getFileContent());
		return CompletableFuture.completedFuture(null);
	}

	@Async("taskExecutor")
	public CompletableFuture<Void> node1(byte[] array, String fileName, String filePortion) {
		SharedFile sharedFile = new SharedFile();
		sharedFile.setFileName(fileName);
		sharedFile.setFilePortionNumber(filePortion);
		sharedFile.setFileContent(array);
		sharedFileService.save(sharedFile);
		return CompletableFuture.completedFuture(null);
	}

	@Async("taskExecutor")
	public CompletableFuture<Void> node2(byte[] array, String fileName, String filePortion) {
		SharedFile sharedFile = new SharedFile();
		sharedFile.setFileName(fileName);
		sharedFile.setFilePortionNumber(filePortion);
		sharedFile.setFileContent(array);
		sharedFileService.save(sharedFile);
		return CompletableFuture.completedFuture(null);
	}

	@Async("taskExecutor")
	public CompletableFuture<Void> node3(byte[] array, String fileName, String filePortion) {
		SharedFile sharedFile = new SharedFile();
		sharedFile.setFileName(fileName);
		sharedFile.setFilePortionNumber(filePortion);
		sharedFile.setFileContent(array);
		sharedFileService.save(sharedFile);
		return CompletableFuture.completedFuture(null);
	}

	@Async("taskExecutor")
	public CompletableFuture<Void> node4(byte[] array, String fileName, String filePortion) {
		SharedFile sharedFile = new SharedFile();
		sharedFile.setFileName(fileName);
		sharedFile.setFilePortionNumber(filePortion);
		sharedFile.setFileContent(array);
		sharedFileService.save(sharedFile);
		return CompletableFuture.completedFuture(null);
	}

	public String getFileContent(String fileName) {
		logger.info("Retrieving file content for fileName: {}", fileName);
        CompletableFuture<Void> part1 = node1(fileName, "part_1");
        CompletableFuture<Void> part2 = node2(fileName, "part_2");
        CompletableFuture<Void> part3 = node3(fileName, "part_3");
        CompletableFuture<Void> part4 = node4(fileName, "part_4");
        CompletableFuture.allOf(part1, part2, part3, part4).join();
        logger.debug("All parts retrieved, merging content");
        String content = mergeByteArrays(filePrtions.get("part_1"), filePrtions.get("part_2"), filePrtions.get("part_3"), filePrtions.get("part_4"));
        logger.info("Retrieved content for fileName: {}", fileName);
        return content;
	}

	public String saveFile(MultipartFile file) throws IOException {
		logger.info("Saving file: {}", file.getOriginalFilename());
		Map<String, byte[]> fileParts = splitFile(file, 4);
		node1(fileParts.get("part_1"), file.getOriginalFilename(), "part_1");
		node2(fileParts.get("part_2"), file.getOriginalFilename(), "part_2");
		node3(fileParts.get("part_3"), file.getOriginalFilename(), "part_3");
		node4(fileParts.get("part_4"), file.getOriginalFilename(), "part_4");
		logger.info("File saved: {}", file.getOriginalFilename());
		return "";
	}

	public Boolean updateFile(FileMessageRequest data) throws IOException {
		logger.info("DFS : Updating File");
		SharedFile sharedFile = sharedFileService.findByFileNameAndFilePortionNumber(data.getFileName(), "part_4");
		if (sharedFile != null) {
			byte[] content = sharedFile.getFileContent();
			byte[] newMessage = mergeByteArrays(content, data.getMessage().getBytes());
			sharedFile.setFileContent(newMessage);
			sharedFileService.save(sharedFile);
			return true;
		} else {
			return false;
		}
	}

	public Map<String, byte[]> splitFile(MultipartFile multipartFile, int numberOfParts) throws IOException {
		ConcurrentHashMap<String, byte[]> partsMap = new ConcurrentHashMap<>();
		long totalFileSize = multipartFile.getSize();
		long partSize = totalFileSize / numberOfParts;
		long remainingBytes = totalFileSize - (partSize * numberOfParts);

		try (InputStream inputStream = multipartFile.getInputStream()) {
			for (int partCounter = 1; partCounter <= numberOfParts; partCounter++) {
				long currentPartSize = (partCounter == numberOfParts && remainingBytes > 0) ? partSize + remainingBytes
						: partSize;

				byte[] buffer = new byte[(int) currentPartSize];
				int bytesRead = inputStream.read(buffer);

				while (bytesRead < currentPartSize) {
					bytesRead += inputStream.read(buffer, bytesRead, (int) (currentPartSize - bytesRead));
				}

				partsMap.put("part_" + partCounter, buffer);
			}
		}
		return partsMap;
	}

	// Method to merge byte arrays
	public String mergeByteArrays(byte[] array1, byte[] array2, byte[] array3, byte[] array4) {
		byte[] mergedArray = new byte[array1.length + array2.length + array3.length + array4.length];
		System.arraycopy(array1, 0, mergedArray, 0, array1.length);
		System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);
		System.arraycopy(array3, 0, mergedArray, array1.length + array2.length, array3.length);
		System.arraycopy(array4, 0, mergedArray, array1.length + array2.length + array3.length, array4.length);
		return new String(mergedArray, StandardCharsets.UTF_8);
	}

	// Method to merge byte arrays
	public byte[] mergeByteArrays(byte[] array1, byte[] array2) {
		byte[] mergedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, mergedArray, 0, array1.length);
		System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);
		return mergedArray;
	}
}