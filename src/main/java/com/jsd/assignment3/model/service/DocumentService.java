package com.jsd.assignment3.model.service;

import com.jsd.assignment3.model.entity.Document;
import com.jsd.assignment3.model.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
