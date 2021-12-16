package com.jsd.assignment3.controller;

import com.jsd.assignment3.model.entity.Document;
import com.jsd.assignment3.model.entity.Setting;
import com.jsd.assignment3.model.repository.DocumentRepository;
import com.jsd.assignment3.model.service.DocumentService;
import com.jsd.assignment3.model.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "test")
public class Test {
    @Autowired
    DocumentService documentService;

    @Autowired
    SettingService settingService;

    @Autowired
    DocumentRepository documentRepository;
    @RequestMapping(value = "/get")
    public List<Document> getAndPage(@RequestParam("pageNumber") int pageNumber){


        Pageable page = PageRequest.of(pageNumber,2);
        return documentRepository.findByFileSizeLessThanEqualAndStatusNot(200,"DELETED",page);
    }
}
