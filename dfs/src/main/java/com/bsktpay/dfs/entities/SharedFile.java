package com.bsktpay.dfs.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Column;


@Entity
@Table(name = "shared_file")
public class SharedFile {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	 	@Column(name = "id")
	    private Long id;
	 	@Column(name = "file_name")
	    private String fileName;
	 	
	 	@Column(name = "file_portion_number")
	    private String filePortionNumber;
	 	
	    @Lob
	    @Column(name = "file_content")
	    private byte[] fileContent;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFilePortionNumber() {
			return filePortionNumber;
		}

		public void setFilePortionNumber(String filePortionNumber) {
			this.filePortionNumber = filePortionNumber;
		}

		public byte[] getFileContent() {
			return fileContent;
		}

		public void setFileContent(byte[] fileContent) {
			this.fileContent = fileContent;
		}
	    
	    
	    
	    

}
