package pl.domodro.shop.simple.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
import pl.domodro.shop.simple.model.Order;
import pl.domodro.shop.simple.model.OrderItem;
import pl.domodro.shop.simple.service.OrderService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    private static final Long TEST_ID = 78L;

    private static final String TEST_EMAIL = "test@email.com";

    private static final String TEST_PRODUCT = "orange";

    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldGetOrderForEmail() throws Exception {
        // given
        var order = Order.builder()
                .withId(TEST_ID)
                .withEmail(TEST_EMAIL)
                .build();
        given(orderService.getOrdersByEmail(TEST_EMAIL)).willReturn(Collections.singletonList(order));

        // when
        mvc.perform(get("/orders").param("email", TEST_EMAIL))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", is(TEST_EMAIL)));
    }

    @Test
    public void shouldCreateOrderReturnOk() throws Exception {
        // given
        var item = OrderItem.builder()
                .withQuantity(BigDecimal.ONE)
                .withProduct(TEST_PRODUCT)
                .build();
        var items = Collections.singletonList(item);
        var order = Order.builder()
                .withEmail(TEST_EMAIL)
                .withItems(items)
                .build();
        given(orderService.createOrder(any())).willReturn(TEST_ID);

        // when
        mvc.perform(put("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order))
        )
                // then
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/orders/" + TEST_ID));
    }

    @Test
    public void shouldCreateOrderReturnBadRequest() throws Exception {
        // given
        var item = OrderItem.builder()
                .withQuantity(BigDecimal.ONE)
                .withProduct(TEST_PRODUCT)
                .build();
        var items = Collections.singletonList(item);
        var order = Order.builder()
                .withId(TEST_ID)
                .withEmail(TEST_EMAIL)
                .withItems(items)
                .build();

        // when
        mvc.perform(put("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order))
        )
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetOrderForValidId() throws Exception {
        // given
        var order = Order.builder()
                .withId(TEST_ID)
                .withEmail(TEST_EMAIL)
                .build();
        given(orderService.getOrder(TEST_ID)).willReturn(order);

        // when
        mvc.perform(get("/orders/" + TEST_ID))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TEST_ID.intValue())))
                .andExpect(jsonPath("$.email", is(TEST_EMAIL)));
    }

    @Test
    public void shouldGetOrderForInvalidId() throws Exception {
        // given
        given(orderService.getOrder(TEST_ID)).willThrow(InvalidIdException.class);

        // when
        mvc.perform(get("/orders/" + TEST_ID))

                // then
                .andExpect(status().isNotFound());
    }
}
