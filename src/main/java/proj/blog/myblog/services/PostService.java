package proj.blog.myblog.services;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import proj.blog.myblog.exceptions.PostNotFoundException;
import proj.blog.myblog.models.Post;
import proj.blog.myblog.requests.CreatePostRequest;

import java.util.List;

public interface PostService {
    String createPost(CreatePostRequest postRequest);
    String deletePost(ObjectId id);
    Post findPost(ObjectId id) throws PostNotFoundException;
    List<Post> pagePostsSort(int pageNumber, int pageSize);
    void addCatToPosts(ObjectId id, List<Long> catIds);
    String removeCatFromPosts(Long catId, ObjectId postId);
    List<Post> getPostsByCategory(Long catId, int page, int size);
    List<Post> getPostsByCreatedDate(int page, int size);
    List<Post> getPostsByModifiedDate(int page, int size);
}
