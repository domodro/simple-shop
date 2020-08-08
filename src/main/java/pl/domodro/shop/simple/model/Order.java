package pl.domodro.shop.simple.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder(setterPrefix = "with")
@RequiredArgsConstructor
@JsonDeserialize(builder = Order.OrderBuilder.class)
public class Order {

    @Null
    private final Long id;

    @NotEmpty
    private final Collection<OrderItem> items;

    @NotNull
    @Email
    private final String email;

    @Null
    private final LocalDateTime date;

    @Null
    private final BigDecimal totalPrice;
}
