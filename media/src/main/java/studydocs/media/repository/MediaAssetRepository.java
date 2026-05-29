package studydocs.media.repository;

import studydocs.media.model.entity.MediaAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import studydocs.media.model.enums.AssetState;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {
    List<MediaAsset> findByStateAndCreatedAtBefore(AssetState state, LocalDateTime time);
}
