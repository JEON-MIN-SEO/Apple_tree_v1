package mate.apple_tree_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mate.apple_tree_reservation.enums.ReservationType;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationReturnDTO {
    private Long reservationId;
    private ReservationType reservationType;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
}