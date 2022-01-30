package ir.maktab.homeservice.repositories;

import ir.maktab.homeservice.entities.orders.Order;

import java.util.List;
import java.util.Map;

public interface CustomOrderRepository {
    public List<Order> findByParameterMap(Map<String,String> parameterMap);
}
