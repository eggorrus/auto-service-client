package entity;

import javafx.beans.property.*;

public class Price {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nameOfService = new SimpleStringProperty();
    private final FloatProperty price = new SimpleFloatProperty();

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNameOfService() { return nameOfService.get(); }
    public void setNameOfService(String name) { this.nameOfService.set(name); }
    public StringProperty nameOfServiceProperty() { return nameOfService; }

    public float getPrice() { return price.get(); }
    public void setPrice(float price) { this.price.set(price); }
    public FloatProperty priceProperty() { return price; }
}
