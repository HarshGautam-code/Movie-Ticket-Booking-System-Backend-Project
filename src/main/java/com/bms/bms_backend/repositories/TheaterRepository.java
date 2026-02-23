package com.bms.bms_backend.repositories;

import com.bms.bms_backend.models.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface TheaterRepository  extends JpaRepository<Theater, UUID> {


}
