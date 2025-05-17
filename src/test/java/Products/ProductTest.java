package Products;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void getId() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        //assert
        assertNotNull(product.getId());
    }

    @Test
    void getName() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        //Act
        String name = product.getName();
        //Assert
        assertEquals("Apple", name);
    }

    @Test
    void getCategory() {
    }

    @Test
    void setName() {
    }
}