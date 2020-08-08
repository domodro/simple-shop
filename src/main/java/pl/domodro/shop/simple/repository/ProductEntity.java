package pl.domodro.shop.simple.repository;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "simple_shop_product")
public class ProductEntity {

    @Id
    @GenericGenerator(name = "product_id_gen", strategy = "pl.domodro.shop.simple.repository.ProductIdGenerator")
    @GeneratedValue(generator = "product_id_gen")
    private String id;

    private String name;

    private BigDecimal price;
}
