package pl.domodro.shop.simple.service;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.domodro.shop.simple.exception.InvalidIdException;
import pl.domodro.shop.simple.exception.ValidationException;
import pl.domodro.shop.simple.mapper.ProductMapper;
import pl.domodro.shop.simple.model.Product;
import pl.domodro.shop.simple.repository.ProductRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    private final ProductRepository productRepository;

    public Collection<Product> getProducts() {
        log.debug("Getting all products");
        return productRepository.findAll()
                .stream()
                .map(productMapper::map)
                .collect(Collectors.toUnmodifiableList());
    }

    public Product getProduct(String id) throws InvalidIdException {
        log.debug("Getting product for id {}", id);
        return productRepository.findById(id)
                .map(productMapper::map)
                .orElseThrow(() -> new InvalidIdException("Product with given ID does not exist"));
    }

    public String createProduct(final Product product) throws ValidationException {
        log.debug("Creating product {} with a price {}", product.getName(), product.getPrice());
        if (product.getId() != null) {
            throw new ValidationException("Product ID must be empty");
        }
        return productRepository.save(productMapper.map(product)).getId();
    }

    public void updateProduct(final Product product) throws ValidationException {
        log.debug("Updating product {} with a price {}", product.getId(), product.getPrice());
        if (product.getId() == null) {
            throw new ValidationException("Product ID must not be empty");
        }
        productRepository.save(productMapper.map(product));
    }
}
