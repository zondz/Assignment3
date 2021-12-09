package com.jsd.assignment3.model.service;

import com.jsd.assignment3.model.entity.Document;
import com.jsd.assignment3.model.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
}
