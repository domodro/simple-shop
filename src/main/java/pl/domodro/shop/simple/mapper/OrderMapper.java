package pl.domodro.shop.simple.mapper;

import static java.util.Optional.ofNullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.domodro.shop.simple.model.Order;
import pl.domodro.shop.simple.repository.OrderEntity;
import pl.domodro.shop.simple.repository.OrderItemEntity;

@Service
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public OrderEntity map(Order order) {
        return OrderEntity.builder()
                .id(order.getId())
                .email(order.getEmail())
                .date(LocalDateTime.now())
                .build();
    }

    public Order map(OrderEntity orderEntity) {
        var orderItems = ofNullable(orderEntity.getItemCollection()).stream()
                .flatMap(Collection::stream)
                .map(orderItemMapper::map)
                .collect(Collectors.toUnmodifiableList());
        return Order.builder()
                .withId(orderEntity.getId())
                .withItems(orderItems)
                .withEmail(orderEntity.getEmail())
                .withDate(orderEntity.getDate())
                .withTotalPrice(sumTotalPrice(orderEntity.getItemCollection()))
                .build();
    }

    private BigDecimal sumTotalPrice(Collection<OrderItemEntity> items) {
        return ofNullable(items).stream()
                .flatMap(Collection::stream)
                .map(item -> item.getPrice().multiply(item.getQuantity()))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
