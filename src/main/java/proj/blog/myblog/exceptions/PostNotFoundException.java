package proj.blog.myblog.exceptions;

public class PostNotFoundException extends Exception{
    public PostNotFoundException(){
        super("Post Not Found");
    }
}
