package com.bsktpay.dfs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bsktpay.dfs.entities.SharedFile;


public interface SharedFileService {
	public List<SharedFile> findAll();
	SharedFile findByFileNameAndFilePortionNumber(String fileName, String PortionNumber);
	public void save(SharedFile sharedFile);
}
