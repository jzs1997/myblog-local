package proj.blog.myblog;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import proj.blog.myblog.controllers.CatController;
import proj.blog.myblog.models.Category;
import proj.blog.myblog.repositories.CategoryRepository;
import proj.blog.myblog.services.CategoryService;
import proj.blog.myblog.services.impls.CategoryServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@SpringBootTest
public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository repository;

    @Test
    void catCreationTest() throws Exception{
//        for createCat and createCatWithPosts
//        String catName = "Sample1";
//        Category cat = Category.builder().catId(0L).catName(catName).build();
//        System.out.println(cat);
//        when(repository.save(cat))
//                .thenReturn(cat);
//        Long id = this.categoryService.createCat(catName);
//        verify(repository).save(cat);
//        Assertions.assertEquals(cat.getCatId(), id);

        // TODO: Test `createCatWithPosts` method

    }


    @Test
    void catAccessingTest(){
//        Test both getCat and getCatByName here
        String catName = "Sample1";
        Category cat = Category.builder().catName(catName).build();
        when(repository.findByCatName(catName))
                .thenReturn(Optional.of(cat));

        Optional<Category> opCat = categoryService.getCatByName(catName);
//        verify(repository).findByCatName(catName);
        Assertions.assertEquals(true, opCat.isPresent());
    }


    @Test
    void catUpdatingTest(){
        String catName = "Sample1";
        String newCatName = "newSample1";
        Category cat = Category.builder().catName(catName).build();
        categoryService.updateCatByName(catName, newCatName);
        verify(repository).updateByCatName(catName, newCatName);
    }

    @Test
    void catDeletingTest() throws Exception {
        Long id = 0L;
        String catName = "Sample1";
        Category cat = Category.builder().catId(id).catName(catName).build();
        when(repository.findById(id)).thenReturn(Optional.of(cat));
        doNothing().when(repository).deleteById(id);
        categoryService.deleteCat(id);
        verify(repository).deleteById(id);
        Assertions.assertAll(() -> categoryService.deleteCat(id));
    }

    @Test
    void catSortedTest(){
        Long id1 = 0L;
        Long id2 = 1L;

        String catName1 = "Sample1";
        String catName2 = "Sample2";

        Category cat1 = Category.builder().catId(id1).catName(catName1).build();
        Category cat2 = Category.builder().catId(id2).catName(catName2).build();

        int page = 0, size = 2;
        int sort_type = 1;
        Sort sort = Sort.by("modifiedAt").descending();
        Pageable catsSortPage = PageRequest.of(page, size, sort);
        List<Category> lc = new ArrayList<>();
        lc.add(cat1);
        lc.add(cat2);
        when(repository.findAll(catsSortPage)).thenReturn(new PageImpl<>(lc));
        List<Category> res = categoryService.getAllCatsSorted(sort_type, page, size);
        Assertions.assertEquals(2, res.size());
    }

    @Test
    void catTimeRangeTest(){

    }
}
