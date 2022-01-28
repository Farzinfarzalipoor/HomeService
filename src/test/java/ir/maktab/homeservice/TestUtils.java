package ir.maktab.homeservice;

import ir.maktab.homeservice.entities.orders.Order;
import ir.maktab.homeservice.entities.services.Service;
import ir.maktab.homeservice.entities.services.SubService;
import ir.maktab.homeservice.entities.users.Customer;
import ir.maktab.homeservice.entities.users.Expert;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {
    public Customer getCustomer1() {
        Customer customer = new Customer();
        customer.setFirstName("ali3");
        customer.setLastName("rezaeig");
        customer.setEmail("ali@rezaegi.ir");
        customer.setPassword("123456s78a");
        customer.setCredit(100000d);
        return customer;

    }

    public Customer getCustomer2() {
        Customer customer = new Customer();
        customer.setFirstName("ali2");
        customer.setLastName("rezaeiw");
        customer.setEmail("ali@rezaei.ir");
        customer.setPassword("12345678a");
        customer.setCredit(100000d);
        return customer;

    }

    public Customer getCustomer3() {
        Customer customer = new Customer();
        customer.setFirstName("ali3");
        customer.setLastName("rezaeiw");
        customer.setEmail("ali@rezaeei.ir");
        customer.setPassword("123456e78a");
        customer.setCredit(100000d);
        return customer;

    }

    public Service getService() {
        Service service = new Service();
        service.setTitle("title1");
        return service;
    }


    public Service getService2() {
        Service service = new Service();
        service.setTitle("title3");
        return service;
    }

    public Expert getExpert() {
        Expert expert = new Expert();
        expert.setFirstName("gholam");
        expert.setLastName("alavi");
        expert.setEmail("g.alavi@gmail.com");
        expert.setPassword("ghalghal323");
        expert.setCredit(100d);
        return expert;
    }

    public void testEquality(Customer inputCustomer, Customer outputCustomer) {
        assertEquals(inputCustomer.getFirstName(), outputCustomer.getFirstName());
        assertEquals(inputCustomer.getLastName(), outputCustomer.getLastName());
        assertEquals(inputCustomer.getEmail(), outputCustomer.getEmail());
        assertEquals(inputCustomer.getCredit(), outputCustomer.getCredit());
    }

    public void testEquality(Expert target, Expert other) {
        assertEquals(target.getId(), other.getId());
        assertEquals(target.getFirstName(), other.getFirstName());
        assertEquals(target.getLastName(), other.getLastName());
        assertEquals(target.getEmail(), other.getEmail());
        assertEquals(target.getStatus(), other.getStatus());
        assertEquals(target.getPhotoURL(), other.getPhotoURL());
        assertEquals(target.getCredit(), other.getCredit());
        assertEquals(target.getPoints(), other.getPoints());
    }

    public SubService getSubService() {
        SubService subService = new SubService();
        subService.setBasePrice(10d);
        subService.setDescription("some description 1");
        subService.setTitle("some title 1");
        return subService;
    }

    public SubService getSubService2() {
        SubService subService = new SubService();
        subService.setBasePrice(103d);
        subService.setDescription("some deggscription 1");
        subService.setTitle("some tifftle 1");
        return subService;
    }

    public void testEquality(SubService target, SubService other) {
        assertEquals(target.getTitle(), other.getTitle());
        assertEquals(target.getDescription(), other.getDescription());
        assertEquals(target.getBasePrice(), other.getBasePrice());
    }

    public Order getOrder(Long subServiceId, Long customerId) {
        Order order = new Order();
        order.setExecutionDate(new Date(2022, 11, 11, 15, 30));
        order.setAddress("tehran valiasr");
        order.setDescription("some job you can't refuse");
        order.setOfferedPrice(200000d);
        return order;
    }
}
