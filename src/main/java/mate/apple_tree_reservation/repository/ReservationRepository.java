package mate.apple_tree_reservation.repository;

import mate.apple_tree_reservation.entity.ReservationEntity;
import mate.apple_tree_reservation.enums.ReservationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByReservationDateBetween(LocalDate startDate, LocalDate endDate);
    List<ReservationEntity> findByReservationDateAndReservationType(LocalDate date, ReservationType type);
    List<ReservationEntity> findByElderlyId(Long elderlyId);
}