package proj.blog.myblog.exceptions;

public class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(){
        super("Category not found!");
    }

    public CategoryNotFoundException(String msg){
        super(msg);
    }
}
