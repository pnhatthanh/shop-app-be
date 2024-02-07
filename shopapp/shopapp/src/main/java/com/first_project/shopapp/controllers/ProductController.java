package com.first_project.shopapp.controllers;

import com.first_project.shopapp.dtos.ProductDTO;
import com.first_project.shopapp.dtos.ProductImageDTO;
import com.first_project.shopapp.models.Product;
import com.first_project.shopapp.models.ProductImage;
import com.first_project.shopapp.responses.ProductResponse;
import com.first_project.shopapp.services.DetailProductService;
import com.first_project.shopapp.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import java.net.MalformedURLException;
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
    @Autowired
    DetailProductService detailProductService;

    //add new product by admin
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

    //upload image for product by admin
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

    //store image in uploads
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

    //get image by name's image in uploads
    @GetMapping("/thumbnail/{img_url}")
    public ResponseEntity<?> getThumbnail(@PathVariable("img_url") String imgUrl){
        try{
                Path imagePth=Paths.get("uploads/"+imgUrl);
                Resource resource=new UrlResource(imagePth.toUri());
                if(resource.exists()){
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                            .body(resource);
                }else{
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                            .body(new UrlResource(Paths.get("uploads/not_found.png").toUri()));
                }
            } catch (MalformedURLException e) {
                return ResponseEntity.notFound().build();
            }
    }

    //update product by id with admin
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

    //get all product by page
    @GetMapping("")
    public ResponseEntity<?> getProducts(@RequestParam(value = "page",defaultValue = "0") int page,
                                         @RequestParam(value = "limit",defaultValue = "12") int limit){
        PageRequest pageRequest=PageRequest.of(page,limit, Sort.by("id").ascending());
        Page<Product> productPage = productService.getAllProducts(pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(productPage);
    }

    //search products by category or name
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "limit",defaultValue = "12") int limit,
                                            @RequestParam(value = "category", defaultValue = "0") Long idCategory,
                                            @RequestParam(value = "nameProduct", defaultValue = "") String nameProduct){
        PageRequest pageRequest=PageRequest.of(page, limit);
        Page<Product> products=productService.searchProducts(idCategory,nameProduct,pageRequest);
        List<Product> productList=products.getContent();
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }






    //get detail product by id
    @GetMapping("/description/{id}")
    public ResponseEntity<?> getDescriptionProduct(@PathVariable(name = "id") Long id){
        try{
            ProductResponse product=detailProductService.getDetailProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable(name = "id") Long id){
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

    //get all image's product by id
    @GetMapping("/image/{id}")
    public ResponseEntity<?> getImageProduct(@PathVariable(name = "id") Long id){
        try{
            return ResponseEntity.ok().body(detailProductService.getProductImage(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
