package mate.apple_tree_reservation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor //기본 생성자
@AllArgsConstructor //모든 필드를 매개변수로 하는 생성자
public class ElderlyDTO {

    private Long elderlyId;

    @NotNull(message = "이름은 필수 항목입니다.")
    @Size(min = 1, max = 3, message = "이름은 1자 이상 3자 이하로 입력해야 합니다.")
    private String name;

    @NotNull(message = "층 수는 필수 항목입니다.")
    @Min(value = 1, message = "층 수는 최소 1이어야 합니다.")
    @Max(value = 4, message = "층 수는 최대 4이어야 합니다.")
    private Integer floor;
}