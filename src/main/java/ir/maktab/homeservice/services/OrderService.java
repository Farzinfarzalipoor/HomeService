package ir.maktab.homeservice.services;

import ir.maktab.homeservice.entities.Offer;
import ir.maktab.homeservice.entities.orders.Order;
import ir.maktab.homeservice.entities.orders.OrderStatus;
import ir.maktab.homeservice.entities.services.SubService;
import ir.maktab.homeservice.entities.users.Customer;
import ir.maktab.homeservice.entities.users.Expert;
import ir.maktab.homeservice.repositories.*;
import ir.maktab.homeservice.services.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OfferRepository offerRepository;

    @Autowired
    ExpertRepository specialistRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SubServiceRepository subServiceRepository;

    @Transactional
    public Order save(Order order) {
        order.setStatus(OrderStatus.WAITING_FOR_OFFERS);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return new ArrayList<>(orderRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).get().collect(Collectors.toList());
    }

    @Transactional
    public Order update(Long id, Order order) {
        Order a_order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        SubService subAssistance = subServiceRepository
                .findById(order.getSubService().getId()).orElseThrow(subServiceNotFoundException::new);
        a_order.setSubService(subAssistance);
        a_order.setOfferedPrice(order.getOfferedPrice());
        a_order.setDescription(order.getDescription());
        a_order.setExecutionDate(order.getExecutionDate());
        a_order.setAddress(order.getAddress());
        return a_order;
    }

    @Transactional
    public Order selectOffer(Long orderId, Long offerId) {
        Offer offer = offerRepository.findById(offerId).orElseThrow(OfferNotFoundException::new);
        if (!Objects.equals(offer.getOrder().getId(), orderId))
            throw new InvalidOfferSelectionException();

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (order.getCustomer().getCredit() < offer.getPrice())
            throw new NotEnoughCreditException();

        order.setSelectedOffer(offer);
        order.setStatus(OrderStatus.WAITING_ARRIVAL);

        return orderRepository.save(order);
    }

    @Transactional
    public void removeById(Long requestId) {
        orderRepository.deleteById(requestId);
    }
}
