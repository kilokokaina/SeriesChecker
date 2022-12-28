package com.serieschecker.SeriesChecker.repos;

import com.serieschecker.SeriesChecker.models.TitleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TitleRepository extends JpaRepository<TitleModel, Long> {
    TitleModel findByTitleName(String titleName);

    @Query(value = "SELECT * FROM title_model WHERE title_genre LIKE %:genre%", nativeQuery = true)
    List<TitleModel> findByGenre(@Param("genre") String genre);

    @Query(value = "SELECT * FROM title_model", nativeQuery = true)
    Page<TitleModel> findAllPageable(Pageable pageable);
}
