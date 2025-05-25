package domain.product;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private String name;
    private final Category category;


    public Product(String name, Category category) {
        id =  UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public Category getCategory(){
        return category;
    }

    public void setName(String newName){
        name = newName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product that)) return false;
        return name.equals(that.name) && category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category);
    }
}
