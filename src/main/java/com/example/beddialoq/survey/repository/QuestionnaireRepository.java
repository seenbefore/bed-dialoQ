package com.example.beddialoq.survey.repository;

import com.example.beddialoq.survey.entity.Questionnaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 问卷Repository
 */
@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, String> {
    
    /**
     * 根据ID查询未删除的问卷
     * @param id 问卷ID
     * @return 问卷对象
     */
    Optional<Questionnaire> findByIdAndIsDeletedFalse(String id);
    
    /**
     * 动态条件分页查询问卷
     * @param title 问卷标题
     * @param status 问卷状态
     * @param createTimeStart 创建开始时间
     * @param createTimeEnd 创建结束时间
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("SELECT q FROM Questionnaire q WHERE q.isDeleted = false " +
            "AND (:title IS NULL OR q.title LIKE %:title%) " +
            "AND (:status IS NULL OR :status = '' OR q.status = :status) " +
            "AND (:createTimeStart IS NULL OR q.createTime >= :createTimeStart) " +
            "AND (:createTimeEnd IS NULL OR q.createTime <= :createTimeEnd) " +
            "ORDER BY q.createTime DESC")
    Page<Questionnaire> findByConditions(
            @Param("title") String title,
            @Param("status") String status,
            @Param("createTimeStart") LocalDateTime createTimeStart,
            @Param("createTimeEnd") LocalDateTime createTimeEnd,
            Pageable pageable);
    
    /**
     * 统计问卷的答卷数量
     * @param questionnaireId 问卷ID
     * @return 答卷数量
     */
    @Query("SELECT COUNT(r) FROM Response r WHERE r.questionnaireId = :questionnaireId AND r.isDeleted = false")
    long countResponsesByQuestionnaireId(@Param("questionnaireId") String questionnaireId);
} 