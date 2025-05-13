package Store;
import java.math.BigDecimal;
import java.util.UUID;
public class Cashier {
    private final String id;
    private final String name;
    private BigDecimal salary;

    public Cashier(String name, BigDecimal salary){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.salary = salary;
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public BigDecimal getSalary(){
        return salary;
    }
    public void setSalary(BigDecimal newSalary){
        this.salary = newSalary;
    }
}
