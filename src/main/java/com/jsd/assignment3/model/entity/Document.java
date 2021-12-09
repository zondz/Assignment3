package com.jsd.assignment3.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "files")
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String path;
    @Column(name ="file_size")
    private float fileSize;
    private String mime;
    @Column(name = "download_number")
    private int numberOfDownload;
    private int version;
    private String status;
    @Column(name = "create_date_time")
    private LocalDateTime createdDateTime;
    @Column(name = "version_id")
    private String versionId;


}
