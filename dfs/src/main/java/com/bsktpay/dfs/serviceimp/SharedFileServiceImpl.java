package com.bsktpay.dfs.serviceimp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsktpay.dfs.entities.SharedFile;
import com.bsktpay.dfs.entities.repository.SharedFileRepo;
import com.bsktpay.dfs.service.SharedFileService;


@Service
public class SharedFileServiceImpl implements SharedFileService{
	
	@Autowired
	SharedFileRepo sharedFileRepo;

	@Override
	public List<SharedFile> findAll() {
		return sharedFileRepo.findAll();
	}

	@Override
	public SharedFile findByFileNameAndFilePortionNumber(String fileName, String PortionNumber) {
		// TODO Auto-generated method stub
		return sharedFileRepo.findByFileNameAndFilePortionNumber(fileName, PortionNumber);
	}

	@Override
	public void save(SharedFile sharedFile) {
		sharedFileRepo.save(sharedFile);
		
	}

}
