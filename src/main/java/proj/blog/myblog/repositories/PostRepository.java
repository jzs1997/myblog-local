package proj.blog.myblog.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import proj.blog.myblog.models.Post;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, ObjectId> {
    @Update("{'$set' :  {'categories' :  ?1} }")
    void findAndPushCategoriesByPostId(ObjectId postId, List<Long> categories);
    Page<Post> findAllByCategoriesContaining(Long catId, Pageable pageable);
    Page<Post> findAllByCreatedDate(Pageable pageable);
    Page<Post> findAllByModifiedDate(Pageable pageable);

    // No usage in controller
    Page<Post> findAllByCreatedDateBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    Page<Post> findAllByModifiedDateBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}
