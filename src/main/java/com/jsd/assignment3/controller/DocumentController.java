package com.jsd.assignment3.controller;

import com.jsd.assignment3.model.entity.Document;
import com.jsd.assignment3.model.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    uploading
    downloading file

 */
@RestController
@RequestMapping(path="document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload")
    public String uploadFileHandler(@RequestParam("name")String fileName ,@RequestParam("file")MultipartFile file){
        System.out.println("initial file name : "+ fileName);
        File dir = null;
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("user.home");
                 dir = new File(rootPath + File.separator + "uploadFiles");
                if (!dir.exists()) {
                    dir.mkdirs();
                }


                File serverFile = null;

                // get older version name
                List<Document> oldVersion = documentService.findByName(fileName);
                if(!oldVersion.isEmpty()){

                    String[] seperateFileName = fileName.split("\\.");
                    int version = oldVersion.size();
                    String newFileNameWithVersion = seperateFileName[0]+"("+version+")"+"."+seperateFileName[1];
                    // Create the file on server

                    serverFile = new File(dir.getAbsolutePath() + File.separator + newFileNameWithVersion);

                }
                else{
                    // Create the file on server
                    serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);

                }

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                System.out.println("Server File Location=" + serverFile.getAbsolutePath());

                // save to database

                Document document = new Document();

                document.setName(fileName);

                document.setPath(serverFile.getAbsolutePath());

                document.setFileSize(bytes.length);


                document.setMime(fileName.split("\\.")[1]);

                if(!oldVersion.isEmpty()){
                    document.setVersion(oldVersion.size()+1);

                }else{

                    document.setVersion(1);
                }
                document.setStatus(null);
                document.setCreatedDateTime(LocalDateTime.now());
                document.setVersionId(null);

                documentService.save(document);


                return "finish upload !" ;
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return fileName;
    }


}
