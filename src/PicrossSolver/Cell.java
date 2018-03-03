package PicrossSolver;

public class Cell{
    private boolean certain;
    private boolean value;

    public Cell(){
        certain = false;
    }

    public boolean isCertain(){
        return certain;
    }

    public boolean getValue(){
        return value;
    }

    public void setCertain(boolean certain){
        this.certain = certain;
    }

    public void setValue(boolean value){
        this.value = value;
    }
}