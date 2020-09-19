package com.example.demo.controller;

import com.example.demo.service.QywxThirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class Contact {
    @Autowired
    private QywxThirdService qywxThirdService;

    @RequestMapping("/contact/department")
    Map department(@RequestParam(value = "id") String id){
        return  qywxThirdService.getDepartmentList(id);
    }

    @RequestMapping("/contact/user")
    Map user(@RequestParam(value = "department_id") String departmentId){
        return  qywxThirdService.getUserSimplelist(departmentId,"0");
    }

}
