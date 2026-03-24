package service;

import entity.Master;
import enums.Specailization;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MasterService {

    public void create(Master master) {
        try {
            JSONObject json = new JSONObject();
            json.put("firstName", master.getFirstName());
            json.put("lastName", master.getLastName());
            json.put("active", master.isActive());
            json.put("specialization", master.getSpecailization().name());

            HttpClient.sendRequest("/masters", "POST", json.toString());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании мастера", e);
        }
    }

    public List<Master> findAll() {
        try {
            String response = HttpClient.sendRequest("/masters", "GET", null);
            JSONArray jsonArray = new JSONArray(response);
            List<Master> masters = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                masters.add(mapJsonToMaster(obj));
            }

            return masters;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении мастеров", e);
        }
    }

    public void update(Master master) {
        try {
            JSONObject json = new JSONObject();
            json.put("firstName", master.getFirstName());
            json.put("lastName", master.getLastName());
            json.put("active", master.isActive());
            json.put("specialization", master.getSpecailization().name());

            HttpClient.sendRequest("/masters/" + master.getId(), "PUT", json.toString());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении мастера", e);
        }
    }

    public void delete(long id) {
        try {
            HttpClient.sendRequest("/masters/" + id, "DELETE", null);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении мастера", e);
        }
    }

    private Master mapJsonToMaster(JSONObject obj) {
        Master master = new Master();
        master.setId(obj.getLong("id"));
        master.setFirstName(obj.getString("firstName"));
        master.setLastName(obj.getString("lastName"));
        master.setActive(obj.getBoolean("active"));
        master.setSpecailization(Specailization.valueOf(obj.getString("specialization")));
        return master;
    }
}