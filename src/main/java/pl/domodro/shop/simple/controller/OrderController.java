package pl.domodro.shop.simple.controller;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.domodro.shop.simple.exception.InvalidIdException;
import pl.domodro.shop.simple.model.Order;
import pl.domodro.shop.simple.service.OrderService;

@Validated
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    Collection<Order> getOrders(@Email @RequestParam("email") final String email) {
        return orderService.getOrdersByEmail(email);
    }

    @GetMapping("/{order_id}")
    Order getOrder(@Min(1) @PathVariable("order_id") final Long orderId) {
        return orderService.getOrder(orderId);
    }

    @PutMapping
    ResponseEntity<Void> createOrder(@Valid @RequestBody final Order order) throws InvalidIdException {
        var orderId = orderService.createOrder(order);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(orderId).toUri();
        return ResponseEntity.created(location)
                .build();
    }
}
