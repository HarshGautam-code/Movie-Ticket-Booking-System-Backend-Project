package com.bms.bms_backend.repositories;

import com.bms.bms_backend.models.Hall;
import com.bms.bms_backend.models.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShowRepository extends JpaRepository<Show, UUID> {

    public List<Show> findByHall(Hall hall);
}
