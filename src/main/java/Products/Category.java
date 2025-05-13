package Products;

public enum Category {
    Food(0.3),
    NonFood(0.2);

    private final double markup;

    Category(double markup) {
        this.markup=markup;
    }

    public double getMarkup() {
        return markup;
    }
}
