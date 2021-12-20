package com.jsd.assignment3.controller;

import com.jsd.assignment3.model.entity.Document;
import com.jsd.assignment3.model.entity.Setting;
import com.jsd.assignment3.model.service.DocumentService;
import com.jsd.assignment3.model.service.SettingService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
@RequestMapping(path = "document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private SettingService settingService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // b1 : backend nhận file từ FE
    // b2 : backend lưu file vào ổ D , lấy path lưu vào trường path trong database
    // b3 : lấy đường dẫn file từ ổ D : ví dụ : D:\\...abc.png
    @PostMapping(value = "/upload")
    public String uploadFileHandler(@RequestParam("file") MultipartFile file) throws Exception {
        System.out.println("in here");


        if (!validateFile(file)) {

            return "Invalid file contraints";
        }
        // get file name : abc.png
        String fileName = file.getOriginalFilename();

        // png
        String fileNameExtension = FilenameUtils.getExtension(file.getOriginalFilename());

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
                if (!oldVersion.isEmpty()) {

                    int version = oldVersion.get(oldVersion.size() - 1).getVersion();
                    String newFileNameWithVersion = FilenameUtils.removeExtension(fileName) + "(" + version + ")" + "." + FilenameUtils.getExtension(file.getOriginalFilename());
                    // Create the file on server

                    serverFile = new File(dir.getAbsolutePath() + File.separator + newFileNameWithVersion);

                } else {
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

                document.setFileSize(bytes.length / 1024);


                document.setMime(fileNameExtension);
                //  document.setMime(fileName.split("\\.")[1]);
                String versionIds = null;
                if (!oldVersion.isEmpty()) {
                    // 1,2,3,4,5
                    versionIds = oldVersion.get(0).getVersionId();
                    // split

                    // if there is only the first version
                    // 1
                    if (versionIds.equals("1")) {
                        document.setVersion(2);
                        // wrong here // not update old version and new version
                        versionIds += "," + 2;
                        document.setVersionId(versionIds);
                    }
                    // if there are two more version ids
                    // 1,2,3,4,5
                    else {
                        String[] versionIdsList = versionIds.split("\\,");
                        // System.out.println(versionIdsList);

                        // length - 1 = highest version , highest version + 1 = new version

                        String lastVersionId = versionIdsList[versionIdsList.length - 1];
                        int newVersionId = Integer.parseInt(lastVersionId) + 1;
                        document.setVersion(newVersionId);
                        versionIds += "," + document.getVersion();
                        document.setVersionId(versionIds);
                    }

                    // save new record and update all list --
                } else {

                    document.setVersion(1);
                    document.setVersionId("1");
                }
                document.setStatus("OPEN");
                document.setCreatedDateTime(LocalDateTime.now());
                // document.setVersionId(null);
                if (!oldVersion.isEmpty()) {
                    for (Document d : oldVersion) {
                        d.setVersionId(versionIds);

                    }

                    documentService.saveAll(oldVersion);
                }
                documentService.save(document);


                return "finish upload !";
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return fileName;
    }

    private boolean validateFile(MultipartFile file) throws IOException {

        Setting setting = settingService.findRecord();

        // no condition
        if (setting == null) {
            // System.out.println("record not exist");
            return true;
        }

        boolean result = true;

        if (!setting.getMimeTypeAllowed().equalsIgnoreCase(FilenameUtils.getExtension(file.getOriginalFilename()))) {
            result = false;

        }
        // check file size
        if (file.getBytes().length / 1024 > setting.getMaxFileSize()) {
            result = false;
        }


        return result;

    }


    @GetMapping(value = "/get-all")
    public List<Document> getAllDocuments() {


         return documentService.getAllOpenDocuments();
    }

    @GetMapping(value = "/download")
    public void downloadDocument(@RequestParam(name = "id") Long id, HttpServletResponse response) throws Exception {
        Optional<Document> downloadDocument = documentService.findById(id);
        if (!downloadDocument.isPresent()) {
            throw new Exception("Could not find file with id : " + id);

        }

        Document result = downloadDocument.get();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=" + result.getName();
        response.setHeader(headerKey, headerValue);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        // get the content from server file
        // get the file
        File serverFile = documentService.getDocumentByPath(result.getPath());
        byte[] fileContent = Files.readAllBytes(serverFile.toPath());
        servletOutputStream.write(fileContent);
        // update in database
        result.setNumberOfDownload(result.getNumberOfDownload() + 1);
        documentService.save(result);
        servletOutputStream.close();


    }


    @DeleteMapping("/delete")
    public void deleteDocument(@RequestParam(name = "id") Long id) throws Exception {
        Optional<Document> deleteDocument = documentService.findById(id);
        if (!deleteDocument.isPresent()) {
            throw new Exception("Not found delete document with id : " + id);

        }
        Document document = deleteDocument.get();
        File deleteServerFile = new File(document.getPath());
        // boolean result = deleteServerFile.delete();
//        if(!result){
//            throw new Exception("Delete file failed ! ");
//        }

        document.setStatus("DELETED");
        documentService.save(document);


    }


    @GetMapping(value = "/get-and-paginate")
    public List<Document> getAndPageDocumentsByMaxFileSize(@RequestParam(name = "pageNumber") int pageNumber) {
        // if exist setting -> applied setting properties
        // else {
        //  return all records with status != DELETED
        // }
        Setting setting = settingService.findRecord();
        if(setting==null){
            // get document but still check condition

            return documentService.getAllOpenDocuments();
        }
        else{
            return documentService.getAndPageOpenDocumentsByMaxFileSize(setting.getMaxFileSize(), setting.getItemPerPage(), pageNumber);
        }


    }

}
