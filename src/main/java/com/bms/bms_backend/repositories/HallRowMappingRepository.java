package com.bms.bms_backend.repositories;

import com.bms.bms_backend.models.Hall;
import com.bms.bms_backend.models.HallRowMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HallRowMappingRepository extends JpaRepository<HallRowMapping, UUID> {

    List<HallRowMapping> findByHall(Hall hall);
}
