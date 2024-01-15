package com.first_project.shopapp.controllers;

import com.first_project.shopapp.dtos.ProductDTO;
import com.first_project.shopapp.dtos.ProductImageDTO;
import com.first_project.shopapp.models.Product;
import com.first_project.shopapp.models.ProductImage;
import com.first_project.shopapp.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    @Autowired
    ProductService productService;
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                           BindingResult result){
        if(result.hasErrors()){
            List<String> messageErrors=result.getAllErrors().stream()
                    .map(error->error.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provider");
        }
        try{
            productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Create product successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getMessage());
        }
    }

    @PostMapping(value = "upload/img/{product_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImg(@PathVariable("product_id")Long id,
                                       @ModelAttribute("files") List<MultipartFile> files){
        try{
            Product existingProduct=productService.getProductById(id);
            files=files==null?new ArrayList<MultipartFile>():files;
            if(files.size()> ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                throw new RuntimeException("You can only upload maximum 5 images");
            }
            List<ProductImage> productImages=new ArrayList<ProductImage>();
            for(MultipartFile file : files){
                if(file.getSize()==0){
                    continue;
                }
                //Kiem tra kich thuoc file
                //10MB
                if(file.getSize()>10*1024*1024){
                    throw new RuntimeException("File is too large! Maximum size=10MB");
                }
                //Kiem tra dinh dang file
                String contentType=file.getContentType();
                if(contentType==null|| !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                }
                String fileName=storeFile(file);
                ProductImageDTO productImageDTO=new ProductImageDTO();
                productImageDTO.setImageUrl(fileName);
                ProductImage productImage=productService.createProductImage(id,productImageDTO);
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body("Upload successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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
    @PutMapping("/update/{product_id}")
    public ResponseEntity<?> updateProduct(@Valid @PathVariable("product_id") Long id,
                                           @RequestBody ProductDTO productDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> messageErrors=result.getAllErrors().stream()
                    .map(error->error.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provider");
        }
        try{
            productService.updateProduct(id, productDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Update successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getMessage());
        }
    }
    @GetMapping("")
    public ResponseEntity<?> getProducts(@RequestParam("page") int page, @RequestParam("limit") int limit){
        PageRequest pageRequest=PageRequest.of(page,limit, Sort.by("createdAt").descending());
        Page<Product> productPage = productService.getAllProducts(pageRequest);
        int totalPages=productPage.getTotalPages();
        List<Product> products=productPage.getContent();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id){
        try{
            Product product=productService.getProductById(id);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");
    }
}
