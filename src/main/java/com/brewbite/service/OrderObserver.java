package com.brewbite.service;

import com.brewbite.model.Order;
import java.util.List;

public interface OrderObserver {
    void updateOrders(List<Order> orders);
}