package com.example.beddialoq.survey.repository;

import com.example.beddialoq.survey.entity.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 答卷Repository
 */
@Repository
public interface ResponseRepository extends JpaRepository<Response, String> {
    
    /**
     * 根据ID查询未删除的答卷
     * @param id 答卷ID
     * @return 答卷对象
     */
    Optional<Response> findByIdAndIsDeletedFalse(String id);
    
    /**
     * 根据问卷ID查询未删除的答卷列表
     * @param questionnaireId 问卷ID
     * @return 答卷列表
     */
    List<Response> findByQuestionnaireIdAndIsDeletedFalse(String questionnaireId);
    
    /**
     * 根据问卷ID查询未删除的答卷分页列表
     * @param questionnaireId 问卷ID
     * @param pageable 分页参数
     * @return 答卷分页列表
     */
    Page<Response> findByQuestionnaireIdAndIsDeletedFalse(String questionnaireId, Pageable pageable);
    
    /**
     * 动态条件分页查询答卷
     * @param questionnaireId 问卷ID
     * @param submitter 提交人
     * @param submitTimeStart 提交开始时间
     * @param submitTimeEnd 提交结束时间
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("SELECT r FROM Response r WHERE r.isDeleted = false " +
            "AND (:questionnaireId IS NULL OR :questionnaireId = '' OR r.questionnaireId = :questionnaireId) " +
            "AND (:submitter IS NULL OR r.submitter LIKE %:submitter%) " +
            "AND (:submitTimeStart IS NULL OR r.submitTime >= :submitTimeStart) " +
            "AND (:submitTimeEnd IS NULL OR r.submitTime <= :submitTimeEnd) " +
            "ORDER BY r.submitTime DESC")
    Page<Response> findByConditions(
            @Param("questionnaireId") String questionnaireId,
            @Param("submitter") String submitter,
            @Param("submitTimeStart") LocalDateTime submitTimeStart,
            @Param("submitTimeEnd") LocalDateTime submitTimeEnd,
            Pageable pageable);
    
    /**
     * 计算问卷的平均答题时长
     * @param questionnaireId 问卷ID
     * @return 平均答题时长（秒）
     */
    @Query("SELECT AVG(r.duration) FROM Response r WHERE r.questionnaireId = :questionnaireId AND r.isDeleted = false")
    Double calculateAverageDuration(@Param("questionnaireId") String questionnaireId);
    
    /**
     * 统计每日答卷数量
     * @param questionnaireId 问卷ID
     * @param startDate 开始日期
     * @return 每日答卷数量列表 [日期, 数量]
     */
    @Query("SELECT FUNCTION('DATE', r.submitTime) as date, COUNT(r) as count " +
            "FROM Response r " +
            "WHERE r.questionnaireId = :questionnaireId AND r.isDeleted = false " +
            "AND r.submitTime >= :startDate " +
            "GROUP BY FUNCTION('DATE', r.submitTime) " +
            "ORDER BY date ASC")
    List<Object[]> countDailyResponses(
            @Param("questionnaireId") String questionnaireId,
            @Param("startDate") LocalDateTime startDate);
} 