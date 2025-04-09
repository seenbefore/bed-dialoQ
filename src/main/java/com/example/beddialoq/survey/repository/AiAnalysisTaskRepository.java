package com.example.beddialoq.survey.repository;

import com.example.beddialoq.survey.entity.AiAnalysisTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * AI分析任务Repository
 */
@Repository
public interface AiAnalysisTaskRepository extends JpaRepository<AiAnalysisTask, String> {
    
    /**
     * 根据答卷ID查询任务
     * @param responseId 答卷ID
     * @return 任务列表
     */
    List<AiAnalysisTask> findByResponseId(String responseId);
    
    /**
     * 根据答卷ID查询最新任务
     * @param responseId 答卷ID
     * @return 最新任务
     */
    @Query("SELECT t FROM AiAnalysisTask t WHERE t.responseId = :responseId ORDER BY t.createTime DESC")
    Optional<AiAnalysisTask> findLatestByResponseId(@Param("responseId") String responseId);
    
    /**
     * 查询待处理任务列表
     * @param status 任务状态
     * @param limit 限制数量
     * @return 待处理任务列表
     */
    @Query("SELECT t FROM AiAnalysisTask t WHERE t.status = :status ORDER BY t.createTime ASC LIMIT :limit")
    List<AiAnalysisTask> findPendingTasks(@Param("status") String status, @Param("limit") int limit);
} 