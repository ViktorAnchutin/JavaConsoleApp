package model;

public class Project {

    private int id;
    private String name;

    public Project(String name)
    {
        this.name = name;
    }

    public Project(){}

    public int getId() {return id;}

    public Project setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return name ;
    }
}
