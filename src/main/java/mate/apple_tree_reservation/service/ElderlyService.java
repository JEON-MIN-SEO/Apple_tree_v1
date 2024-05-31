package mate.apple_tree_reservation.service;

import mate.apple_tree_reservation.dto.ElderlyDTO;
import mate.apple_tree_reservation.entity.ElderlyEntity;
import mate.apple_tree_reservation.exception.ResourceNotFoundException;
import mate.apple_tree_reservation.repository.ElderlyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ElderlyService {

    private final ElderlyRepository elderlyRepository;

    @Autowired
    public ElderlyService(ElderlyRepository elderlyRepository) {
        this.elderlyRepository = elderlyRepository;
    }

    //명부 모두 가져오기
    @Transactional(readOnly = true)
    public List<ElderlyDTO> findAll() {
        try {
            List<ElderlyEntity> elderlyList = elderlyRepository.findAll();
            return elderlyList.stream()
                    .map(elderly -> new ElderlyDTO(elderly.getElderlyId(), elderly.getName(), elderly.getFloor()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("입소자 명부를 가져오는 중 오류가 발생했습니다.", e);
        }
    }

    //이름으로 어르신 id 반환
    @Transactional(readOnly = true)
    public ElderlyDTO findByName(String name) {
        try {
            ElderlyEntity elderly = elderlyRepository.findByName(name)
                    .orElseThrow(() -> new ResourceNotFoundException("해당 이름의 어르신을 찾지 못했습니다"));
            return new ElderlyDTO(elderly.getElderlyId(), elderly.getName(), elderly.getFloor());
        } catch (Exception e) {
            throw new RuntimeException("해당 이름의 어르신을 찾지 못했습니다.", e);
        }
    }

    //어르신 명부 저장
    @Transactional
    public void save(ElderlyDTO elderlyDTO) {
        try {
            ElderlyEntity elderly = new ElderlyEntity();
            elderly.setName(elderlyDTO.getName());
            elderly.setFloor(elderlyDTO.getFloor());
            elderlyRepository.save(elderly);
        } catch (Exception e) {
            throw new RuntimeException("어르신 정보를 저장하는 중 오류가 발생했습니다.", e);
        }
    }


    //수정
    @Transactional
    public void updateElderly(Long id, ElderlyDTO elderlyDetails) {
        try {
            elderlyRepository.findById(id).map(existingElderly -> {
                existingElderly.setName(elderlyDetails.getName());
                existingElderly.setFloor(elderlyDetails.getFloor());
                return elderlyRepository.save(existingElderly);
            }).orElseThrow(() -> new ResourceNotFoundException("해당 어르신을 찾지 못했습니다 id: " + id));
        } catch (Exception e) {
            throw new RuntimeException("어르신 정보를 업데이트하는 중 오류가 발생했습니다.", e);
        }
    }

    //삭제
    @Transactional
    public void deleteById(Long id) {
        try {
            if (elderlyRepository.existsById(id)) {
                elderlyRepository.deleteById(id);
            } else {
                throw new ResourceNotFoundException("해당 어르신을 찾지 못했습니다 id: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("어르신 정보를 삭제하는 중 오류가 발생했습니다.", e);
        }
    }
}
