package com.first_project.shopapp.controllers;

import com.first_project.shopapp.dtos.CategoryDTO;
import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.Category;
import com.first_project.shopapp.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    //Sửa cái cùng tên category
    @PostMapping("")
    public ResponseEntity<?> createCategroy(@Valid @RequestPart String name,
                                            @RequestPart("file") MultipartFile file,
                                            BindingResult result){
        if(result.hasErrors()){
            List<String> messageErrors=result.getAllErrors().stream()
                    .map(error->error.getDefaultMessage()).toList();
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provider");
        }
        try {
            if(file.getSize()>10*1024*1024){
                throw new RuntimeException("File is too large! Maximum size=10MB");
            }
            String contentType=file.getContentType();
            if(contentType==null|| !contentType.startsWith("image/")){
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
            }
            String fileName=storeFile(file);
            CategoryDTO categoryDTO=new CategoryDTO();
            categoryDTO.setName(name);
            categoryDTO.setSymbol(fileName);
            categoryService.createCategory(categoryDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Insert category successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/symbol/{img_url}")
    public ResponseEntity<?> getUrlCategory(@PathVariable("img_url") String urlImg){
        try{
            Path path=Paths.get("uploads/"+urlImg);
            Resource resource=new UrlResource(path.toUri());
            if(resource.exists()){
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            }else{
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("upload/+not_found.png").toUri()));
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("")
    public ResponseEntity<?> getAllProduct(){
        List<Category> categories=categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> messageErrors=result.getAllErrors().stream().map(error->error.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provider");
        }
        try {
            Category category=categoryService.updateCategory(id,categoryDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Update category successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete category successfully");
    }

    private String storeFile(MultipartFile file) throws IOException {
        if(file.getOriginalFilename()==null){
            throw new RuntimeException("Invalid image format");
        }
        String fileName= StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Them UUID de file la duy nhat
        String uniqueFile= UUID.randomUUID().toString()+"_"+fileName;
        Path uploadDir= Paths.get("uploads");
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFile);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFile;
    }
}
