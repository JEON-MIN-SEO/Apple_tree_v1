package mate.apple_tree_reservation.dto;

import lombok.Getter;
import lombok.Setter;

// 어드민 예약 조회 DTO
@Getter
@Setter
public class DateRangeRequestDTO {
    private String start;
    private String end;
    private String reservationType;
}
