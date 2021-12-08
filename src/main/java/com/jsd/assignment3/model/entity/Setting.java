package com.jsd.assignment3.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "settings")
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "max_file_size")
    private int maxFileSize;
    @Column(name = "item_per_page")
    private int itemPerPage;
    @Column(name = "mime_type_allow")
    private String mimeTypeAllowed;
    @Column(name = "last_update_time")
    private LocalDateTime lastUpdatedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

    public String getMimeTypeAllowed() {
        return mimeTypeAllowed;
    }

    public void setMimeTypeAllowed(String mimeTypeAllowed) {
        this.mimeTypeAllowed = mimeTypeAllowed;
    }

    public LocalDateTime getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(LocalDateTime lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
