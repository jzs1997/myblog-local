package proj.blog.myblog.exceptions;

public class SortTypeInvalidException extends Exception{
    public SortTypeInvalidException(){
        super("parameter sort_type invalid. \n Usage: \n0: sort by created_at \n1:sort by modified_at");
    }
}
