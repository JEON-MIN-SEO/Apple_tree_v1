package mate.apple_tree_reservation.repository;

import mate.apple_tree_reservation.entity.ElderlyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ElderlyRepository extends JpaRepository<ElderlyEntity,Long> {
    // 어르신 이름으로 조회
    Optional<ElderlyEntity> findByName(String name);
}