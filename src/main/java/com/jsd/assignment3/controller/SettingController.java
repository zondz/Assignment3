package com.jsd.assignment3.controller;


import com.jsd.assignment3.model.entity.Setting;
import com.jsd.assignment3.model.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(path = "setting")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @PutMapping(value = "/update")
    public void updateSetting(@RequestBody Setting setting){

      Setting record  = settingService.findRecord();

       if(record ==  null){
           record = new Setting();
       }

       record.setItemPerPage(setting.getItemPerPage());
       record.setMaxFileSize(setting.getMaxFileSize());
       record.setMimeTypeAllowed(setting.getMimeTypeAllowed());// png
       record.setLastUpdatedTime(LocalDateTime.now());

       settingService.save(record);



    }


    @GetMapping("/get")
    public Setting getSetting(){
        Setting record  = settingService.findRecord();
            return record;
    }


}
