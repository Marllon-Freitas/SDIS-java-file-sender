package javaDataSendAndReceive;

public class MyFile {

	private int id;
	private String fileName;
	private byte[] fileData;
	private String fileExtension;
	
	
	public MyFile(int id, String fileName, byte[] fileData, String fileExtension) {
		this.id = id;
		this.fileName = fileName;
		this.fileData = fileData;
		this.fileExtension = fileExtension;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	

}
