package pl.domodro.shop.simple.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import pl.domodro.shop.simple.model.OrderItem;
import pl.domodro.shop.simple.repository.OrderEntity;
import pl.domodro.shop.simple.repository.OrderItemEntity;
import pl.domodro.shop.simple.repository.ProductEntity;

public class OrderItemMapperTest {

    private static final String TEST_PRODUCT_ID = "test_product_id";

    private static final BigDecimal TEST_PRICE = BigDecimal.TEN;

    private static final BigDecimal TEST_QUANTITY = BigDecimal.ONE;

    private final OrderItemMapper orderItemMapper = new OrderItemMapper();

    @Test
    public void shouldMapToOrderItemEntity() {
        // given
        var orderItem = OrderItem.builder()
                .withQuantity(TEST_QUANTITY)
                .build();
        var orderEntity = OrderEntity.builder().build();
        var productEntity = ProductEntity.builder()
                .price(TEST_PRICE)
                .build();

        // when
        var result = orderItemMapper.map(orderItem, orderEntity, productEntity);

        // then
        assertThat(result.getQuantity()).isEqualTo(TEST_QUANTITY);
        assertThat(result.getPrice()).isEqualTo(TEST_PRICE);
        assertThat(result.getOrder()).isSameAs(orderEntity);
        assertThat(result.getProduct()).isSameAs(productEntity);
    }

    @Test
    public void shouldMapEmptyToOrderItemEntity() {
        // given
        var orderItem = OrderItem.builder().build();
        var orderEntity = OrderEntity.builder().build();
        var productEntity = ProductEntity.builder().build();

        // when
        var result = orderItemMapper.map(orderItem, orderEntity, productEntity);

        // then
        assertThat(result.getQuantity()).isNull();
        assertThat(result.getPrice()).isNull();
        assertThat(result.getOrder()).isSameAs(orderEntity);
        assertThat(result.getProduct()).isSameAs(productEntity);
    }

    @Test
    public void shouldMapToOrderItem() {
        // given
        var orderEntity = OrderEntity.builder().build();
        var productEntity = ProductEntity.builder()
                .id(TEST_PRODUCT_ID)
                .build();
        var orderItemEntity = OrderItemEntity.builder()
                .order(orderEntity)
                .product(productEntity)
                .quantity(TEST_QUANTITY)
                .price(TEST_PRICE)
                .build();

        // when
        var result = orderItemMapper.map(orderItemEntity);

        // then
        assertThat(result.getQuantity()).isEqualTo(TEST_QUANTITY);
        assertThat(result.getPrice()).isEqualTo(TEST_PRICE);
        assertThat(result.getProduct()).isEqualTo(TEST_PRODUCT_ID);
    }

    @Test
    public void shouldMapEmptyToOrderItem() {
        // given
        var orderItemEntity = OrderItemEntity.builder().build();

        // when
        var result = orderItemMapper.map(orderItemEntity);

        // then
        assertThat(result.getQuantity()).isNull();
        assertThat(result.getPrice()).isNull();
        assertThat(result.getProduct()).isNull();
    }
}
