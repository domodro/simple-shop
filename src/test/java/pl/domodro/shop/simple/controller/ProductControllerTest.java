package pl.domodro.shop.simple.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.domodro.shop.simple.exception.InvalidIdException;
import pl.domodro.shop.simple.exception.ValidationException;
import pl.domodro.shop.simple.model.Product;
import pl.domodro.shop.simple.service.ProductService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    private static final String TEST_NAME = "Orange Juice";

    private static final String TEST_ID = "orange_juice";

    private static final BigDecimal TEST_PRICE = BigDecimal.valueOf(99L);

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldGetProductsReturn() throws Exception {
        // given
        var product = Product.builder()
                .withId(TEST_ID)
                .withName(TEST_NAME)
                .withPrice(TEST_PRICE)
                .build();
        given(productService.getProducts()).willReturn(Collections.singletonList(product));

        // when
        mvc.perform(get("/products"))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(TEST_ID)))
                .andExpect(jsonPath("$[0].name", is(TEST_NAME)))
                .andExpect(jsonPath("$[0].price", is(TEST_PRICE.intValue())));
    }

    @Test
    public void shouldGetProductForValidId() throws Exception {
        // given
        var product = Product.builder()
                .withId(TEST_ID)
                .withName(TEST_NAME)
                .withPrice(TEST_PRICE)
                .build();
        given(productService.getProduct(TEST_ID)).willReturn(product);

        // when
        mvc.perform(get("/products/" + TEST_ID))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TEST_ID)))
                .andExpect(jsonPath("$.name", is(TEST_NAME)))
                .andExpect(jsonPath("$.price", is(TEST_PRICE.intValue())));
    }

    @Test
    public void shouldGetProductForInvalidId() throws Exception {
        // given
        given(productService.getProduct(TEST_ID)).willThrow(InvalidIdException.class);

        // when
        mvc.perform(get("/products/" + TEST_ID))

                // then
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateProductValid() throws Exception {
        // given
        var product = Product.builder()
                .withName(TEST_NAME)
                .withPrice(TEST_PRICE)
                .build();
        given(productService.createProduct(any())).willReturn(TEST_ID);

        // when
        mvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        )

                // then
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/products/" + TEST_ID));
    }

    @Test
    public void shouldCreateProductInvalid() throws Exception {
        // given
        var product = Product.builder()
                .withId(TEST_ID)
                .withName(TEST_NAME)
                .withPrice(TEST_PRICE)
                .build();
        given(productService.createProduct(any())).willThrow(ValidationException.class);

        // when
        mvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        )

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateProductValid() throws Exception {
        // given
        var product = Product.builder()
                .withId(TEST_ID)
                .withName(TEST_NAME)
                .withPrice(TEST_PRICE)
                .build();

        // when
        mvc.perform(patch("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        )

                // then
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldUpdateProductInvalid() throws Exception {
        // given
        var product = Product.builder()
                .withName(TEST_NAME)
                .withPrice(TEST_PRICE)
                .build();
        doThrow(new ValidationException(null)).when(productService).updateProduct(any());

        // when
        mvc.perform(patch("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        )

                // then
                .andExpect(status().isBadRequest());
    }
}
