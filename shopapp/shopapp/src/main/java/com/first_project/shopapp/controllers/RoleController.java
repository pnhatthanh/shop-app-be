package com.first_project.shopapp.controllers;

import com.first_project.shopapp.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    @GetMapping("")
    public ResponseEntity<?> getAllRole(){
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}
