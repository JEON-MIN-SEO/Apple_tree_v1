package mate.apple_tree_reservation.dto;

import lombok.Getter;
import lombok.Setter;
import mate.apple_tree_reservation.enums.MealType;

//어드민 외출 상세 예약 정보 DTO + 면회 상속 예약 정보 DTO 상속
@Getter
@Setter
public class OutingReservationDetailDTO extends VisitReservationDetailDTO{
    private MealType meal = MealType.DEFAULT;
}
