package proj.blog.myblog;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import proj.blog.myblog.models.Category;
import proj.blog.myblog.repositories.CategoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestEntityManager
@Transactional
@EnableJpaAuditing
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager testEntityManager;
    private Category catWoPost;
    private Category catWPost;

    @BeforeEach
    public void setup(){
        catWoPost = Category.builder().catName("test1").build();
        testEntityManager.persist(catWoPost);
    }


    @Test
    void insertionTest(){
        Category savedCat = categoryRepository.findByCatName("test1").get();
        assertEquals(savedCat.getCatName(), catWoPost.getCatName());
    }

    @Test
    void updateTest(){
        // Test updateByCatId
        Category savedCat = categoryRepository.findByCatName("test1").get();
        Long catId = savedCat.getCatId();
        String newCatName = "newTest1";
        categoryRepository.updateByCatId(catId, newCatName);
        testEntityManager.clear();
        Category newSavedCat = categoryRepository.findByCatName(newCatName).get();
        assertEquals(newCatName, newSavedCat.getCatName());

        // Test updateByCatName
        String newnewCatName = "newnewTest1";
        categoryRepository.updateByCatName(newCatName, newnewCatName);
        newSavedCat = categoryRepository.findByCatName(newnewCatName).get();
        assertEquals(newSavedCat.getCatId(), catId);
    }


    @Test
    void findAllTest(){
        int pageSize = 1;
        Pageable pageRequest = PageRequest.of(1, pageSize);
        List<Category> categoryList = categoryRepository.findAll(pageRequest).getContent();
        assertEquals(categoryList.size(), pageSize);
    }


    @Test
    void TimeTest(){
        int pageSize = 1;
        Pageable pageRequest = PageRequest.of(0, pageSize);
        Category savedCat = categoryRepository.findByCatName("test1").get();
        System.out.println(savedCat);
        LocalDateTime createdTime = savedCat.getCreatedAt();
        LocalDateTime modifiedTime = savedCat.getModifiedAt();

        LocalDateTime time1 = createdTime.minusHours(1);
        LocalDateTime time2 = createdTime.plusHours(1);
        LocalDateTime time3 = modifiedTime.minusHours(1);
        LocalDateTime time4 = modifiedTime.plusHours(1);

        Category cat1 = categoryRepository.findByCreatedAtBetween(time1, time2, pageRequest).getContent().get(0);
        Category cat2 = categoryRepository.findByModifiedAtBetween(time3, time4, pageRequest).getContent().get(0);

        assertEquals(savedCat.getCatId(), cat1.getCatId());
        assertEquals(savedCat.getCatId(), cat2.getCatId());
    }
}
