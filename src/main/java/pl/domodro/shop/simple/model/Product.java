package pl.domodro.shop.simple.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder(setterPrefix = "with")
@RequiredArgsConstructor
@JsonDeserialize(builder = Product.ProductBuilder.class)
public class Product {

    private final String id;

    @NotNull
    private final String name;

    @NotNull
    @Positive
    private final BigDecimal price;
}
