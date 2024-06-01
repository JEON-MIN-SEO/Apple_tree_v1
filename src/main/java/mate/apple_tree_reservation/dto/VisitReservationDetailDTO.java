package mate.apple_tree_reservation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

//어드민 면회 상세 예약 정보 DTO
@Getter
@Setter
public class VisitReservationDetailDTO {
    private Long elderlyId;

    private String name;

    private Integer floor;

    private Long reservationId;

    private String guardianRelation;

    private LocalDate reservationDate;

    private LocalTime reservationTime;
}
