package ir.maktab.homeservice.services;

import ir.maktab.homeservice.entities.EvaluationInput;
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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OfferRepository offerRepository;

    @Autowired
    ExpertRepository expertRepository;

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
                .findById(order.getSubService().getId()).orElseThrow(SubServiceNotFoundException::new);
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
    public Order pay(Long requestId){
        Order request = orderRepository.findById(requestId).orElseThrow(() -> new OrderNotFoundException());
        Offer selectedOffer = request.getSelectedOffer();
        Expert expert = selectedOffer.getExpert();
        Customer customer = request.getCustomer();
        Double price = selectedOffer.getPrice();
        if (request.getStatus()!= OrderStatus.DONE )
            throw new InvalidOrderStatusException();
        if (customer.getCredit() < price)
            throw new OrderSettlementException("customer credit is not enough");
        expert.setCredit(expert.getCredit() + price*.7);
        customer.setCredit(customer.getCredit() - price);
        request.setStatus(OrderStatus.PAID);
        customerRepository.save(customer);
        expertRepository.save(expert);
        Order saved = orderRepository.save(request);
        return saved;
    }
    @Transactional(readOnly = true)
    public List<Order> findForExpert(Long expertId, Pageable pageable){
        return new ArrayList<>(orderRepository.findForExpert(expertId, pageable));
    }

    @Transactional(readOnly = true)
    public List<Order> findByExpertId(Long expertId , OrderStatus status , Pageable pageable){
        return orderRepository.findBySpecialistId(expertId , status , pageable).stream().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Order> findByUserId(Long userId , Pageable pageable){
        return orderRepository.findByUserId(userId, pageable, OrderStatus.DONE , OrderStatus.PAID).stream().collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<Order> findByParameterMap(Map<String, String> parameterMap) {
        return orderRepository.findByParameterMap(parameterMap).stream().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Order> findByCustomerId(Long customerId, OrderStatus status, Pageable pageable) {
        return orderRepository.findByCustomerId(customerId, status, pageable).stream().collect(Collectors.toList());
    }


    @Transactional
    public Order evaluate(Long requestId, EvaluationInput inputDTO) {
        Order request = orderRepository.findById(requestId).orElseThrow(() -> new OrderNotFoundException());
        if (request.getStatus() != OrderStatus.DONE && request.getStatus() != OrderStatus.PAID)
            throw new InvalidOrderEvaluationException();
        Offer selectedOffer = request.getSelectedOffer();
        Expert specialist = selectedOffer.getExpert();
        request.setComment(inputDTO.getComment());
        request.setPoints(inputDTO.getPoints());
        Order saved = orderRepository.save(request);
        expertRepository.updateExpertPoints(specialist.getId());
        return saved;
    }


    @Transactional
    public void removeById(Long requestId) {
        orderRepository.deleteById(requestId);
    }
}
