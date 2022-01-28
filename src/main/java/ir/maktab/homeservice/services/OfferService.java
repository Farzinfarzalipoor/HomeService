package ir.maktab.homeservice.services;

import ir.maktab.homeservice.entities.Offer;
import ir.maktab.homeservice.entities.orders.Order;
import ir.maktab.homeservice.entities.orders.OrderStatus;
import ir.maktab.homeservice.entities.users.Expert;
import ir.maktab.homeservice.repositories.ExpertRepository;
import ir.maktab.homeservice.repositories.OfferRepository;
import ir.maktab.homeservice.repositories.OrderRepository;
import ir.maktab.homeservice.repositories.SubServiceRepository;
import ir.maktab.homeservice.services.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OfferService {
    @Autowired
    OfferRepository offerRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ExpertRepository expertRepository;

    @Autowired
    SubServiceRepository subServiceRepository;

    @Transactional
    public Offer save(Long orderID, Offer offer) {
        Order order = orderRepository.findById(orderID).orElseThrow(OrderNotFoundException::new);

        if (offer.getPrice() < order.getSubService().getBasePrice())
            throw new LowBasePriceException();

        offer.setOrder(order);
        order.setStatus(OrderStatus.WAITING_FOR_SELECT);
        Offer saved = offerRepository.save(offer);
        orderRepository.save(order);
        return saved;
    }

    @Transactional(readOnly = true)
    public Offer findByRequestIdAndOfferId(Long requestId, Long offerId) {
        return offerRepository.findByRequestIdAndOfferId(requestId, offerId).orElseThrow(() -> new OfferNotFoundException());
    }

    @Transactional(readOnly = true)
    public List<Offer> findByOrderId(Long orderId) {
        List<Offer> offers = offerRepository.findByOrderId(orderId);

        return new ArrayList<>(offers);
    }


    @Transactional
    public Offer update(Long requestId, Long offerId, Offer offer) {
        Offer a_offer = offerRepository.findByRequestIdAndOfferId(requestId, offerId).orElseThrow(OfferNotFoundException::new);
        Expert expert = expertRepository.findById(offer.getExpert().getId())
                .orElseThrow(ExpertNotFoundException::new);
        a_offer.setExpert(expert);
        a_offer.setPrice(offer.getPrice());
        a_offer.setExecutionPeriod(offer.getExecutionPeriod());
        a_offer.setStartDate(offer.getStartDate());
        return offerRepository.save(a_offer);
    }

    @Transactional
    public void removeById(Long requestId, Long offerId) {
        Offer offer = offerRepository.findByRequestIdAndOfferId(requestId, offerId).orElseThrow(OfferNotFoundException::new);
        //if (request.getStatus()==) some policy here
        offerRepository.delete(offer);
    }

}
