package model;

public class FileInfo {
    
    private final String fileName;
    private final String fileMimeType;
    private final long fileSize;
    private final String fileExtension;
    
    
    public FileInfo(String fileName,String fileMimeType,long fileSize,String fileExtension){
        this.fileName=fileName;
        this.fileMimeType=fileMimeType;
        this.fileSize=fileSize;
        this.fileExtension=fileExtension;
    }
    
    
    public String getFileName() {
        return fileName;
    }
  
    public String getFileMimeType() {
        return fileMimeType;
    }
    public long getFileSize() {
        return fileSize;
    }
    public String getFileExtension() {
        return fileExtension;
    }
}
