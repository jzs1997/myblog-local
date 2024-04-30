package proj.blog.myblog.controllers;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proj.blog.myblog.exceptions.CategoryNotFoundException;
import proj.blog.myblog.models.Category;
import proj.blog.myblog.requests.CreateCategoryRequest;
import proj.blog.myblog.requests.UpdateCategoryRequest;
import proj.blog.myblog.services.CategoryService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cats")
@RequiredArgsConstructor
public class CatController {

    private final CategoryService categoryService;

    @PostMapping("/cat")
    public ResponseEntity<Long> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest){
        List<String> postIds = createCategoryRequest.getPostIds();
        String catName = createCategoryRequest.getCatName();
        Long id;
        if(postIds.size() == 0) id = categoryService.createCat(catName);
        else id = categoryService.createCatWithPosts(catName, postIds);
        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }

    @PutMapping("/cat/{catId}")
    public ResponseEntity<Void> updateCategoryById(@RequestBody UpdateCategoryRequest updateCategoryRequest, @PathVariable Long catId){
        String newCatName = updateCategoryRequest.getNewCatName();
        categoryService.updateCatName(catId, newCatName);
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }

    @PutMapping("/cat/{catName}/names")
    public ResponseEntity<Void> updateCategoryById(@RequestBody UpdateCategoryRequest updateCategoryRequest, @PathVariable String catName){
        String newCatName = updateCategoryRequest.getNewCatName();
        categoryService.updateCatByName(catName, newCatName);
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }


    @GetMapping("/cat/{catId}")
    public ResponseEntity<Category> findCategory(@PathVariable Long catId) throws Exception {
        return ResponseEntity.ok().body(
                categoryService.getCat(catId).orElseThrow(() -> new CategoryNotFoundException())
        );
    }

    @GetMapping("/cat/{catName}/names")
    public ResponseEntity<Category> findCategoryByName(@PathVariable String catName) throws Exception {
        return ResponseEntity.ok().body(
                categoryService.getCatByName(catName).orElseThrow(()-> new CategoryNotFoundException())
        );
    }

    @GetMapping("/cat/sorted")
    public ResponseEntity<List<Category>> findCategorySorted(
            @RequestParam int sort_type,
            @RequestParam int page,
            @RequestParam int size
    ){
        List<Category> res = categoryService.getAllCatsSorted(sort_type, page, size);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/cat/cTime")
    public ResponseEntity<List<Category>> findByCreatedTimeRange(
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam int page,
            @RequestParam int size
    ){

        List<Category> res = categoryService.getByCreatedAtBetween(LocalDateTime.parse(startTime), LocalDateTime.parse(endTime), page, size);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/cat/mTime")
    public ResponseEntity<List<Category>> findByModifiedTimeRange(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            @RequestParam int page,
            @RequestParam int size
    ){
        List<Category> res = categoryService.getByModifiedAtBetween(startTime, endTime, page, size);
        return ResponseEntity.ok().body(res);
    }


    @DeleteMapping("/cat/{catId}")
    public  ResponseEntity<String> deleteCategory(@PathVariable Long catId) throws Exception {
        categoryService.deleteCat(catId);
        return ResponseEntity.ok("Deleted");
    }
}
