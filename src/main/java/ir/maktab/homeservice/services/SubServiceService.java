package ir.maktab.homeservice.services;

import ir.maktab.homeservice.entities.services.Service;
import ir.maktab.homeservice.entities.services.SubService;
import ir.maktab.homeservice.repositories.ServiceRepository;
import ir.maktab.homeservice.repositories.SubServiceRepository;
import ir.maktab.homeservice.services.exceptions.ServiceNotFoundException;
import ir.maktab.homeservice.services.exceptions.subServiceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class SubServiceService {
    @Autowired
    SubServiceRepository subServiceRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Transactional
    public SubService save(Long serviceId, SubService subService) {
        Optional<Service> optional = serviceRepository.findById(serviceId);
        Service service = optional.orElseThrow(ServiceNotFoundException::new);
        subService.setService(service);
        return subServiceRepository.save(subService);
    }

    @Transactional(readOnly = true)
    public SubService findById(Long serviceId, Long subServiceId) {
        SubService subAssistance = subServiceRepository
                .findByAssistanceIdAndSubAssistanceId(serviceId, subServiceId)
                .orElseThrow(subServiceNotFoundException::new);
        return subAssistance;
    }

    @Transactional(readOnly = true)
    public List<SubService> findAll(Long serviceId) {
        List<SubService> srbServices = subServiceRepository.findByAssistanceId(serviceId);
        return new ArrayList<>(srbServices);
    }

    @Transactional(readOnly = true)
    public List<SubService> findAll(Long assistanceId, Pageable pageable) {
        Page<SubService> subServices = subServiceRepository.findByAssistanceId(assistanceId, pageable);
        return subServices.get().collect(Collectors.toList());
    }

    @Transactional
    public SubService update(Long serviceId, Long subServiceId, SubService subService) {
        SubService a_subService = subServiceRepository
                .findByAssistanceIdAndSubAssistanceId(serviceId, subServiceId)
                .orElseThrow(subServiceNotFoundException::new);
        a_subService.setTitle(subService.getTitle());
        a_subService.setDescription(subService.getDescription());
        a_subService.setBasePrice(subService.getBasePrice());
        return subServiceRepository.save(a_subService);
    }

    @Transactional
    public void removeById(Long serviceId, Long subServiceId) {
        SubService subAssistance = subServiceRepository
                .findByAssistanceIdAndSubAssistanceId(serviceId, subServiceId)
                .orElseThrow(subServiceNotFoundException::new);
        subServiceRepository.deleteById(subAssistance.getId());
    }
}
