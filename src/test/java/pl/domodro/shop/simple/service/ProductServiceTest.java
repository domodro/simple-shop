package pl.domodro.shop.simple.service;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.domodro.shop.simple.exception.InvalidIdException;
import pl.domodro.shop.simple.exception.ValidationException;
import pl.domodro.shop.simple.mapper.ProductMapper;
import pl.domodro.shop.simple.model.Product;
import pl.domodro.shop.simple.repository.ProductEntity;
import pl.domodro.shop.simple.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private static final String TEST_ID = "test_id";

    private static final String OTHER_TEST_ID = "di_tset";

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void shouldGetProductsReturn() {
        // given
        var productEntity = ProductEntity.builder().build();
        var product = Product.builder().build();

        given(productRepository.findAll()).willReturn(singletonList(productEntity));
        given(productMapper.map(productEntity)).willReturn(product);

        // when
        var result = productService.getProducts();

        // then
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(product);
    }

    @Test
    public void shouldGetProductReturnExistingId() throws InvalidIdException {
        // given
        var productEntity = ProductEntity.builder().build();
        var product = Product.builder().build();

        given(productRepository.findById(TEST_ID)).willReturn(Optional.of(productEntity));
        given(productMapper.map(productEntity)).willReturn(product);

        // when
        var result = productService.getProduct(TEST_ID);

        // then
        assertThat(result).isSameAs(product);
    }

    @Test
    public void shouldGetProductThrowOnNotExistingId() {
        assertThatThrownBy(() ->
                productService.getProduct(OTHER_TEST_ID)
        )
                .isInstanceOf(InvalidIdException.class);
    }

    @Test
    public void shouldCreateProductReturnId() throws InvalidIdException {
        // given
        var newProduct = Product.builder().build();
        var newProductEntity = ProductEntity.builder().build();
        var returnedProductEntity = ProductEntity.builder()
                .id(TEST_ID)
                .build();

        given(productMapper.map(newProduct)).willReturn(newProductEntity);
        given(productRepository.save(newProductEntity)).willReturn(returnedProductEntity);

        // when
        var result = productService.createProduct(newProduct);

        // then
        assertThat(result).isEqualTo(TEST_ID);
    }

    @Test
    public void shouldCreateProductThrowForNonEmptyId() throws InvalidIdException {
        // given
        var newProduct = Product.builder()
                .withId(TEST_ID)
                .build();

        assertThatThrownBy(() ->
                productService.createProduct(newProduct)
        )
                .isInstanceOf(ValidationException.class);
    }

    @Test
    public void shouldUpdateProductPass() throws InvalidIdException {
        // given
        var updatedProduct = Product.builder()
                .withId(TEST_ID)
                .build();
        var updatedProductEntity = ProductEntity.builder().build();
        var returnedProductEntity = ProductEntity.builder().build();

        given(productMapper.map(updatedProduct)).willReturn(updatedProductEntity);
        given(productRepository.save(updatedProductEntity)).willReturn(returnedProductEntity);

        assertThatCode(() ->
                productService.updateProduct(updatedProduct)
        )
                .doesNotThrowAnyException();
    }

    @Test
    public void shouldUpdateProductThrowForEmptyId() throws InvalidIdException {
        // given
        var updatedProduct = Product.builder().build();

        assertThatThrownBy(() ->
                productService.updateProduct(updatedProduct)
        )
                .isInstanceOf(ValidationException.class);
    }
}
