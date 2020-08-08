package pl.domodro.shop.simple.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.domodro.shop.simple.model.Order;
import pl.domodro.shop.simple.model.OrderItem;
import pl.domodro.shop.simple.repository.OrderEntity;
import pl.domodro.shop.simple.repository.OrderItemEntity;

@ExtendWith(MockitoExtension.class)
public class OrderMapperTest {

    private static final Long TEST_ID = 42L;

    private static final String TEST_EMAIL = "test_email";

    private static final LocalDateTime TEST_DATE = LocalDateTime.now();

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrderMapper orderMapper;

    @Test
    public void shouldMapToOrderEntity() {
        // given
        var order = Order.builder()
                .withId(TEST_ID)
                .withEmail(TEST_EMAIL)
                .build();

        // when
        var result = orderMapper.map(order);

        // then
        assertThat(result.getId()).isEqualTo(TEST_ID);
        assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(result.getDate()).isNotNull();
        assertThat(result.getItemCollection()).isNull();
    }

    @Test
    public void shouldMapEmptyToOrderEntity() {
        // given
        var order = Order.builder().build();

        // when
        var result = orderMapper.map(order);

        // then
        assertThat(result.getId()).isNull();
        assertThat(result.getEmail()).isNull();
        assertThat(result.getDate()).isNotNull();
        assertThat(result.getItemCollection()).isNull();
    }

    @Test
    public void shouldMapToOrder() {
        // given
        var orderItemEntity1 = OrderItemEntity.builder()
                .quantity(BigDecimal.valueOf(2L))
                .price(BigDecimal.valueOf(4L))
                .build();
        var orderItemEntity2 = OrderItemEntity.builder()
                .quantity(BigDecimal.valueOf(3L))
                .price(BigDecimal.valueOf(3L))
                .build();
        var orderEntity = OrderEntity.builder()
                .id(TEST_ID)
                .email(TEST_EMAIL)
                .date(TEST_DATE)
                .itemCollection(Arrays.asList(orderItemEntity1, orderItemEntity2))
                .build();
        var orderItem1 = OrderItem.builder().build();
        var orderItem2 = OrderItem.builder().build();
        given(orderItemMapper.map(orderItemEntity1)).willReturn(orderItem1);
        given(orderItemMapper.map(orderItemEntity2)).willReturn(orderItem2);

        // when
        var result = orderMapper.map(orderEntity);

        // then
        assertThat(result.getId()).isEqualTo(TEST_ID);
        assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(result.getDate()).isEqualTo(TEST_DATE);
        assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.valueOf(17L));
        assertThat(result.getItems()).containsExactly(orderItem1, orderItem2);
    }

    @Test
    public void shouldMapEmptyToOrder() {
        // given
        var orderEntity = OrderEntity.builder()
                .build();

        // when
        var result = orderMapper.map(orderEntity);

        // then
        assertThat(result.getId()).isNull();
        assertThat(result.getEmail()).isNull();
        assertThat(result.getDate()).isNull();
        assertThat(result.getTotalPrice()).isZero();
        assertThat(result.getItems()).isEmpty();
    }
}
