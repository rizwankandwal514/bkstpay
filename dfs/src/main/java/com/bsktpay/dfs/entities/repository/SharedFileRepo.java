package com.bsktpay.dfs.entities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bsktpay.dfs.entities.SharedFile;

@Repository
public interface SharedFileRepo extends JpaRepository<SharedFile, Long>{

	
	@Query(value = "SELECT * from shared_file where file_name = ?1 and file_portion_number = ?2", nativeQuery = true)
 	SharedFile findByFileNameAndFilePortionNumber(String fileName, String PortionNumber);
}
