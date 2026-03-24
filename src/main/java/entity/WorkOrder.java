package entity;

import enums.Breakdowns;
import enums.Status;
import javafx.beans.property.*;

import java.time.LocalDateTime;

public class WorkOrder {

    private final LongProperty id = new SimpleLongProperty();
    private final ObjectProperty<Breakdowns> breakdowns = new SimpleObjectProperty<>();
    private final ObjectProperty<Client> client = new SimpleObjectProperty<>();
    private ObjectProperty<Price> price = new SimpleObjectProperty<>();
    private FloatProperty finalPrice = new SimpleFloatProperty();
    private final ObjectProperty<Status> currentStatus = new SimpleObjectProperty<>();
    private final ObjectProperty<Master> master = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> updatedAt = new SimpleObjectProperty<>();

    public long getId() { return id.get(); }
    public void setId(long id) { this.id.set(id); }
    public LongProperty idProperty() { return id; }

    public Breakdowns getBreakdowns() { return breakdowns.get(); }
    public void setBreakdowns(Breakdowns b) { breakdowns.set(b); }
    public ObjectProperty<Breakdowns> breakdownsProperty() { return breakdowns; }

    public Client getClient() { return client.get(); }
    public void setClient(Client client) { this.client.set(client); }
    public ObjectProperty<Client> clientProperty() { return client; }

    public Price getPrice() { return price.get(); }
    public void setPrice(Price price) { this.price.set(price); }
    public ObjectProperty<Price> priceProperty() { return price; }

    public float getFinalPrice() { return finalPrice.get(); }
    public void setFinalPrice(float finalPrice) { this.finalPrice.set(finalPrice); }
    public FloatProperty finalPriceProperty() { return finalPrice; }


    public Status getCurrentStatus() { return currentStatus.get(); }
    public void setCurrentStatus(Status status) { currentStatus.set(status); }
    public ObjectProperty<Status> currentStatusProperty() { return currentStatus; }

    public Master getMaster() { return master.get(); }
    public void setMaster(Master master) { this.master.set(master); }
    public ObjectProperty<Master> masterProperty() { return master; }

    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(LocalDateTime t) { createdAt.set(t); }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt.get(); }
    public void setUpdatedAt(LocalDateTime t) { updatedAt.set(t); }
    public ObjectProperty<LocalDateTime> updatedAtProperty() { return updatedAt; }
}
