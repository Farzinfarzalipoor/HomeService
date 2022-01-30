package ir.maktab.homeservice.controller;


import ir.maktab.homeservice.controller.dto.ResponseTemplate;
import ir.maktab.homeservice.controller.dto.ServiceInputParam;
import ir.maktab.homeservice.controller.dto.SubServiceInputParam;
import ir.maktab.homeservice.entities.services.Service;
import ir.maktab.homeservice.entities.services.SubService;
import ir.maktab.homeservice.services.ServiceService;
import ir.maktab.homeservice.services.SubServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/assistances")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @Autowired
    private SubServiceService subServiceService;

    @PreAuthorize("hasAuthority('can_add_assistance')")
    @PostMapping
    public ResponseEntity<ResponseTemplate<Service>> addAssistance(@Valid @RequestBody ServiceInputParam inputParam) {
        Service service = new Service();
        service.setTitle(inputParam.getTitle());
        Service saved = serviceService.save(service);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTemplate.<Service>builder()
                        .code(201)
                        .message("assistance created successfully.")
                        .data(saved).build());
    }

    @GetMapping
    public ResponseEntity<ResponseTemplate<List<Service>>> getAssistances(Pageable pageable) {
        List<Service> retrieved = serviceService.findAll(pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<Service>>builder()
                .code(200)
                .message("ok")
                .data(retrieved)
                .build());
    }

    @PreAuthorize("hasAuthority('can_add_subassistance')")
    @PostMapping("/{id}/subassistances")
    public ResponseEntity<ResponseTemplate<SubService>> addSubAssistance
            (@PathVariable Long id, @Valid @RequestBody SubServiceInputParam inputParam) {
        SubService subService = new SubService();
        subService.setBasePrice(inputParam.getBasePrice());
        subService.setDescription(inputParam.getDescription());
        subService.setTitle(inputParam.getTitle());

        SubService saved = subServiceService.save(id, subService);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTemplate.<SubService>builder()
                        .code(201)
                        .message("subassistance created successfully.")
                        .data(saved).build());
    }

    @GetMapping("/{id}/subassistances")
    public ResponseEntity<ResponseTemplate<List<SubService>>> getSubAssistances(@PathVariable(name = "id") Long assistanceId, Pageable pageable) {
        List<SubService> retrieved = subServiceService.findAll(assistanceId, pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<SubService>>builder()
                .code(200)
                .message("ok")
                .data(retrieved)
                .build());
    }
}
