package mate.apple_tree_reservation.controller.api;

import mate.apple_tree_reservation.dto.ElderlyDTO;
import mate.apple_tree_reservation.service.ElderlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/elderly")
public class API_ElderlyController {

    @Autowired
    private ElderlyService elderlyService;

    // 입소자 명부 모두 조회
    // http://localhost:8080/elderly
    @GetMapping
    public List<ElderlyDTO> getAllElderly() {
        return elderlyService.findAll();
    }

    // 입소자 명부 추가
    // http://localhost:8080/elderly
    @PostMapping
    public ResponseEntity<Void> createElderly(@RequestBody ElderlyDTO elderly) {
        elderlyService.save(elderly);
        return ResponseEntity.ok().build();
    }

    // 입소자 명부 수정
    // http://localhost:8080/elderly/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateElderly(@PathVariable("id") Long id, @RequestBody ElderlyDTO elderlyDetails) {
        elderlyService.updateElderly(id, elderlyDetails);
        return ResponseEntity.ok().build(); // http://localhost:8080/elderly/2
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteElderly(@PathVariable("id") Long id) {
        elderlyService.deleteById(id);
        return ResponseEntity.ok().build(); // http://localhost:8080/elderly/4
    }

    //이름으로 id 찾기
    @GetMapping("/findByName")
    public ResponseEntity<Long> getElderlyIdByName(@RequestParam("name") String name) {
        ElderlyDTO elderly = elderlyService.findByName(name);
        return ResponseEntity.ok(elderly.getElderlyId()); //http://localhost:8080/elderly/findByName?name=홍길순
    }
}
