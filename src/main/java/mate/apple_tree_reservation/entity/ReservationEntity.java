package mate.apple_tree_reservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mate.apple_tree_reservation.enums.MealType;
import mate.apple_tree_reservation.enums.ReservationType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@EntityListeners(AuditingEntityListener.class) //생성, 수정 시간 기록
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation_table")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(nullable = false)
    private Long elderlyId;

    @Column(nullable = false)
    private String guardianRelation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationType reservationType = ReservationType.DEFAULT; //기본값 설정

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private LocalTime reservationTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealType meal = MealType.DEFAULT; //기본값 설정

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //자동 관리 DTO에 필요 없음
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    //자동 관리 DTO에 필요 없음
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
