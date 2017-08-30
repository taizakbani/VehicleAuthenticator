package service;

import static java.lang.String.format;
import static java.nio.file.Files.probeContentType;
import static java.nio.file.Paths.get;
import static java.util.Arrays.stream;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.FileInfo;

public class InfoDeriver {

    public static final Logger LOGGER= getLogger(InfoDeriver.class.getName());

    public List<FileInfo> retrieveInfoForFilesInDirectory(File fileDirectory){
        List<FileInfo> fileInformationList= new ArrayList<>();
        stream(fileDirectory.listFiles()).forEach(i->fileInformationList.add(getInformationForIndividualFile(i)));
        return fileInformationList;
    }

    public FileInfo getInformationForIndividualFile(File file){
        try {
            return new FileInfo(file.getName(),probeContentType(get(file.getAbsolutePath())),file.length(),getExtension(file.getName()));
        } catch (IOException e) {
            LOGGER.log(SEVERE, ()->format("Unable to parse path %s",file.getAbsolutePath()));
        }
        return null;
    }

    public List<File> getFilesWithPermittedMimeTypes(List<FileInfo> filesList){
        List<File> filesWithValidMimeType=  new ArrayList<>();
        filesList.forEach(i->{
            if(isPermissibleMimeType(i.getFileMimeType())){
                filesWithValidMimeType.add(new File(getConfigDirAbsoultePath()+i.getFileName()));
            }
        });

        return filesWithValidMimeType;
    }

    public boolean isPermissibleMimeType(String mimeType){
        if(!isBlank(mimeType)){
            return mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")||mimeType.equals("application/vnd.ms-excel");
        }
        return false;
    }

    public String getConfigDirAbsoultePath(){
        return new File("").getAbsolutePath()+"/config_dir/";
    }


}
