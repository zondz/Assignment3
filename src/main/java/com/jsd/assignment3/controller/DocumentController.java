package com.jsd.assignment3.controller;

import com.jsd.assignment3.model.entity.Document;
import com.jsd.assignment3.model.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
                    int version = oldVersion.get(oldVersion.size()-1).getVersion();
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
                    //set version = last version + 1
                    document.setVersion(oldVersion.get(oldVersion.size()-1).getVersion()+1);

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


    @GetMapping(value = "/get-all")
    public List<Document> getAllDocuments(){


        return documentService.getDocuments();
    }

    @GetMapping(value="/download")
    public void downloadDocument(@RequestParam(name = "id") Long id, HttpServletResponse response) throws Exception {
        Optional<Document> downloadDocument = documentService.findById(id);
        if(!downloadDocument.isPresent()){
            throw new Exception("Could not find file with id : "+id);

        }

        Document result =downloadDocument.get();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename="+result.getName();
        response.setHeader(headerKey,headerValue);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        // get the content from server file
        // get the file
        File serverFile = documentService.getDocumentByPath(result.getPath());
        byte[] fileContent =  Files.readAllBytes(serverFile.toPath());
        servletOutputStream.write(fileContent);
        // update in database
        result.setNumberOfDownload(result.getNumberOfDownload()+1);
        documentService.save(result);
        servletOutputStream.close();


    }


    @DeleteMapping("/delete")
    public void deleteDocument(@RequestParam(name="id") Long id) throws Exception {
        Optional<Document> deleteDocument = documentService.findById(id);
        if(!deleteDocument.isPresent()){
            throw new Exception("Not found delete document with id : "+id);

        }
        Document document = deleteDocument.get();
        File deleteServerFile= new File(document.getPath());
        boolean result = deleteServerFile.delete();
        if(!result){
            throw new Exception("Delete file failed ! ");
        }
        documentService.delete(document);



    }

}
