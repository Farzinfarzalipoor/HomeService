package ir.maktab.homeservice.services;

import ir.maktab.homeservice.entities.users.Customer;
import ir.maktab.homeservice.entities.users.UserStatus;
import ir.maktab.homeservice.repositories.CustomerRepository;
import ir.maktab.homeservice.services.exceptions.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Transactional
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer getById(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Customer findById(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable).get().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return new ArrayList<>(customerRepository.findAll());
    }

    @Transactional
    public Customer changePassword(Long customerId, String password) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        customer.setPassword(password);
        customerRepository.save(customer);
        return customer;
    }

    @Transactional
    public Customer changeStatus(Long customerId, UserStatus status) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        customer.setStatus(status);
        customerRepository.save(customer);
        return customer;
    }

    @Transactional
    public Customer update(Long customerId, Customer customer) {
        Customer a_customer = customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        a_customer.setFirstName(customer.getFirstName());
        a_customer.setLastName(customer.getLastName());
        a_customer.setEmail(customer.getEmail());
        a_customer.setPassword(customer.getPassword());
        a_customer.setCredit(customer.getCredit());
        customerRepository.save(a_customer);
        return customer;
    }

    @Transactional
    public void removeById(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        customerRepository.delete(customer);
    }
}
