package com.studydocs.modules.system.repository;

import com.studydocs.modules.system.entity.MediaAssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaAssetRepository extends JpaRepository<MediaAssetEntity, String> {
}
