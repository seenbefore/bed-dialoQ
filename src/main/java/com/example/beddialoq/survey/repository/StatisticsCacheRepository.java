package com.example.beddialoq.survey.repository;

import com.example.beddialoq.survey.entity.StatisticsCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 统计缓存Repository
 */
@Repository
public interface StatisticsCacheRepository extends JpaRepository<StatisticsCache, String> {
    
    /**
     * 根据问卷ID、缓存类型和缓存键查询缓存
     * @param questionnaireId 问卷ID
     * @param cacheType 缓存类型
     * @param cacheKey 缓存键
     * @return 缓存对象
     */
    Optional<StatisticsCache> findByQuestionnaireIdAndCacheTypeAndCacheKey(
            String questionnaireId, String cacheType, String cacheKey);
    
    /**
     * 根据问卷ID和缓存类型查询缓存列表
     * @param questionnaireId 问卷ID
     * @param cacheType 缓存类型
     * @return 缓存列表
     */
    List<StatisticsCache> findByQuestionnaireIdAndCacheType(String questionnaireId, String cacheType);
    
    /**
     * 根据问卷ID查询缓存列表
     * @param questionnaireId 问卷ID
     * @return 缓存列表
     */
    List<StatisticsCache> findByQuestionnaireId(String questionnaireId);
    
    /**
     * 查询过期的缓存列表
     * @param now 当前时间
     * @return 过期缓存列表
     */
    @Query("SELECT s FROM StatisticsCache s WHERE s.expireTime IS NOT NULL AND s.expireTime < :now")
    List<StatisticsCache> findExpiredCaches(@Param("now") LocalDateTime now);
    
    /**
     * 删除过期的缓存
     * @param now 当前时间
     * @return 影响行数
     */
    @Query("DELETE FROM StatisticsCache s WHERE s.expireTime IS NOT NULL AND s.expireTime < :now")
    int deleteExpiredCaches(@Param("now") LocalDateTime now);
} 