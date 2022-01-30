package ir.maktab.homeservice.controller;

import ir.maktab.homeservice.controller.dto.CustomerRegisterParam;
import ir.maktab.homeservice.controller.dto.ResponseTemplate;
import ir.maktab.homeservice.entities.orders.Order;
import ir.maktab.homeservice.entities.orders.OrderStatus;
import ir.maktab.homeservice.entities.users.Customer;
import ir.maktab.homeservice.services.CustomerService;
import ir.maktab.homeservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @PostMapping()
    public ResponseEntity<ResponseTemplate<Customer>> registerCustomer(@Valid @RequestBody CustomerRegisterParam input) {
        Customer customer = convertFromParam(input);
        Customer saved = customerService.save(customer);
        ResponseTemplate<Customer> result = ResponseTemplate.<Customer>builder()
                .message("customer registered successfully.")
                .code(201)
                .data(saved)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("#id==authentication.name or hasAuthority('can_get_customers')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTemplate<Customer>> getCustomer(@PathVariable String id) {
        Customer byId = customerService.findById(Long.valueOf(id));
        return ResponseEntity.ok().body(ResponseTemplate.<Customer>builder()
                .code(200)
                .message("ok")
                .data(byId)
                .build());
    }

    @PreAuthorize("#customerId==authentication.name or hasAuthority('can_get_customers')")
    @GetMapping("/{id}/requests")
    public ResponseEntity<ResponseTemplate<List<Order>>> getCustomersRequests(@PathVariable("id") String customerId, @RequestParam String status, Pageable pageable) {
        List<Order> requestOutputDTOS = orderService.findByCustomerId(Long.valueOf(customerId), OrderStatus.valueOf(status), pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<Order>>builder().code(200).message("ok").data(requestOutputDTOS).build());
    }

    public Customer convertFromParam(CustomerRegisterParam input) {
        Customer customer = new Customer();
        customer.setFirstName(input.getFirstName());
        customer.setLastName(input.getLastName());
        customer.setEmail(input.getEmail());
        customer.setPassword(input.getPassword());
        customer.setCredit(input.getCredit());

        return customer;
    }
}
