package pl.domodro.shop.simple.mapper;

import org.springframework.stereotype.Service;
import pl.domodro.shop.simple.model.Product;
import pl.domodro.shop.simple.repository.ProductEntity;

@Service
public class ProductMapper {

    public ProductEntity map(Product product) {
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    public Product map(ProductEntity productEntity) {
        return Product.builder()
                .withId(productEntity.getId())
                .withName(productEntity.getName())
                .withPrice(productEntity.getPrice())
                .build();
    }
}
