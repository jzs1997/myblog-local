package proj.blog.myblog.requests;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CreatePostRequest implements Serializable {
    private String text;
    private String title;
    private List<Long> categoryIds;
}
