package ir.maktab.homeservice.repositories;

import ir.maktab.homeservice.entities.services.SubService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubServiceRepository extends JpaRepository<SubService, Long> {

    @Query("SELECT s FROM SubService s WHERE s.id=:subServiceId AND s.service.id = :serviceId")
    public Optional<SubService> findByAssistanceIdAndSubAssistanceId(Long serviceId, Long subServiceId);

    @Query("SELECT s FROM SubService s WHERE s.service.id = :serviceId")
    public List<SubService> findByAssistanceId(Long serviceId);

    @Query("SELECT s FROM SubService s WHERE s.service.id = :serviceIdo")
    public Page<SubService> findByAssistanceId(Long serviceId, Pageable pageable);
}