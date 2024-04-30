package proj.blog.myblog.controllers;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proj.blog.myblog.exceptions.PostNotFoundException;
import proj.blog.myblog.models.Post;
import proj.blog.myblog.requests.CreatePostRequest;
import proj.blog.myblog.services.PostService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    ResponseEntity<String> createPost(@RequestBody CreatePostRequest postRequest){
        String postId = postService.createPost(postRequest);
        return ResponseEntity.created(URI.create(postId)).build();
    }

    @GetMapping("/post/{id}")
    ResponseEntity<Post> findPost(@PathVariable String id) throws PostNotFoundException {
        // Must be a valid hexString
        ObjectId objectId = new ObjectId(id);
        Post post = postService.findPost(objectId);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/post/{id}")
    ResponseEntity<String> deletePost(@PathVariable String id){
        ObjectId objectId = new ObjectId(id);
        postService.deletePost(objectId);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/post/page-posts")
    ResponseEntity<List<Post>> getPagePosts(@RequestParam int pageNumber, @RequestParam int pageSize){
        List<Post> posts = postService.pagePostsSort(pageNumber, pageSize);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/post/cat-posts/{catId}")
    ResponseEntity<List<Post>> getPostsByCategory(@PathVariable Long catId, @RequestParam int pageNumber, @RequestParam int pageSize){
        List<Post> posts = postService.getPostsByCategory(catId, pageNumber, pageSize);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/post/posts/cTime")
    ResponseEntity<List<Post>> getPostByCreateDate(@PathVariable int page, @PathVariable int size){
        List<Post> posts = postService.getPostsByCreatedDate(page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/post/posts/mTime")
    ResponseEntity<List<Post>> getPostsByModifiedDate(@PathVariable int page, @PathVariable int size){
        List<Post> posts = postService.getPostsByModifiedDate(page, size);
        return ResponseEntity.ok(posts);
    }
}
