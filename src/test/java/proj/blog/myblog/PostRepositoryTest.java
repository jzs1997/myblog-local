package proj.blog.myblog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import proj.blog.myblog.repositories.PostRepository;

@DataMongoTest
public class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;


    @BeforeEach
    void insertOne(){

    }


    @Test
    void testFindAllByDate(){

    }


    @Test
    void testFindAllByDateRange(){

    }

    @Test
    void testUpdate(){

    }
}
