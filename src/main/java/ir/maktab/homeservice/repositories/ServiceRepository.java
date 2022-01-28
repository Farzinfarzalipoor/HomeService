package ir.maktab.homeservice.repositories;

import ir.maktab.homeservice.entities.services.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    public List<Service> findAll();
}
