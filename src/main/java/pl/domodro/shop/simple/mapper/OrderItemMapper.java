package pl.domodro.shop.simple.mapper;

import static java.util.Optional.ofNullable;

import org.springframework.stereotype.Service;
import pl.domodro.shop.simple.model.OrderItem;
import pl.domodro.shop.simple.repository.OrderEntity;
import pl.domodro.shop.simple.repository.OrderItemEntity;
import pl.domodro.shop.simple.repository.ProductEntity;

@Service
public class OrderItemMapper {

    public OrderItemEntity map(OrderItem orderItem, OrderEntity orderEntity, ProductEntity productEntity) {
        return OrderItemEntity.builder()
                .product(productEntity)
                .order(orderEntity)
                .price(productEntity.getPrice())
                .quantity(orderItem.getQuantity())
                .build();
    }

    public OrderItem map(OrderItemEntity orderItemEntity) {
        var productId = ofNullable(orderItemEntity.getProduct())
                .map(ProductEntity::getId)
                .orElse(null);
        return OrderItem.builder()
                .withProduct(productId)
                .withPrice(orderItemEntity.getPrice())
                .withQuantity(orderItemEntity.getQuantity())
                .build();
    }
}
