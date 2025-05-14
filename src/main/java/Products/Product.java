package Products;

import java.math.BigDecimal;
import java.util.UUID;


public class Product {
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

}
