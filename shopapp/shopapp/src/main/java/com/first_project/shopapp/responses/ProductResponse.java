package com.first_project.shopapp.responses;

import com.first_project.shopapp.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse{
    private String nameProduct;
    private Long categoryId;
    private float price;
    private String thumbnail;
    private String description;
    public static ProductResponse fromProduct(Product product){
        ProductResponse productResponse=ProductResponse.builder()
                .nameProduct(product.getNameProduct())
                .categoryId(product.getCategory().getId())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .build();
        productResponse.setCreateAt(product.getCreatedAt());
        productResponse.setUpdateAt(product.getUpdatedAt());
        return productResponse;
    }
}
