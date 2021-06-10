package com.yss.logrecord.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface LogRecordRepository extends JpaRepository<LogEntityDo,String> {

    @Query(value = "SELECT T.BROWSER as browser,\n" +
            "       T.START_TIME as startTime,\n" +
            "       T.URL as url,\n" +
            "       T.RETURN_TIME as returnTime,\n" +
            "       T.TIME_CONSUMING as timeConsuming,\n" +
            "       T.REFRESH_TOKEN as refreshtoken \n" +
            "  FROM LOG_RECORD T\n" +
            " WHERE T.START_TIME BETWEEN :startTime AND :endTime\n" +
            " ORDER BY T.START_TIME ASC", nativeQuery = true)
    List<Map<String, Object>> findByStartTime(@Param(value = "startTime") LocalDateTime startTime, @Param(value = "endTime")LocalDateTime endTime);

    List<LogEntityDo> findAllByUrlLikeAndStartTimeBetween(String url,LocalDateTime startTime, LocalDateTime endTime);


    @Query(value = "SELECT r.BROWSER code,count(r.BROWSER) num FROM LOG_RECORD r GROUP BY r.BROWSER",nativeQuery = true)
    List findAllByGroupAndBrowsers();

    @Query(value = "SELECT T.BROWSER,\n" +
            "       T.START_TIME,\n" +
            "       T.URL,\n" +
            "       T.RETURN_TIME,\n" +
            "       T.TIME_CONSUMING,\n" +
            "       T.REFRESH_TOKEN\n" +
            "  FROM LOG_RECORD T\n" +
            " WHERE T.URL LIKE :url AND T.START_TIME BETWEEN :startTime AND :endTime\n" +
            " ORDER BY T.START_TIME ASC", nativeQuery = true)
    List<Map<String, Object>> findByUrLAndTimeOrderByTime(@Param(value = "url") String url,
                                                                             @Param(value = "startTime") LocalDateTime startTime,
                                                                             @Param(value = "endTime")LocalDateTime endTime);
    @Query(value = "SELECT T.BROWSER,\n" +
            "       T.START_TIME,\n" +
            "       T.URL,\n" +
            "       T.RETURN_TIME,\n" +
            "       T.TIME_CONSUMING,\n" +
            "       T.REFRESH_TOKEN\n" +
            "  FROM LOG_RECORD T\n" +
            " WHERE T.URL LIKE :url \n" +
            " ORDER BY T.START_TIME ASC", nativeQuery = true)
    List<Map<String, Object>> findByUrlOrderByTimeAsc(@Param(value = "url") String url);

    Page<LogEntityDo> findAll(Specification<LogEntityDo> specification, Pageable pageable);
}