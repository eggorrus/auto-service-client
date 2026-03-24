package entity;

import enums.Specailization;
import javafx.beans.property.*;


public class Master {

    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final BooleanProperty active = new SimpleBooleanProperty();
    private final ObjectProperty<Specailization> specailization = new SimpleObjectProperty<>();

    public long getId() { return id.get(); }
    public void setId(long id) { this.id.set(id); }
    public LongProperty idProperty() { return id; }

    public String getFirstName() { return firstName.get(); }
    public void setFirstName(String firstName) { this.firstName.set(firstName); }
    public StringProperty firstNameProperty() { return firstName; }

    public String getLastName() { return lastName.get(); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }
    public StringProperty lastNameProperty() { return lastName; }

    public boolean isActive() { return active.get(); }
    public void setActive(boolean active) { this.active.set(active); }
    public BooleanProperty activeProperty() { return active; }

    public Specailization getSpecailization() { return specailization.get(); }
    public void setSpecailization(Specailization s) { this.specailization.set(s); }
    public ObjectProperty<Specailization> specailizationProperty() { return specailization; }
}
