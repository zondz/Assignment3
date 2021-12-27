package com.jsd.assignment3.model.service;

import com.jsd.assignment3.model.entity.Document;
import com.jsd.assignment3.model.entity.Setting;
import com.jsd.assignment3.model.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    public File[] getStoredFiles(String folderPath){
        File folder  = new File(folderPath);
        File[] files = folder.listFiles();
        return files;

    }

    public List<Document> findByName(String name) {
        return documentRepository.findDocumentsByNameEquals(name);

    }

    public void save(Document document) {
        documentRepository.save(document);

    }

    public List<Document> getDocuments() {

        return documentRepository.findAll();

    }

    public Optional<Document> findById(Long id) {
        return documentRepository.findById( id);

    }

    public File getDocumentByPath(String path) {
        File result = new File(path);
        return result;

    }

    public void delete(Document document) {

        documentRepository.delete(document);

    }

    public Page<Document> getAndPageOpenDocumentsByMaxFileSize(Setting setting, int pageNumber) {
//        Pageable page = PageRequest.of(pageNumber,numberOfRecordPerPage);
//
        Pageable page = PageRequest.of(pageNumber,setting.getItemPerPage(),
                Sort.by("name").ascending().and(Sort.by("version").ascending()));
        Page<Document> result = documentRepository.findByStatusContaining("OPEN",page);

    System.out.println(result);
    return result;

    }

    public void saveAll(List<Document> oldVersion) {

        documentRepository.saveAll(oldVersion);

    }

    public List<Document> getAllOpenDocuments() {

        return documentRepository.findByStatusNot("DELETED");

    }
}
