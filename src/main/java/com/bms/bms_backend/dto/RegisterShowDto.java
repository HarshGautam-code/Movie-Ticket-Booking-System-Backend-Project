package com.bms.bms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterShowDto {

    private String showName;
    private LocalDateTime displayStartTime;
    private LocalDateTime displayEndTime;

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public LocalDateTime getDisplayStartTime() {
        return displayStartTime;
    }

    public void setDisplayStartTime(LocalDateTime displayStartTime) {
        this.displayStartTime = displayStartTime;
    }

    public LocalDateTime getDisplayEndTime() {
        return displayEndTime;
    }

    public void setDisplayEndTime(LocalDateTime displayEndTime) {
        this.displayEndTime = displayEndTime;
    }
}
