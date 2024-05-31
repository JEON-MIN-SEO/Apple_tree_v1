package mate.apple_tree_reservation.dto;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor //기본 생성자
@AllArgsConstructor //모든 필드를 매개변수로 하는 생성자
public class AvailableTimeDTO {
    private LocalTime time;
    private boolean available;
}
