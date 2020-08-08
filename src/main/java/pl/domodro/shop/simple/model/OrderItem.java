package pl.domodro.shop.simple.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
@JsonDeserialize(builder = OrderItem.OrderItemBuilder.class)
public class OrderItem {

    @NotNull
    String product;

    @Null
    BigDecimal price;

    @NotNull
    @Positive
    BigDecimal quantity;

}
