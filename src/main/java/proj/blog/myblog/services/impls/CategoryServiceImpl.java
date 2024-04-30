package proj.blog.myblog.services.impls;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import proj.blog.myblog.exceptions.SortTypeInvalidException;
import proj.blog.myblog.models.Category;
import proj.blog.myblog.models.Post;
import proj.blog.myblog.repositories.CategoryRepository;
import proj.blog.myblog.repositories.PostRepository;
import proj.blog.myblog.services.CategoryService;
import proj.blog.myblog.services.PostService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * TODO:
 * 1. Add ascending sorting for getByCreatedAtBetween
 * 2. Add ascending sorting for getByModifiedAtBetween
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final PostRepository postRepository;
    private final CategoryRepository repository;
    @Override
    public Long createCat(String cateName) {
        Category cat = Category.builder().
                catName(cateName).
                posts(new ArrayList<>()).
                build();
        Category createdCat = repository.save(cat);
        return createdCat.getCatId();
    }

    @Override
    public Optional<Category> getCat(Long catId) {
        return repository.findById(catId);
    }

    @Override
    public Optional<Category> getCatByName(String catName) {
        return repository.findByCatName(catName);
    }

    @Override
    public void updateCatName(Long catId, String newCatName) {
        repository.updateByCatId(catId, newCatName);
    }

    @Override
    public void updateCatByName(String catName, String newCatName) {
        repository.updateByCatName(catName, newCatName);
    }

    @Override
    public Long createCatWithPosts(String cateName, List<String> postIds) {
        Category cat = Category.builder()
                        .catName(cateName)
                        .posts(new ArrayList<>(postIds))
                        .build();
        Category createdCat = repository.save(cat);
        for(String postId: postIds){
            ObjectId objectId = new ObjectId(postId);
            List<Long> catIds = new ArrayList<>();
            catIds.add(createdCat.getCatId());
            addCatToPosts(objectId, catIds);
        }

        return createdCat.getCatId();
    }

    @Override
    public void addPostsToCat(Long catId, List<String> postIds) {
        List<String> originPostIds = repository.findById(catId).get().getPosts();
        for(String postId: postIds){
            originPostIds.add(postId);
        }
//        List<ObjectId> newPostIds = new ArrayList<>();
//        for(String postId: originPostIds){
//            newPostIds.add(new ObjectId(postId));
//        }
        updatePostsByCatId(catId, originPostIds);
    }


    @Override
    public void deleteCat(Long catId) throws Exception {
        Optional<Category> cat = repository.findById(catId);
        if(!cat.isPresent()) {
            throw new Exception("Category not found");
        }
        List<String> postIds = repository.findById(catId).get().getPosts();
        if(postIds != null && postIds.size() != 0){
            for(String postId: postIds){
                removeCatFromPosts(catId, new ObjectId(postId));
            }
        }
        repository.deleteById(catId);
    }

    @Override
    public void updatePosts(Long catId, List<String> postIds) {
//        List<ObjectId> newPostIds = postIds.stream().map(ObjectId::new).collect(toList());
        updatePostsByCatId(catId, postIds);
    }

    @Override
    public void deletePostsFromCat(Long catId, List<String> postIds) {
        List<String> originPostIds = repository.findById(catId).get().getPosts();
        for(String postId: postIds){
            originPostIds.remove(postId);
        }
//        List<ObjectId> newPostIds = originPostIds.stream().map(ObjectId::new).collect(toList());
        updatePostsByCatId(catId, originPostIds);
    }

    @Override
    public void updatePostsByCatId(Long catId, List<String> postIds) {
        Category cat = repository.findById(catId).get();
        cat.setPosts(postIds);
        repository.save(cat);
        return ;
    }

    @Override
    public List<Category> getAllCatsSorted(int sort_type, int page, int size) {
        Sort sort = Sort.by("modifiedAt").descending();
        if(sort_type == 1){
            sort = Sort.by("modifiedAt").descending();
        }
        Pageable catsSortPage = PageRequest.of(page, size, sort);
        Page<Category> cats = repository.findAll(catsSortPage);
        List<Category> res = cats.getContent();
        return res;
    }

    @Override
    public List<Category> getByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime, int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable catsSortPage = PageRequest.of(page, size, sort);
        List<Category> res = repository.findByCreatedAtBetween(startTime, endTime, catsSortPage).getContent();
        return res;
    }

    @Override
    public List<Category> getByModifiedAtBetween(LocalDateTime startTime, LocalDateTime endTime, int page, int size) {
        Sort sort = Sort.by("modifiedAt").descending();
        Pageable catsSortPage = PageRequest.of(page, size, sort);
        List<Category> res = repository.findByModifiedAtBetween(startTime, endTime, catsSortPage).getContent();
        return res;
    }

    private void addCatToPosts(ObjectId id, List<Long> catIds) {
        Post post = postRepository.findById(id).get();
        List<Long> originCatIds = post.getCategories();
        if(originCatIds == null) originCatIds = new ArrayList<>();
        for(Long catId: catIds){
            originCatIds.add(catId);
        }
        postRepository.findAndPushCategoriesByPostId(id, originCatIds);
        return ;
    }

    private String removeCatFromPosts(Long catId, ObjectId postId) {
        System.out.println(postId.toHexString());
        Post post = postRepository.findById(postId).get();
        if(post == null){
            return "No post found";
        }
        List<Long> catIds = post.getCategories();
        catIds.remove(catId);
        postRepository.findAndPushCategoriesByPostId(postId, catIds);
        return postId.toHexString() + " updated";
    }
    /**
     * TODO: deletePostFromCat
     */
}
