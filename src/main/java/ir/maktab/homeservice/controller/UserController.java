package ir.maktab.homeservice.controller;

import ir.maktab.homeservice.controller.dto.ResponseTemplate;
import ir.maktab.homeservice.entities.orders.Order;
import ir.maktab.homeservice.entities.users.User;
import ir.maktab.homeservice.services.OrderService;
import ir.maktab.homeservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAuthority('can_get_user_requests')")
    @GetMapping("/{id}/requests")
    public ResponseEntity<ResponseTemplate<List<Order>>> findUserRequests(@PathVariable Long id, Pageable pageable) {
        List<Order> userRequests = orderService.findByUserId(id, pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<Order>>builder()
                .code(200)
                .message("ok")
                .data(userRequests)
                .build());
    }

    @PreAuthorize("hasAuthority('can_get_users')")
    @GetMapping()
    public ResponseEntity<ResponseTemplate<List<User>>> findUserByParam(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> parameters = new HashMap<>();
        parameterMap.forEach((key, value) -> parameters.put(key, value[0]));
        List<User> userOutputDTOList = userService.findByParameters(parameters);
        return ResponseEntity.ok(ResponseTemplate.<List<User>>builder()
                .data(userOutputDTOList)
                .message("ok")
                .code(200)
                .build());
    }

    @PreAuthorize("hasAuthority('can_get_report')")
    @GetMapping("/report")
    public ResponseEntity<ResponseTemplate<List<User>>> getReport(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> parameters = new HashMap<>();
        parameterMap.forEach((key, value) -> parameters.put(key, value[0]));
        List<User> report = userService.getReportByParameters(parameters);
        return ResponseEntity.ok(ResponseTemplate.<List<User>>builder()
                .data(report)
                .message("ok")
                .code(200)
                .build());
    }
}
