package pl.domodro.shop.simple.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.domodro.shop.simple.exception.InvalidIdException;
import pl.domodro.shop.simple.mapper.OrderItemMapper;
import pl.domodro.shop.simple.mapper.OrderMapper;
import pl.domodro.shop.simple.model.Order;
import pl.domodro.shop.simple.model.OrderItem;
import pl.domodro.shop.simple.repository.OrderEntity;
import pl.domodro.shop.simple.repository.OrderItemEntity;
import pl.domodro.shop.simple.repository.OrderRepository;
import pl.domodro.shop.simple.repository.ProductEntity;
import pl.domodro.shop.simple.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private static final String TEST_PRODUCT_ID = "test_product_id";

    private static final Long TEST_ORDER_ID = 23L;

    private static final String TEST_EMAIL = "test_email";

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void shouldCreateOrderReturnId() {
        // given
        var item = OrderItem.builder()
                .withProduct(TEST_PRODUCT_ID)
                .build();
        var order = Order.builder()
                .withItems(Collections.singletonList(item))
                .build();
        var orderEntity = OrderEntity.builder().build();
        var savedOrderEntity = OrderEntity.builder()
                .id(TEST_ORDER_ID)
                .build();
        var productEntity = ProductEntity.builder().build();
        var orderItemEntity = OrderItemEntity.builder().build();

        given(orderMapper.map(order)).willReturn(orderEntity);
        given(productRepository.findById(TEST_PRODUCT_ID)).willReturn(Optional.of(productEntity));
        given(orderItemMapper.map(item, orderEntity, productEntity)).willReturn(orderItemEntity);
        given(orderRepository.save(orderEntity)).willReturn(savedOrderEntity);

        // when
        var result = orderService.createOrder(order);

        // then
        assertThat(result).isEqualTo(TEST_ORDER_ID);
    }

    @Test
    public void shouldCreateOrderThrowOnNonExistingProduct() {
        // given
        var item = OrderItem.builder()
                .withProduct(TEST_PRODUCT_ID)
                .build();
        var order = Order.builder()
                .withItems(Collections.singletonList(item))
                .build();
        var orderEntity = OrderEntity.builder().build();

        given(orderMapper.map(order)).willReturn(orderEntity);
        given(productRepository.findById(TEST_PRODUCT_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                orderService.createOrder(order)
        )
                .isInstanceOf(InvalidIdException.class);
    }

    @Test
    public void shouldGetOrdersByEmailReturn() {
        // given
        var orderEntity = OrderEntity.builder().build();
        var order = Order.builder().build();

        given(orderRepository.findByEmail(TEST_EMAIL)).willReturn(Collections.singletonList(orderEntity));
        given(orderMapper.map(orderEntity)).willReturn(order);

        // when
        var result = orderService.getOrdersByEmail(TEST_EMAIL);

        // then
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(order);
    }

    @Test
    public void shouldGetOrderReturn() {
        // given
        var orderEntity = OrderEntity.builder().build();
        var order = Order.builder().build();

        given(orderRepository.findById(TEST_ORDER_ID)).willReturn(Optional.of(orderEntity));
        given(orderMapper.map(orderEntity)).willReturn(order);

        // when
        var result = orderService.getOrder(TEST_ORDER_ID);

        // then
        assertThat(result).isSameAs(order);
    }

    @Test
    public void shouldGetOrderThrowOnNonExistingOrder() {
        // given
        given(orderRepository.findById(TEST_ORDER_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                orderService.getOrder(TEST_ORDER_ID)
        )
                .isInstanceOf(InvalidIdException.class);
    }
}
