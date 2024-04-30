package proj.blog.myblog.services;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import proj.blog.myblog.models.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public Long createCat(String cateName);
    public Optional<Category> getCat(Long catId);
    public Optional<Category> getCatByName(String catName);
    public void updateCatName(Long catId, String newCatName);
    public void updateCatByName(String catName, String newCatName);
    public Long createCatWithPosts(String cateName, List<String> postIds);
    public void addPostsToCat(Long catId, List<String> postIds);
    public void deleteCat(Long catId) throws Exception;
    public void updatePosts(Long catId, List<String> postIds);
    public void deletePostsFromCat(Long catId, List<String> postIds);
    public void updatePostsByCatId(Long catId, List<String> postIds);

    /**
     * @param sort_type: 0 for created_at, 1 for modified_at
     * @return List<Category>
     */
    public List<Category> getAllCatsSorted(int sort_type, int page, int size);
    public List<Category> getByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime, int page, int size);
    public List<Category> getByModifiedAtBetween(LocalDateTime startTime, LocalDateTime endTime, int page, int size);
}
