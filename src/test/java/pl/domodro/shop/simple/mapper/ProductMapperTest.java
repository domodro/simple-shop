package pl.domodro.shop.simple.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import pl.domodro.shop.simple.model.Product;
import pl.domodro.shop.simple.repository.ProductEntity;

public class ProductMapperTest {

    private static final String TEST_ID = "test_id";

    private static final String TEST_NAME = "test_name";

    private static final BigDecimal TEST_PRICE = BigDecimal.valueOf(44L);

    private final ProductMapper productMapper = new ProductMapper();

    @Test
    public void shouldMapToProductEntity() {
        // given
        var product = Product.builder()
                .withId(TEST_ID)
                .withName(TEST_NAME)
                .withPrice(TEST_PRICE)
                .build();

        // when
        var result = productMapper.map(product);

        // then
        assertThat(result.getId()).isEqualTo(TEST_ID);
        assertThat(result.getName()).isEqualTo(TEST_NAME);
        assertThat(result.getPrice()).isEqualTo(TEST_PRICE);
    }

    @Test
    public void shouldMapEmptyToProductEntity() {
        // given
        var product = Product.builder().build();

        // when
        var result = productMapper.map(product);

        // then
        assertThat(result.getId()).isNull();
        assertThat(result.getName()).isNull();
        assertThat(result.getPrice()).isNull();
    }

    @Test
    public void shouldMapToProduct() {
        // given
        var productEntity = ProductEntity.builder()
                .id(TEST_ID)
                .name(TEST_NAME)
                .price(TEST_PRICE)
                .build();

        // when
        var result = productMapper.map(productEntity);

        // then
        assertThat(result.getId()).isEqualTo(TEST_ID);
        assertThat(result.getName()).isEqualTo(TEST_NAME);
        assertThat(result.getPrice()).isEqualTo(TEST_PRICE);
    }

    @Test
    public void shouldMapEmptyToProduct() {
        // given
        var productEntity = ProductEntity.builder().build();

        // when
        var result = productMapper.map(productEntity);

        // then
        assertThat(result.getId()).isNull();
        assertThat(result.getName()).isNull();
        assertThat(result.getPrice()).isNull();
    }
}
