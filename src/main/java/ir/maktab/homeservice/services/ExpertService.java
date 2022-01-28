package ir.maktab.homeservice.services;

import ir.maktab.homeservice.entities.services.Service;
import ir.maktab.homeservice.entities.users.Expert;
import ir.maktab.homeservice.entities.users.UserStatus;
import ir.maktab.homeservice.repositories.ExpertRepository;
import ir.maktab.homeservice.repositories.ServiceRepository;
import ir.maktab.homeservice.services.exceptions.ExpertNotFoundException;
import ir.maktab.homeservice.services.exceptions.ServiceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ExpertService {
    @Autowired
    ExpertRepository expertRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Transactional
    public Expert save(Expert expert) {
        return expertRepository.save(expert);
    }

    @Transactional(readOnly = true)
    public Expert getById(Long customerId) {
        return expertRepository.findById(customerId).orElseThrow(ExpertNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Expert findById(Long specialistId) {
        return expertRepository.findById(specialistId).orElseThrow(ExpertNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Expert> findAll(Pageable pageable) {
        return expertRepository.findAll(pageable).get().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Expert> findAll() {
        return new ArrayList<>(expertRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<Expert> findByFirstName(String firstName, Pageable pageable) {
        return new ArrayList<>(expertRepository.findByFirstName(firstName, pageable));
    }

    @Transactional(readOnly = true)
    public List<Expert> findByLastName(String lastName, Pageable pageable) {
        return new ArrayList<>(expertRepository.findByLastName(lastName, pageable));
    }

    @Transactional(readOnly = true)
    public List<Expert> findByEmail(String email, Pageable pageable) {
        return new ArrayList<>(expertRepository.findByEmail(email, pageable));
    }

    @Transactional(readOnly = true)
    public List<Expert> findByAssistanceId(Long serviceId, Pageable pageable) {
        Service assistance = serviceRepository.findById(serviceId).orElseThrow(ServiceNotFoundException::new);
        return new ArrayList<>(expertRepository.findByServiceId(assistance, pageable));
    }

    @Transactional
    public Expert changePassword(Long expertId, String password) {
        Expert expert = expertRepository.findById(expertId).orElseThrow(ExpertNotFoundException::new);
        expert.setPassword(password);
        expertRepository.save(expert);
        return expert;
    }

    @Transactional
    public Expert changeStatus(Long specialistId, UserStatus status) {
        Expert specialist = expertRepository.findById(specialistId).orElseThrow(ExpertNotFoundException::new);
        specialist.setStatus(status);
        expertRepository.save(specialist);
        return specialist;
    }

    @Transactional
    public Expert update(Long specialistId, Expert expert) {
        Expert anExpert = expertRepository.findById(specialistId).orElseThrow(ExpertNotFoundException::new);
        anExpert.setFirstName(expert.getFirstName());
        anExpert.setLastName(expert.getLastName());
        anExpert.setEmail(expert.getEmail());
        anExpert.setPassword(expert.getPassword());
        anExpert.setCredit(expert.getCredit());
        anExpert.setPhotoURL(expert.getPhotoURL());
        expertRepository.save(anExpert);
        return anExpert;
    }

    @Transactional
    public void addAssistance(Long specialistId, Long assistanceId) {
        Expert specialist = expertRepository.findById(specialistId).orElseThrow(ExpertNotFoundException::new);
        Service service = serviceRepository.findById(assistanceId).orElseThrow(ServiceNotFoundException::new);
        specialist.addService(service);
        expertRepository.save(specialist);
    }

    @Transactional
    public void removeAssistance(Long expertId, Long serviceId) {
        Expert expert = expertRepository.findById(expertId).orElseThrow(ExpertNotFoundException::new);
        Service assistance = serviceRepository.findById(serviceId).orElseThrow(ServiceNotFoundException::new);
        expert.removeService(assistance);
        expertRepository.save(expert);
    }

    @Transactional
    public void removeById(Long specialistId) {
        Expert expert = expertRepository.findById(specialistId).orElseThrow(ExpertNotFoundException::new);
        expertRepository.delete(expert);
    }
}
