package proj.blog.myblog.services.impls;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import proj.blog.myblog.exceptions.PostNotFoundException;
import proj.blog.myblog.models.Category;
import proj.blog.myblog.models.Post;
import proj.blog.myblog.repositories.CategoryRepository;
import proj.blog.myblog.repositories.PostRepository;
import proj.blog.myblog.requests.CreatePostRequest;
import proj.blog.myblog.services.CategoryService;
import proj.blog.myblog.services.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository repository;
    private final CategoryRepository categoryRepository;
    @Override
    public String createPost(CreatePostRequest postRequest) {
        String text = postRequest.getText();
        String title = postRequest.getTitle();
        List<Long> catIds = postRequest.getCategoryIds();
        Post newPost = Post.builder().title(title).text(text).categories(catIds).build();
        System.out.println(text);
        System.out.println(title);
        catIds.forEach(e -> System.out.println(e));
        newPost.toString();
        Post savedPost = repository.save(newPost);
        if(catIds != null && catIds.size() != 0){
            String postId = savedPost.getPostId().toHexString();
            for(Long catId: catIds){
                Category cat = categoryRepository.findById(catId).get();
                List<String> postIds = cat.getPosts();
                postIds.add(postId);
//                List<ObjectId> newPostIds = postIds.stream().map(ObjectId::new).collect(Collectors.toList());
                updatePostsByCatId(catId, postIds);
            }
        }
        return savedPost.getPostId().toString();
    }

    @Override
    public String deletePost(ObjectId id) {
        List<Long> catIds = repository.findById(id).get().getCategories();
        for(Long catId : catIds){
            Category cat = categoryRepository.findById(catId).get();
            List<String> postIds = cat.getPosts();
            postIds.remove(id.toHexString());
//            List<ObjectId> newPostIds = postIds.stream().map(ObjectId::new).collect(Collectors.toList());
            updatePostsByCatId(catId, postIds);
        }
        repository.deleteById(id);
        return id.toString();
    }

    @Override
    public Post findPost(ObjectId id) throws PostNotFoundException {
        return repository.findById(id).orElseThrow(() -> new PostNotFoundException());
    }

    @Override
    public List<Post> pagePostsSort(int pageNumber, int pageSize) {
        Sort sort = Sort.by("create_at").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        List<Post> posts = repository.findAll(pageRequest).getContent();
        return posts;
    }

    @Override
    public void addCatToPosts(ObjectId id, List<Long> catIds) {
        Post post = repository.findById(id).get();
        List<Long> originCatIds = post.getCategories();
        if(originCatIds == null) originCatIds = new ArrayList<>();
        for(Long catId: catIds){
            originCatIds.add(catId);
        }
        repository.findAndPushCategoriesByPostId(id, originCatIds);
        return ;
    }

    @Override
    public String removeCatFromPosts(Long catId, ObjectId postId) {
        /**
         * TODO: Add exception handler
         */
        Post post = repository.findById(postId).get();
        List<Long> catIds = post.getCategories();
        catIds.remove(catId);
        repository.findAndPushCategoriesByPostId(postId, catIds);
        return postId.toHexString() + " updated";
    }

    @Override
    public List<Post> getPostsByCategory(Long catId, int page, int size) {
        Sort sort = Sort.by("create_at");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Post> posts = repository.findAllByCategoriesContaining(catId, pageable).getContent();
        return posts;
    }

    @Override
    public List<Post> getPostsByCreatedDate(int page, int size) {
        Sort sort = Sort.by("create_at");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Post> posts = repository.findAllByCreatedDate(pageable).getContent();
        return posts;
    }

    @Override
    public List<Post> getPostsByModifiedDate(int page, int size) {
        Sort sort = Sort.by("modified_at");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Post> posts = repository.findAllByModifiedDate(pageable).getContent();
        return posts;
    }


    private void updatePostsByCatId(Long catId, List<String> postIds) {
        Category cat = categoryRepository.findById(catId).get();
        cat.setPosts(postIds);
        categoryRepository.save(cat);
        return ;
    }


    /**
     * TODO: DeleteCatFromPosts
     */
}
