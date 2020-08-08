package pl.domodro.shop.simple.service;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.domodro.shop.simple.exception.InvalidIdException;
import pl.domodro.shop.simple.mapper.OrderItemMapper;
import pl.domodro.shop.simple.mapper.OrderMapper;
import pl.domodro.shop.simple.model.Order;
import pl.domodro.shop.simple.repository.OrderRepository;
import pl.domodro.shop.simple.repository.ProductRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    public Long createOrder(Order order) throws InvalidIdException {
        log.debug("Creating order for {}", order.getEmail());
        var orderEntity = orderMapper.map(order);
        var items = order.getItems()
                .stream()
                .map(
                        item -> productRepository.findById(item.getProduct())
                                .map(product -> orderItemMapper.map(item, orderEntity, product))
                                .orElseThrow(() -> new InvalidIdException("Product placed in the order does not exist"))
                )
                .collect(Collectors.toList());
        orderEntity.setItemCollection(items);
        return orderRepository.save(orderEntity).getId();
    }

    public Collection<Order> getOrdersByEmail(String email) {
        log.debug("Getting orders for email: {}", email);
        return orderRepository.findByEmail(email)
                .stream()
                .map(orderMapper::map)
                .collect(Collectors.toUnmodifiableList());
    }

    public Order getOrder(Long id) throws InvalidIdException {
        log.debug("Getting order for id: {}", id);
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new InvalidIdException("Order with such id does not exist"));
        return orderMapper.map(order);
    }
}
