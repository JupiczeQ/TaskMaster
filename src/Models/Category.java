package Models;

public class Category {
    private int id;
    private String name;
    private int userID;

    public Category(){}

    public Category(int id, String name, int userID){
        this.id = id;
        this.name = name;
        this.userID = userID;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return id == category.id;
    }
}
