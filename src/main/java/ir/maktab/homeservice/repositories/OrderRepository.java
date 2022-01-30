package ir.maktab.homeservice.repositories;

import ir.maktab.homeservice.entities.orders.Order;
import ir.maktab.homeservice.entities.orders.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {
    public List<Order> findAll();

    @Query("SELECT r FROM Order r WHERE EXISTS ( SELECT s FROM Expert s WHERE r.subService.service MEMBER OF s.services AND s.id=:specialistId) ORDER BY r.registerDate DESC ")
    public List<Order> findForExpert(Long expertId, Pageable pageable);

    @Query("SELECT r FROM Order r WHERE r.customer.id=:customerId AND r.status =:status ORDER BY r.registerDate DESC ")
    public List<Order> findByCustomerId(Long customerId, OrderStatus status, Pageable pageable);

    @Query("SELECT r FROM Order r WHERE r.selectedOffer.expert.id=:expertID AND r.status =:status ORDER BY r.registerDate DESC ")
    public List<Order> findBySpecialistId(Long expertID, OrderStatus status, Pageable pageable);

    @Query("SELECT r FROM Order r WHERE ( r.status =:status1 OR r.status =:status2 ) AND (r.selectedOffer.expert.id=:userId OR r.customer.id =:userId) ORDER BY r.registerDate DESC ")
    public List<Order> findByUserId(Long userId, Pageable pageable, OrderStatus status1, OrderStatus status2);
}
