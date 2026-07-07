package com.helloworld.dreamshopsbackend.service.order;

import com.helloworld.dreamshopsbackend.model.dto.OrderDto;
import com.helloworld.dreamshopsbackend.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder (Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
