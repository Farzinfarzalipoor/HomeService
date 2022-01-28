package ir.maktab.homeservice.services;

import ir.maktab.homeservice.entities.services.Service;
import ir.maktab.homeservice.repositories.ExpertRepository;
import ir.maktab.homeservice.repositories.ServiceRepository;
import ir.maktab.homeservice.services.exceptions.ExpertNotFoundException;
import ir.maktab.homeservice.services.exceptions.ServiceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ExpertRepository expertRepository;

    @Transactional
    public Service save(Service service) {
        return serviceRepository.save(service);
    }

    @Transactional(readOnly = true)
    public Service findById(Long id) {
        Optional<Service> optional = serviceRepository.findById(id);
        return optional.orElseThrow(ServiceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Service> findBySpecialistId(Long expertId) {
        return new ArrayList<>(expertRepository.findById(expertId).orElseThrow(ExpertNotFoundException::new)
                .getServices());
    }

    @Transactional(readOnly = true)
    public List<Service> findAll() {
        List<Service> all = serviceRepository.findAll();
        return new ArrayList<>(all);
    }

    @Transactional(readOnly = true)
    public List<Service> findAll(Pageable pageable) {
        return serviceRepository.findAll(pageable).get().collect(Collectors.toList());
    }

    @Transactional
    public Service update(Long id, Service service) {
        Optional<Service> optional = serviceRepository.findById(id);
        Service assistance = optional.orElseThrow(ServiceNotFoundException::new);
        assistance.setTitle(service.getTitle());
        return serviceRepository.save(assistance);
    }

    @Transactional
    public void removeById(Long assistanceId) {
        serviceRepository.deleteById(assistanceId);
    }

}
