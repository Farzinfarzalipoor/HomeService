package ir.maktab.homeservice.repositories;

import ir.maktab.homeservice.entities.Offer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Long> , JpaSpecificationExecutor<Offer> {
    @Query("SELECT o FROM Offer o WHERE o.id =:offerId AND o.order.id=:orderID")
    public Optional<Offer> findByRequestIdAndOfferId(Long orderID, Long offerId);

    @Query("SELECT o FROM Offer o WHERE o.order.id=:orderID order by o.price")
    public List<Offer> findByOrderId(Long orderID);

    @Query("SELECT o FROM Offer o WHERE o.order.id=:orderID")
    public List<Offer> findByOrderId(Long orderID, Pageable pageable);
}