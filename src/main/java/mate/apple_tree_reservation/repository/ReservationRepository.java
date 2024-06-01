package mate.apple_tree_reservation.repository;

import mate.apple_tree_reservation.entity.ReservationEntity;
import mate.apple_tree_reservation.enums.ReservationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    //시작~끝 날짜의 모든 일정 조회
    List<ReservationEntity> findByReservationDateBetween(LocalDate startDate, LocalDate endDate);
    //특정 날짜와 특정 예약 타입의 해당하는 예약 모두 조회
    List<ReservationEntity> findByReservationDateAndReservationType(LocalDate date, ReservationType type);
    //특정 어르신의 예약 정보를 모두 조회
    List<ReservationEntity> findByElderlyId(Long elderlyId);
}