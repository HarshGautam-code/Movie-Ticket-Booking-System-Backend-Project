package com.bms.bms_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "showPriceMappings")
public class ShowPriceMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Show show;

    @ManyToOne
    private Hall hall;

    @ManyToOne
    private HallRowMapping hallRowMapping;

    private Double price;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public HallRowMapping getHallRowMapping() {
        return hallRowMapping;
    }

    public void setHallRowMapping(HallRowMapping hallRowMapping) {
        this.hallRowMapping = hallRowMapping;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
