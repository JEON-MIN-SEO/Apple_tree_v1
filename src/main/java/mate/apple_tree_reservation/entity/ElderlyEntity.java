package mate.apple_tree_reservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "elderly_table")
@NoArgsConstructor //모든 필드를 받아야 함으로 기본 생성자는 필요하지 않다고 판단
@AllArgsConstructor //모든 필드를 매개변수로 하는 생성자
public class ElderlyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long elderlyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int floor;
}
