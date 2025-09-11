package com.example.barkbuddy_backend.service;

import java.util.List;

import com.example.barkbuddy_backend.dto.OrderDTO;

public interface OrderService {
    OrderDTO createPaidOrder(Long userId, Long listingId, int quantity);
    List<OrderDTO> getMyOrders(Long userId);
}
