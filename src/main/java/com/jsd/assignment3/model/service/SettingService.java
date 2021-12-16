package com.jsd.assignment3.model.service;

import com.jsd.assignment3.model.entity.Setting;
import com.jsd.assignment3.model.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    public boolean checkExist() {

        List<Setting> settingList = settingRepository.findAll();

        if(!settingList.isEmpty()) {
            return true;
        }
        return false;

    }

    public Setting findRecord() {

      List<Setting> settings = settingRepository.findAll();
        if(settings.isEmpty()){
            return null;
        }
      return settings.get(0);
    }

    public void save(Setting record) {

        settingRepository.save(record);
    }
}
