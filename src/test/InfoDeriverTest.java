package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import model.FileInfo;

import org.junit.Before;
import org.junit.Test;

import service.InfoDeriver;

public class InfoDeriverTest {

    InfoDeriver infoDeriver;

    File configFileDirectory;

    @Before
    public void init(){
        infoDeriver= new InfoDeriver();
        configFileDirectory = new File(new File("").getAbsolutePath()+"/config_dir");
    }

    @Test
    public void testFileInfoForGivenFileDirectory(){
        List<FileInfo> retrieveInfoForFilesInDirectory = infoDeriver.retrieveInfoForFilesInDirectory(configFileDirectory);
        assertTrue(!retrieveInfoForFilesInDirectory.isEmpty());
        assertEquals(10,retrieveInfoForFilesInDirectory.size());
    }

    @Test
    public void testFileInfoForGivenCSVFile(){
        File singleCSVFile= new File(configFileDirectory.getAbsolutePath()+"/TestFile1.csv");
        FileInfo fileInformation = infoDeriver.getInformationForIndividualFile(singleCSVFile);
        assertTrue(singleCSVFile.exists());
        assertEquals("TestFile1.csv", fileInformation.getFileName());
        assertEquals("csv",fileInformation.getFileExtension());
        assertEquals("application/vnd.ms-excel",fileInformation.getFileMimeType());
        assertEquals(75L,fileInformation.getFileSize());
    }

    @Test
    public void testFileInfoForGivenExcelFile(){
        File singleCSVFile= new File(configFileDirectory.getAbsolutePath()+"/TestFile2.xlsx");
        FileInfo fileInformation = infoDeriver.getInformationForIndividualFile(singleCSVFile);
        assertTrue(singleCSVFile.exists());
        assertEquals("TestFile2.xlsx", fileInformation.getFileName());
        assertEquals("xlsx",fileInformation.getFileExtension());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",fileInformation.getFileMimeType());
        assertEquals(104L,fileInformation.getFileSize());
    }

    @Test
    public void testOnlyFilesWithValidMimeTypesAreReturned(){
        List<File> filesWithAllowedMimeTypes = infoDeriver.getFilesWithPermittedMimeTypes(infoDeriver.retrieveInfoForFilesInDirectory(configFileDirectory));
        assertEquals(8,filesWithAllowedMimeTypes.size());
    }

}
