package com.jsd.assignment3.model.repository;

import com.jsd.assignment3.model.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SettingRepository extends JpaRepository<Setting,Long> {


}
