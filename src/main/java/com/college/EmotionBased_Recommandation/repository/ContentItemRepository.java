package com.college.EmotionBased_Recommandation.repository;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ContentItemRepository extends JpaRepository<ContentItem, Long> {

    List<ContentItem> findByTargetEmotion(String targetEmotion);

    List<ContentItem> findByTargetEmotionAndType(String targetEmotion, String type);

    Optional<ContentItem> findByExternalIdAndSourceApi(String externalId, String sourceApi);
}