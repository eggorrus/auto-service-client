package service;

import entity.Price;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PriceService {

    public void create(Price price) {
        try {
            JSONObject json = new JSONObject();
            json.put("nameOfService", price.getNameOfService());
            json.put("price", price.getPrice());

            HttpClient.sendRequest("/prices", "POST", json.toString());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании услуги", e);
        }
    }

    public List<Price> findAll() {
        try {
            String response = HttpClient.sendRequest("/prices", "GET", null);
            JSONArray jsonArray = new JSONArray(response);
            List<Price> prices = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                prices.add(mapJsonToPrice(obj));
            }

            return prices;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении услуг", e);
        }
    }

    public void update(Price price) {
        try {
            JSONObject json = new JSONObject();
            json.put("nameOfService", price.getNameOfService());
            json.put("price", price.getPrice());

            HttpClient.sendRequest("/prices/" + price.getId(), "PUT", json.toString());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении услуги", e);
        }
    }

    public void delete(long id) {
        try {
            HttpClient.sendRequest("/prices/" + id, "DELETE", null);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении услуги", e);
        }
    }

    private Price mapJsonToPrice(JSONObject obj) {
        Price price = new Price();
        price.setId(obj.getInt("id"));
        price.setNameOfService(obj.getString("nameOfService"));
        price.setPrice((float) obj.getDouble("price"));
        return price;
    }
}