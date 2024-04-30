package proj.blog.myblog.models;

import jakarta.persistence.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Document(value = "blogposts")
public class Post {
    @Id
    @Field("post_id")
    private ObjectId postId;

    @Field("create_at")
    @CreatedDate
    private LocalDateTime createdDate;

    @Field("modified_at")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Field("text")
    private String text;

    @Field("title")
    private String title;

    @Field("cats")
    private List<Long> categories;

}
