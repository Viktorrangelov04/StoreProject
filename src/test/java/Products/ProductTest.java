package Products;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void getId() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        //assert
        assertNotNull(product.getId());
    }

    @Test
    public void getName() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        //Act
        String name = product.getName();
        //Assert
        assertEquals("Apple", name);
    }

    @Test
    public void getCategory() {
        //Assert
        Product product = new Product("Apple", Category.Food);
        //Act
        Category category = product.getCategory();
        //Assert
        assertEquals(Category.Food, category);
    }

    @Test
    public void setName() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        //Act
        product.setName("Orange");
        String name = product.getName();
        //Assert
        assertEquals("Orange", name);
    }
}