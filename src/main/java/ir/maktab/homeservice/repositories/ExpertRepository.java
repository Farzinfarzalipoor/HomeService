package ir.maktab.homeservice.repositories;


import ir.maktab.homeservice.entities.services.Service;
import ir.maktab.homeservice.entities.users.Expert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long> {
    public List<Expert> findAll();

    public List<Expert> findByFirstName(String firstName, Pageable pageable);

    public List<Expert> findByLastName(String lastName, Pageable pageable);

    public List<Expert> findByEmail(String email, Pageable pageable);

    @Query(value = "SELECT s FROM Expert s where :assistance MEMBER OF s.services ORDER BY s.id DESC")
    public List<Expert> findByServiceId(Service service, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Expert s SET s.points = (SELECT AVG (r.points ) FROM Order r WHERE r.selectedOffer.expert.id=s.id ) WHERE s.id=:expertId")
    public void updateExpertPoints(Long expertId);
}
