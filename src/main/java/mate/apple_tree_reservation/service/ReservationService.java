package mate.apple_tree_reservation.service;

import mate.apple_tree_reservation.dto.AvailableTimeDTO;
import mate.apple_tree_reservation.dto.ElderlyWithReservationsDTO;
import mate.apple_tree_reservation.dto.ReservationDTO;
import mate.apple_tree_reservation.dto.ReservationReturnDTO;
import mate.apple_tree_reservation.entity.ElderlyEntity;
import mate.apple_tree_reservation.entity.ReservationEntity;
import mate.apple_tree_reservation.enums.ReservationType;
import mate.apple_tree_reservation.exception.ResourceNotFoundException;
import mate.apple_tree_reservation.repository.ElderlyRepository;
import mate.apple_tree_reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ElderlyRepository elderlyRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ElderlyRepository elderlyRepository) {
        this.reservationRepository = reservationRepository;
        this.elderlyRepository = elderlyRepository;
    }

    // 면회 예약 가능 시간대
    private static final List<LocalTime> VISIT_TIMES = Arrays.asList(
            LocalTime.of(11, 30),
            LocalTime.of(12, 0),
            LocalTime.of(14, 0),
            LocalTime.of(15, 0),
            LocalTime.of(16, 0)
    );

    // 외출 예약 가능 시간대
    private static final List<LocalTime> OUTING_TIMES = Arrays.asList(
            LocalTime.of(11, 30),
            LocalTime.of(12, 0),
            LocalTime.of(14, 0),
            LocalTime.of(14, 30),
            LocalTime.of(15, 0),
            LocalTime.of(15, 30),
            LocalTime.of(16, 0)
    );

    //어드민 예약 생성
    @Transactional
    public void createReservation(ReservationDTO reservationDTO) {
        try {
            ReservationEntity reservationEntity = new ReservationEntity();
            reservationEntity.setElderlyId(reservationDTO.getElderlyId());
            reservationEntity.setGuardianRelation(reservationDTO.getGuardianRelation());
            reservationEntity.setReservationType(reservationDTO.getReservationType());
            reservationEntity.setReservationDate(reservationDTO.getReservationDate());
            reservationEntity.setReservationTime(reservationDTO.getReservationTime());
            reservationEntity.setMeal(reservationDTO.getMeal());
            reservationRepository.save(reservationEntity);
        } catch (Exception e) {
            throw new RuntimeException("예약 정보를 저장하는 중 오류가 발생했습니다.", e);
        }
    }

    //예약 수정 어르신 id는 변경 불가
    @Transactional
    public void updateReservation(Long reservationId, ReservationDTO reservationDTO) {
        try {
            ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new ResourceNotFoundException("해당 예약을 찾지 못했습니다 id: " + reservationId));
            reservationEntity.setElderlyId(reservationDTO.getElderlyId());// elderlyId는 고정으로 자동 입력
            reservationEntity.setGuardianRelation(reservationDTO.getGuardianRelation());
            reservationEntity.setReservationType(reservationDTO.getReservationType());
            reservationEntity.setReservationDate(reservationDTO.getReservationDate());
            reservationEntity.setReservationTime(reservationDTO.getReservationTime());
            reservationEntity.setMeal(reservationDTO.getMeal());
            reservationRepository.save(reservationEntity);
        } catch (Exception e) {
            throw new RuntimeException("예약 정보를 업데이트하는 중 오류가 발생했습니다.", e);
        }
    }

    //예약 삭제
    @Transactional
    public void deleteReservation(Long reservationId) {
        try {
            if (reservationRepository.existsById(reservationId)) {
                reservationRepository.deleteById(reservationId);
            } else {
                throw new ResourceNotFoundException("해당 예약을 찾지 못했습니다 id: " + reservationId);
            }
        } catch (Exception e) {
            throw new RuntimeException("예약 정보를 삭제하는 중 오류가 발생했습니다.", e);
        }
    }

    //====================
    //예약 가능한 날짜 반환 API
    @Transactional(readOnly = true)
    public List<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate, int displayMonth, Long elderlyId, String reservationType) {

        // String 타입의 reservationType을 ReservationType enum으로 변환
        ReservationType reservationTypeStr = ReservationType.valueOf(reservationType.toUpperCase());

        // 어르신 정보 가져오기
        ElderlyEntity elderlyEntity = elderlyRepository.findById(elderlyId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 어르신을 찾지 못했습니다 id: " + elderlyId));

        //어르신 거주하는 층 정보 가져오기
        int floor = elderlyEntity.getFloor();
        //층에 따라 예약 불가일 가져오기
        DayOfWeek restrictedDay = getRestrictedDay(floor);

        // 지정된 기간 내의 모든 예약 가져오기
        List<ReservationEntity> reservations = reservationRepository.findByReservationDateBetween(startDate, endDate);

        // 날짜별 예약 유형별 예약 수를 계산
        Map<LocalDate, Map<ReservationType, Long>> reservationCountByDateAndType = reservations.stream()
                .collect(Collectors.groupingBy(
                        ReservationEntity::getReservationDate,
                        Collectors.groupingBy(ReservationEntity::getReservationType, Collectors.counting())
                ));

        //예약 가능한 날짜 return 반환
        List<LocalDate> availableDates = new ArrayList<>();
        LocalDate currentDate = startDate;

        // 시작일과 종료일 사이의 날짜 중 표시할 월에 해당하는 날짜 필터링
        while (!currentDate.isAfter(endDate)) {
            if (currentDate.getMonthValue() == displayMonth && !currentDate.getDayOfWeek().equals(restrictedDay)) {
                if (isReservationAvailable(currentDate, reservationTypeStr, reservationCountByDateAndType)) {
                    availableDates.add(currentDate);
                }
            }
            currentDate = currentDate.plusDays(1);
        }

        return availableDates;
    }
    //====================

    // 층에 따라 예약이 불가한 요일 반환
    private DayOfWeek getRestrictedDay(int floor) {
        switch (floor) {
            case 1:
                return DayOfWeek.MONDAY;
            case 2:
                return DayOfWeek.TUESDAY;
            case 3:
                return DayOfWeek.WEDNESDAY;
            case 4:
                return DayOfWeek.THURSDAY;
            default:
                throw new IllegalArgumentException("유효하지 않은 층 수입니다.");
        }
    }


    // 특정 날짜와 예약 유형에 대해 예약 가능한지 확인
    private boolean isReservationAvailable(LocalDate date, ReservationType reservationType, Map<LocalDate, Map<ReservationType, Long>> reservationCountByDateAndType) {
        long count = reservationCountByDateAndType
                .getOrDefault(date, Map.of())
                .getOrDefault(reservationType, 0L);

        if (reservationType == ReservationType.VISIT) {
            return count < 5; // 면회는 하루에 5팀 제한
        } else if (reservationType == ReservationType.OUTING) {
            return count < 14; // 외출은 하루에 12팀 제한
        }
        return false;
    }

//    // 예약 가능한 시간 반환
//    public List<AvailableTimeDTO> getAvailableTimes(LocalDate date, ReservationType type) {
//        List<ReservationEntity> reservations = reservationRepository.findByReservationDateAndReservationType(date, type);
//
//        Map<LocalTime, Long> timeCountMap = reservations.stream()
//                .collect(Collectors.groupingBy(ReservationEntity::getReservationTime, Collectors.counting()));
//
//        List<AvailableTimeDTO> availableTimes = new ArrayList<>();
//        List<LocalTime> times = type == ReservationType.VISIT ? VISIT_TIMES : OUTING_TIMES;
//        for (LocalTime time : times) {
//            long count = timeCountMap.getOrDefault(time, 0L);
//            if (type == ReservationType.VISIT && count == 0) {
//                availableTimes.add(new AvailableTimeDTO(time, true));
//            } else if (type == ReservationType.OUTING && count < 2) {
//                availableTimes.add(new AvailableTimeDTO(time, true));
//            } else {
//                availableTimes.add(new AvailableTimeDTO(time, false));
//            }
//        }
//
//        return availableTimes;
//    }

    // 예약 가능한 시간 반환
    public List<AvailableTimeDTO> getAvailableTimes(LocalDate date, ReservationType type) {
        List<ReservationEntity> reservations = reservationRepository.findByReservationDateAndReservationType(date, type);

        Map<LocalTime, Long> timeCountMap = reservations.stream()
                .collect(Collectors.groupingBy(ReservationEntity::getReservationTime, Collectors.counting()));

        List<AvailableTimeDTO> availableTimes = new ArrayList<>();
        List<LocalTime> times = type == ReservationType.VISIT ? VISIT_TIMES : OUTING_TIMES;
        for (LocalTime time : times) {
            long count = timeCountMap.getOrDefault(time, 0L);
            if (type == ReservationType.VISIT && count == 0) {
                availableTimes.add(new AvailableTimeDTO(time, true));
            } else if (type == ReservationType.OUTING && count < 2) {
                availableTimes.add(new AvailableTimeDTO(time, true));
            } else {
                availableTimes.add(new AvailableTimeDTO(time, false));
            }
        }

        return availableTimes;
    }

    public List<ReservationReturnDTO> getReservationsByElderlyId(Long elderlyId) {
        return reservationRepository.findByElderlyId(elderlyId).stream()
                .map(reservation -> new ReservationReturnDTO(
                        reservation.getReservationId(),
                        reservation.getReservationType(),
                        reservation.getReservationDate(),
                        reservation.getReservationTime()
                ))
                .collect(Collectors.toList());
    }

    //어드민 일정 조회
    public List<ElderlyWithReservationsDTO> getReservationsWithinDateRange(LocalDate startDate, LocalDate endDate) {
        List<ReservationEntity> reservations = reservationRepository.findByReservationDateBetween(startDate, endDate);

        Map<Long, List<ReservationDTO>> reservationsByElderlyId = reservations.stream()
                .map(reservation -> new ReservationDTO(
                        reservation.getReservationId(),
                        reservation.getElderlyId(),
                        reservation.getGuardianRelation(),
                        reservation.getReservationType(),
                        reservation.getReservationDate(),
                        reservation.getReservationTime(),
                        reservation.getMeal()
                ))
                .collect(Collectors.groupingBy(ReservationDTO::getElderlyId));

        List<Long> elderlyIds = reservationsByElderlyId.keySet().stream().collect(Collectors.toList());
        List<ElderlyEntity> elderlyList = elderlyRepository.findAllById(elderlyIds);

        return elderlyList.stream()
                .map(elderly -> new ElderlyWithReservationsDTO(
                        elderly.getElderlyId(),
                        elderly.getName(),
                        elderly.getFloor(),
                        reservationsByElderlyId.get(elderly.getElderlyId())
                ))
                .collect(Collectors.toList());
    }
}