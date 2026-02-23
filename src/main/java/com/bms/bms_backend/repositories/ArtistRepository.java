package com.bms.bms_backend.repositories;

import com.bms.bms_backend.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {

    // Select * from artist where name = xyz
    Artist findByName(String artistName);
}
