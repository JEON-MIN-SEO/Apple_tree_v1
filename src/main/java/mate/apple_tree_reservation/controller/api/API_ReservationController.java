package mate.apple_tree_reservation.controller.api;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mate.apple_tree_reservation.dto.*;
import mate.apple_tree_reservation.enums.ReservationType;
import mate.apple_tree_reservation.service.ElderlyService;
import mate.apple_tree_reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/elderly/reservation")
public class API_ReservationController {

    private final ReservationService reservationService;
    private final ElderlyService elderlyService;

    @Autowired
    public API_ReservationController(ReservationService reservationService, ElderlyService elderlyService) {
        this.reservationService = reservationService;
        this.elderlyService = elderlyService;
    }

    // 클라이언트(기본) 예약 생성
    // http://localhost:8080/reservations
    @PostMapping
    public ResponseEntity<Void> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        reservationService.createReservation(reservationDTO);
        return ResponseEntity.ok().build();
    }

    // 어드민 예약 생성
    // http://localhost:8080/reservations/admin
    // 어드민의 경우 시간 생성은 자유다. (but, front에서 제어해야함)
    @PostMapping("/admin")
    public ResponseEntity<Void> createReservation(@Valid @RequestBody AdminReservationDTO adminReservationDTO) {
        reservationService.createAdminReservation(adminReservationDTO);
        return ResponseEntity.ok().build();
    }

    // 예약 수정
    // http://localhost:8080/reservations/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReservation(@PathVariable("id") Long id, @Valid @RequestBody ReservationDTO reservationDTO) {
        reservationService.updateReservation(id, reservationDTO);
        return ResponseEntity.ok().build();
    }

    // 예약 삭제
    // http://localhost:8080/reservations/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }

    // 예약 가능 날짜 확인 API
    // http://localhost:8080/reservations/available-dates
    @GetMapping("/available-dates")
    public ResponseEntity<List<LocalDate>> getAvailableDates(@RequestBody AvailableDatesRequest request) {
        List<LocalDate> availableDates = reservationService.findAvailableDates(
                request.getStartDate(),
                request.getEndDate(),
                request.getDisplayMonth(),
                request.getElderlyId(),
                request.getReservationType()
        );
        return ResponseEntity.ok(availableDates);
    }

    // 예약 가능한 시간 반환
    // http://localhost:8080/reservations/available-times
    @GetMapping("/available-times")
    public List<AvailableTimeDTO> getAvailableTimes(@RequestBody ReservationRequestDTO request) {
        LocalDate reservationDate = LocalDate.parse(request.getDate());
        ReservationType reservationType = ReservationType.valueOf(request.getType().toUpperCase());
        return reservationService.getAvailableTimes(reservationDate, reservationType);
    }
//    @GetMapping("/available-times")
//    public List<AvailableTimeDTO> getAvailableTimes(@RequestParam("date") String date, @RequestParam("type") String type) {
//        LocalDate reservationDate = LocalDate.parse(date);
//        ReservationType reservationType = ReservationType.valueOf(type.toUpperCase());
//        return reservationService.getAvailableTimes(reservationDate, reservationType);
//    }

    // 이름으로 예약 조회(클라이언트)
    // http://localhost:8080/reservations/by-name?name=
    @GetMapping("/by-name")
    public ResponseEntity<List<ReservationReturnDTO>> getReservationsByName(@RequestParam("name") String name) {
        ElderlyDTO elderly = elderlyService.findByName(name);
        List<ReservationReturnDTO> reservations = reservationService.getReservationsByElderlyId(elderly.getElderlyId());
        return ResponseEntity.ok(reservations);
    }

    // 어드민 일정 조회
    // http://localhost:8080/reservations/date-range
    @GetMapping("/date-range")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsWithinDateRange(@RequestBody DateRangeRequestDTO dateRangeRequest) {
        LocalDate startDate = LocalDate.parse(dateRangeRequest.getStart());
        LocalDate endDate = LocalDate.parse(dateRangeRequest.getEnd());
        ReservationType reservationType = ReservationType.valueOf(dateRangeRequest.getReservationType().toUpperCase());

        List<ReservationResponseDTO> result = reservationService.getReservationsWithinDateRange(startDate, endDate, reservationType);
        return ResponseEntity.ok(result);
    }
//    @GetMapping("date-range")
//    public ResponseEntity<List<ElderlyWithReservationsDTO>> getReservationsWithinDateRange(@RequestBody Map<String, String> dateRange) {
//        LocalDate startDate = LocalDate.parse(dateRange.get("start"));
//        LocalDate endDate = LocalDate.parse(dateRange.get("end"));
//        List<ElderlyWithReservationsDTO> result = reservationService.getReservationsWithinDateRange(startDate, endDate);
//        return ResponseEntity.ok(result);
//    }

    // 어드민 예약 상세 정보 조회
    // http://localhost:8080/reservations/detail
    @PostMapping("/detail")
    public ResponseEntity<?> getReservationsByTypeAndDate(@Valid @RequestBody ReservationRequestDTO request) {
        if (ReservationType.valueOf(request.getType().toUpperCase()) == ReservationType.VISIT) {
            List<VisitReservationDetailDTO> reservations = reservationService.getVisitReservationsByDate(LocalDate.parse(request.getDate()));
            return ResponseEntity.ok(reservations);
        } else if (ReservationType.valueOf(request.getType().toUpperCase()) == ReservationType.OUTING) {
            List<OutingReservationDetailDTO> reservations = reservationService.getOutingReservationsByDate(LocalDate.parse(request.getDate()));
            return ResponseEntity.ok(reservations);
        } else {
            return ResponseEntity.badRequest().body("예약 타입이 일치하지 않습니다.");
        }
    }


    // 예약 가능 날짜 확인 Request 형식
    @Getter
    @Setter
    public static class AvailableDatesRequest {
        private LocalDate startDate;
        private LocalDate endDate;
        private int displayMonth;
        private Long elderlyId;
        private String reservationType;
    }
}
