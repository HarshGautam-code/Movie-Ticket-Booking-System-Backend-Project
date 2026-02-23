package com.bms.bms_backend.dto;

import com.bms.bms_backend.models.Hall;
import com.bms.bms_backend.models.HallRowMapping;
import com.bms.bms_backend.models.Show;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterShowPriceMappingDto {

    private UUID showId;
    private UUID hallId;
    private UUID hallRowMappingId;
    private Double price;

    public UUID getShowId() {
        return showId;
    }

    public void setShowId(UUID showId) {
        this.showId = showId;
    }

    public UUID getHallId() {
        return hallId;
    }

    public void setHallId(UUID hallId) {
        this.hallId = hallId;
    }

    public UUID getHallRowMappingId() {
        return hallRowMappingId;
    }

    public void setHallRowMappingId(UUID hallRowMappingId) {
        this.hallRowMappingId = hallRowMappingId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
