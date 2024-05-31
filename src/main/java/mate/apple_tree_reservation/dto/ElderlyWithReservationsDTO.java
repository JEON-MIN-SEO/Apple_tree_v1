package mate.apple_tree_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElderlyWithReservationsDTO {
    private Long elderlyId;
    private String name;
    private Integer floor;
    private List<ReservationDTO> reservations;
}