package proj.blog.myblog.models;

import jakarta.persistence.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue
    @Column(name="cat_id")
    private Long catId;

    @Column(name="cat_name")
    private String catName;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="modified_at")
    private LocalDateTime modifiedAt;

    // 24-byte hexadecimal String representation of ObjectId of Category
    @ElementCollection
    @Column(name="posts")
//    @CollectionTable(name = "category_posts", joinColumns = @JoinColumn(name = "category_cat_id"))
    private List<String> posts;

    @Override
    public String toString(){
        return this.getCatId() + this.getCatName() + " " + this.getCreatedAt() +" " + this.getModifiedAt();
    }
}
