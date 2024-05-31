package mate.apple_tree_reservation.controller.api;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import mate.apple_tree_reservation.dto.*;
import mate.apple_tree_reservation.enums.ReservationType;
import mate.apple_tree_reservation.service.ElderlyService;
import mate.apple_tree_reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/reservations")
public class API_ReservationController {

    private final ReservationService reservationService;
    private final ElderlyService elderlyService;

    @Autowired
    public API_ReservationController(ReservationService reservationService, ElderlyService elderlyService) {
        this.reservationService = reservationService;
        this.elderlyService = elderlyService;
    }

    // 어드민 예약 생성
    @PostMapping
    public ResponseEntity<Void> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        reservationService.createReservation(reservationDTO);
        return ResponseEntity.ok().build(); // http://localhost:8080/reservation
    }

    // 예약 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReservation(@PathVariable("id") Long id, @Valid @RequestBody ReservationDTO reservationDTO) {
        reservationService.updateReservation(id, reservationDTO);
        return ResponseEntity.ok().build(); // http://localhost:8080/reservation/{id}
    }

    // 예약 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build(); // http://localhost:8080/reservation/{id}
    }

    //예약 가능 날짜 확인 API
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
    @PostMapping("/available-times")
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

    //이름으로 예약 조회
    @GetMapping("/by-name")
    public ResponseEntity<List<ReservationReturnDTO>> getReservationsByName(@RequestParam("name") String name) {
        ElderlyDTO elderly = elderlyService.findByName(name);
        List<ReservationReturnDTO> reservations = reservationService.getReservationsByElderlyId(elderly.getElderlyId());
        return ResponseEntity.ok(reservations);
    }

    //어드민 일정 조회
    //{
    //    "start": "2024-06-01",
    //    "end": "2024-06-07"
    //}
    @PostMapping("date-range")
    public ResponseEntity<List<ElderlyWithReservationsDTO>> getReservationsWithinDateRange(@RequestBody Map<String, String> dateRange) {
        LocalDate startDate = LocalDate.parse(dateRange.get("start"));
        LocalDate endDate = LocalDate.parse(dateRange.get("end"));
        List<ElderlyWithReservationsDTO> result = reservationService.getReservationsWithinDateRange(startDate, endDate);
        return ResponseEntity.ok(result);
    }


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