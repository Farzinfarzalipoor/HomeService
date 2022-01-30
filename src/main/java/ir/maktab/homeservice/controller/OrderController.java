package ir.maktab.homeservice.controller;

import ir.maktab.homeservice.controller.dto.*;
import ir.maktab.homeservice.entities.EvaluationInput;
import ir.maktab.homeservice.entities.Offer;
import ir.maktab.homeservice.entities.orders.Order;
import ir.maktab.homeservice.services.OfferService;
import ir.maktab.homeservice.services.OrderService;
import ir.maktab.homeservice.services.exceptions.IllegalParameterException;
import ir.maktab.homeservice.services.exceptions.UnauthorizedCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/requests")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OfferService offerService;

    @PreAuthorize("hasAuthority('can_add_request')")
    @PostMapping
    public ResponseEntity<ResponseTemplate<Order>> addRequest(@Valid @RequestBody OrderInputParam inputParam) {
        Order dto = convertFromParam(inputParam);
        Order saved = orderService.save(dto);
        ResponseTemplate<Order> result = ResponseTemplate.<Order>builder()
                .message("request registered successfully.")
                .code(201)
                .data(saved)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasAuthority('can_get_requests_by_parameter')")
    @GetMapping
    public ResponseEntity<ResponseTemplate<List<Order>>> getRequests(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> parameters = new HashMap<>();
        parameterMap.entrySet().forEach(entry -> parameters.put(entry.getKey(), entry.getValue()[0]));
        List<Order> requests = orderService.findByParameterMap(parameters);
        return ResponseEntity.ok().body(ResponseTemplate.<List<Order>>builder()
                .data(requests)
                .code(200)
                .message("ok")
                .build());
    }

    @PreAuthorize("hasAuthority('can_evaluate')")
    @PostMapping("/{id}/evaluate")
    public ResponseEntity<ResponseTemplate<Order>> addEvaluation(@PathVariable("id") Long requestId, EvaluationInputParam inputParam) {
        authorize(requestId);
        EvaluationInput dto = EvaluationInput.builder().comment(inputParam.getComment()).points(inputParam.getPoints()).build();
        Order requestOutputDTO = orderService.evaluate(requestId, dto);
        return ResponseEntity.ok().body(ResponseTemplate.<Order>builder()
                .message("evaluation added successfully.")
                .data(requestOutputDTO)
                .code(200)
                .build());
    }

    @PreAuthorize("hasAuthority('can_add_offers')")
    @PostMapping("/{id}/offers")
    public ResponseEntity<ResponseTemplate<Offer>> addOfferToRequest(@PathVariable("id") Long requestId, OfferInputParam inputParam) {
        Offer dto = convertFromParam(inputParam);
        Offer saved = offerService.save(requestId, dto);
        ResponseTemplate<Offer> result = ResponseTemplate.<Offer>builder()
                .message("request registered successfully.")
                .code(201)
                .data(saved)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasAuthority('can_get_offers')")
    @GetMapping("/{id}/offers")
    public ResponseEntity<ResponseTemplate<List<Offer>>> getOffers(@PathVariable("id") Long requestId, @RequestParam String orderby, Pageable pageable) {
        authorize(requestId);
        List<Offer> offers;
        switch (orderby) {
            case "pointdesc":
                offers = offerService.findByRequestIdOrderByPointsDesc(requestId, pageable);
                break;
            case "priceasc":
                offers = offerService.findByRequestIdOrderByPriceAsc(requestId, pageable);
                break;
            default:
                throw new IllegalParameterException();
        }
        return ResponseEntity.ok(ResponseTemplate.<List<Offer>>builder()
                .data(offers)
                .code(200)
                .message("ok")
                .build());
    }

    @PreAuthorize("hasAuthority('can_select_offer')")
    @PostMapping("/{id}/selectoffer")
    public ResponseEntity<ResponseTemplate<Order>> selectOffer(@PathVariable("id") Long requestId, SelectOfferParam param) {

        Order requestOutputDTO = orderService.selectOffer(requestId, param.getOfferId());
        return ResponseEntity.ok(ResponseTemplate.<Order>builder()
                .code(200)
                .message("offer selected successfully.")
                .data(requestOutputDTO)
                .build());
    }

    @PreAuthorize("hasAuthority('can_pay_request')")
    @PostMapping("/{id}/pay")
    public ResponseEntity<ResponseTemplate<Object>> payRequest(@PathVariable("id") Long requestId) {
        authorize(requestId);
        orderService.pay(requestId);
        return ResponseEntity.ok().body(ResponseTemplate.builder()
                .message("payment was successful.")
                .code(200)
                .build());
    }

    private void authorize(Long requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long customerId = Long.valueOf(authentication.getName());
        Order request = orderService.findById(requestId);
        if (!request.getCustomerId().equals(customerId))
            throw new UnauthorizedCustomerException();
    }

    private Offer convertFromParam(OfferInputParam inputParam) {
        Offer offer = new Offer();
        offer.setStartDate(inputParam.getBeginning());
        offer.setExecutionPeriod(inputParam.getExecutionPeriod());
        offer.setPrice(inputParam.getPrice());
        offer.setExpertId(inputParam.getSpecialistId());
        return offer;

    }

    private Order convertFromParam(OrderInputParam inputParam) {
        Order order = new Order();

        order.setCustomerId(inputParam.getCustomerId());
        order.setSubServiceId(inputParam.getSubAssistanceId());
        order.setAddress(inputParam.getAddress());
        order.setOfferedPrice(inputParam.getOfferedPrice());
        order.setDescription(inputParam.getDescription());
        order.setExecutionDate(inputParam.getExecutionDate());
        return order;
    }
}
