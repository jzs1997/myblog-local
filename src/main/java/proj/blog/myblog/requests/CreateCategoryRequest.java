package proj.blog.myblog.requests;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class CreateCategoryRequest {
    private String catName;
    private List<String> postIds;
}
