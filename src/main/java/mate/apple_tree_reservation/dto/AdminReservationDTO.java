package mate.apple_tree_reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import mate.apple_tree_reservation.enums.MealType;
import mate.apple_tree_reservation.enums.ReservationType;

import java.time.LocalDate;
import java.time.LocalTime;

// 예약 DTO(어드민)
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
@Builder
public class AdminReservationDTO {

    private Long reservationId;

    @NotNull(message = "Elderly Name는 필수 항목입니다.")
    private String elderlyName;

    @NotNull(message = "Guardian relation은 필수 항목입니다.")
    private String guardianRelation;

    @Builder.Default
    private ReservationType reservationType = ReservationType.DEFAULT; // 기본값 설정

    @NotNull(message = "Reservation Date는 필수 항목입니다.")
    private LocalDate reservationDate;

    @NotNull(message = "Reservation Time은 필수 항목입니다.")
    private LocalTime reservationTime;

    @Builder.Default
    private MealType meal = MealType.DEFAULT; // 기본값 설정
}
