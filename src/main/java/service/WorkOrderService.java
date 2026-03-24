package service;

import entity.*;
import enums.Breakdowns;
import enums.Status;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WorkOrderService {

    public void create(WorkOrder order) {
        try {
            JSONObject json = new JSONObject();
            json.put("clientId", order.getClient().getId());
            json.put("masterId", order.getMaster().getId());
            json.put("priceId", order.getPrice().getId());
            json.put("finalPrice", order.getFinalPrice());
            json.put("breakdown", order.getBreakdowns().name());
            json.put("status", order.getCurrentStatus().name());

            HttpClient.sendRequest("/work-orders", "POST", json.toString());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании заказа", e);
        }
    }

    public List<WorkOrder> findAll() {
        try {
            String response = HttpClient.sendRequest("/work-orders", "GET", null);
            JSONArray jsonArray = new JSONArray(response);
            List<WorkOrder> orders = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                orders.add(mapJsonToWorkOrder(obj));
            }

            return orders;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении заказов", e);
        }
    }

    public void update(WorkOrder order) {
        try {
            JSONObject json = new JSONObject();
            json.put("clientId", order.getClient().getId());
            json.put("masterId", order.getMaster().getId());
            json.put("priceId", order.getPrice().getId());
            json.put("finalPrice", order.getFinalPrice());
            json.put("breakdown", order.getBreakdowns().name());
            json.put("status", order.getCurrentStatus().name());

            HttpClient.sendRequest("/work-orders/" + order.getId(), "PUT", json.toString());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении заказа", e);
        }
    }

    public void delete(long id) {
        try {
            HttpClient.sendRequest("/work-orders/" + id, "DELETE", null);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении заказа", e);
        }
    }

    private WorkOrder mapJsonToWorkOrder(JSONObject obj) {
        WorkOrder order = new WorkOrder();
        order.setId(obj.getLong("id"));

        if (obj.has("client")) {
            JSONObject clientObj = obj.getJSONObject("client");
            Client client = new Client();
            client.setId(clientObj.getLong("id"));
            client.setFirstName(clientObj.getString("firstName"));
            client.setLastName(clientObj.getString("lastName"));
            order.setClient(client);
        }

        if (obj.has("master")) {
            JSONObject masterObj = obj.getJSONObject("master");
            Master master = new Master();
            master.setId(masterObj.getLong("id"));
            master.setFirstName(masterObj.getString("firstName"));
            master.setLastName(masterObj.getString("lastName"));
            order.setMaster(master);
        }

        if (obj.has("price")) {
            JSONObject priceObj = obj.getJSONObject("price");
            Price price = new Price();
            price.setId(priceObj.getInt("id"));
            price.setNameOfService(priceObj.getString("nameOfService"));
            order.setPrice(price);
        }

        order.setFinalPrice((float) obj.getDouble("finalPrice"));
        order.setBreakdowns(Breakdowns.valueOf(obj.getString("breakdown")));
        order.setCurrentStatus(Status.valueOf(obj.getString("status")));

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        order.setCreatedAt(LocalDateTime.parse(obj.getString("createdAt").replace(" ", "T"), formatter));
        order.setUpdatedAt(LocalDateTime.parse(obj.getString("updatedAt").replace(" ", "T"), formatter));

        return order;
    }
}