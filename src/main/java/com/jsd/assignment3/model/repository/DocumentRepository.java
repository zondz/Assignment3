package com.jsd.assignment3.model.repository;

import com.jsd.assignment3.model.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findDocumentsByNameEquals(String name);

    List<Document> findByFileSizeLessThanEqualAndStatusNot(float fileSize,String status, Pageable pageable);

    List<Document> findByStatusNot(String status);

   Page<Document> findByStatusContaining(String status,Pageable pageable);

}
