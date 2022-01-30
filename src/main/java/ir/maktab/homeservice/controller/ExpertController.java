package ir.maktab.homeservice.controller;

import ir.maktab.homeservice.controller.dto.AddServiceInputParam;
import ir.maktab.homeservice.controller.dto.ExpertRegisterParam;
import ir.maktab.homeservice.controller.dto.ResponseTemplate;
import ir.maktab.homeservice.entities.orders.Order;
import ir.maktab.homeservice.entities.orders.OrderStatus;
import ir.maktab.homeservice.entities.users.Expert;
import ir.maktab.homeservice.services.ExpertService;
import ir.maktab.homeservice.services.OrderService;
import ir.maktab.homeservice.services.exceptions.FileNotFoundException;
import ir.maktab.homeservice.services.exceptions.InvalidFileFormatException;
import ir.maktab.homeservice.services.exceptions.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/specialists")
public class ExpertController {
    @Autowired
    private ExpertService expertService;

    @Autowired
    private OrderService orderService;

    @Value("${uploaddir}")
    private String uploadDir;

    private Path fileStorageLocation;

    @PostConstruct
    public void init() {
        fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("could not create directory.");
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseTemplate<Expert>> registerSpecialist(@Valid @RequestBody ExpertRegisterParam input) {
        Expert expert = convertFromParam(input);
        Expert saved = expertService.save(expert);
        ResponseTemplate<Expert> result = ResponseTemplate.<Expert>builder()
                .message("specialist registered successfully.")
                .code(201)
                .data(saved)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("#id==authentication.name or hasAuthority('can_get_specialists')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTemplate<Expert>> getSpecialist(@PathVariable String id) {
        Expert byId = expertService.findById(Long.valueOf(id));
        return ResponseEntity.ok().body(ResponseTemplate.<Expert>builder()
                .code(200)
                .message("ok")
                .data(byId)
                .build());
    }

    @PreAuthorize("#id==authentication.name or hasAuthority('can_assign_assistance')")
    @PostMapping("/{id}/assistances")
    public ResponseEntity<ResponseTemplate<Object>> addAssistanceToSpecialist(@Valid @RequestBody AddServiceInputParam inputParam, @PathVariable String id) {
        expertService.addAssistance(Long.valueOf(id), inputParam.getServiceId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseTemplate.builder().code(201).message("assistance added successfully.").build());
    }

    @PreAuthorize("#specialistId==authentication.name")
    @GetMapping("/{id}/relevantrequests")
    public ResponseEntity<ResponseTemplate<List<Order>>> getRevelentRequests(@PathVariable("id") String specialistId, Pageable pageable) {
        List<Order> requestOutputDTOS = orderService.findForExpert(Long.valueOf(specialistId), pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<Order>>builder().code(200).message("ok").data(requestOutputDTOS).build());
    }

    @PreAuthorize("#specialistId==authentication.name or hasAuthority('can_get_specialists')")
    @GetMapping("/{id}/requests")
    public ResponseEntity<ResponseTemplate<List<Order>>> getSpecialistRequests(@PathVariable("id") String specialistId, @RequestParam String status, Pageable pageable) {
        List<Order> requestOutputDTOS = orderService.findByExpertId(Long.valueOf(specialistId), OrderStatus.valueOf(status), pageable);
        return ResponseEntity.ok(ResponseTemplate.<List<Order>>builder().code(200).message("ok").data(requestOutputDTOS).build());
    }

    @PostMapping("/photos")
    public ResponseEntity<ResponseTemplate<String>> uploadPhoto(@RequestParam("file") MultipartFile file) {
        List<String> allowedTypes = Arrays.asList(".png", ".jpg");
        String originalFilename = file.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!allowedTypes.contains(postfix.toLowerCase()))
            throw new InvalidFileFormatException();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser"))
            throw new UnauthenticatedException();
        Long id = Long.valueOf(authentication.getName());
        Expert expert = expertService.findById(id);
        if (expert.getPhotoURL() != null) {
            removeFile(expert.getPhotoURL());
        }
        Path targetLocation = Paths.get(uploadDir + authentication.getName() + postfix);
        System.out.println("created : " + targetLocation);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString()
                + "/specialists/photos/" + authentication.getName() + postfix;
        System.out.println(url);
        expertService.changePhotoUrl(id, url);
        return ResponseEntity.created(URI.create(url)).body(ResponseTemplate.<String>builder()
                .code(201).message("photo uploaded successfully.").data(url).build());
    }

    @GetMapping("/photos/{file}")
    public ResponseEntity<Resource> getPhoto(@PathVariable("file") String file) {
        try {
            Path filePath = Paths.get(uploadDir + file);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new FileNotFoundException();
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException();
        }
    }

    private void removeFile(String photoURL) {
        String fileName = photoURL.substring(photoURL.lastIndexOf('/'));
        Path path = Paths.get(uploadDir + fileName);
        System.out.println("deleted : " + path);
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Expert convertFromParam(ExpertRegisterParam input) {
        Expert expert = new Expert();

        expert.setFirstName(input.getFirstName());
        expert.setLastName(input.getLastName());
        expert.setEmail(input.getLastName());
        expert.setPassword(input.getPassword());
        expert.setCredit(input.getCredit());

        return expert;
    }
}
