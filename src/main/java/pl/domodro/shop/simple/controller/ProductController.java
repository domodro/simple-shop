package pl.domodro.shop.simple.controller;

import java.util.Collection;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.domodro.shop.simple.exception.InvalidIdException;
import pl.domodro.shop.simple.model.Product;
import pl.domodro.shop.simple.service.ProductService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    Collection<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping(value = "/{productId}")
    Product getProduct(@PathVariable("productId") String productId) throws InvalidIdException {
        return productService.getProduct(productId);
    }

    @PutMapping
    ResponseEntity<Void> createProduct(@Valid @RequestBody Product product) throws InvalidIdException {
        var productId = productService.createProduct(product);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(productId).toUri();
        return ResponseEntity.created(location)
                .build();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateProduct(@Valid @RequestBody Product product) throws InvalidIdException {
        productService.updateProduct(product);
    }
}
