package entity;

import javafx.beans.property.*;

public class Client {

    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty car = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final StringProperty info = new SimpleStringProperty();

    public long getId() { return id.get(); }
    public void setId(long id) { this.id.set(id); }
    public LongProperty idProperty() { return id; }

    public String getFirstName() { return firstName.get(); }
    public void setFirstName(String firstName) { this.firstName.set(firstName); }
    public StringProperty firstNameProperty() { return firstName; }

    public String getLastName() { return lastName.get(); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }
    public StringProperty lastNameProperty() { return lastName; }

    public String getCar() { return car.get(); }
    public void setCar(String car) { this.car.set(car); }
    public StringProperty carProperty() { return car; }

    public String getPhoneNumber() { return phoneNumber.get(); }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber.set(phoneNumber); }
    public StringProperty phoneNumberProperty() { return phoneNumber; }

    public String getInfo() { return info.get(); }
    public void setInfo(String info) { this.info.set(info); }
    public StringProperty infoProperty() { return info; }
}
