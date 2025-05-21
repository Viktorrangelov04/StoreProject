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

    @Test
    void equals_ShouldReturnTrue_ForSameValues() {
        Product a = new Product("Banana", Category.Food);
        Product b = new Product("Banana", Category.Food);
        assertEquals(a, b);
    }

    @Test
    void equals_ShouldReturnFalse_ForDifferentName() {
        Product a = new Product("Banana", Category.Food);
        Product b = new Product("Apple", Category.Food);
        assertNotEquals(a, b);
    }

    @Test
    void equals_ShouldReturnFalse_ForDifferentCategory() {
        Product a = new Product("Banana", Category.Food);
        Product b = new Product("Banana", Category.NonFood);
        assertNotEquals(a, b);
    }

    @Test
    void equals_ShouldReturnFalse_ForNull() {
        Product a = new Product("Banana", Category.Food);
        assertNotEquals(null, a);
    }

    @Test
    void equals_ShouldReturnFalse_ForDifferentType() {
        Product a = new Product("Banana", Category.Food);
        assertNotEquals("not a product", a);
    }

    @Test
    void hashCode_ShouldMatch_ForEqualProducts() {
        Product a = new Product("Banana", Category.Food);
        Product b = new Product("Banana", Category.Food);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        Product a = new Product("Banana", Category.Food);
        int hash1 = a.hashCode();
        int hash2 = a.hashCode();
        assertEquals(hash1, hash2);
    }
}