package mate.apple_tree_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mate.apple_tree_reservation.enums.MealType;

import java.time.LocalDate;
import java.time.LocalTime;

// 어드민 예약 조회 response DTO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {
    private int floor;
    private String elderlyName;
    private String guardianRelation;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private String meal;
}
