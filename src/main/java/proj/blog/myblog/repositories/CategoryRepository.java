package proj.blog.myblog.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import proj.blog.myblog.models.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCatName(String catName);

    @Modifying
    @Transactional
    @Query("update Category c set c.catName = ?2 where c.catId = ?1")
    void updateByCatId(Long catId, String newCatName);

    @Modifying
    @Transactional
    @Query("update Category c set c.catName = ?2 where c.catName = ?1")
    void updateByCatName(String catName, String newCatName);

    Page<Category> findAll(Pageable pageable);
    Page<Category> findByCreatedAtBetween(LocalDateTime left, LocalDateTime right, Pageable pageable);
    Page<Category> findByModifiedAtBetween(LocalDateTime left, LocalDateTime right, Pageable pageable);
}
