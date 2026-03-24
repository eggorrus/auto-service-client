package service;

import entity.Client;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ClientService {

    public void create(Client client) {
        try {
            JSONObject json = new JSONObject();
            json.put("firstName", client.getFirstName());
            json.put("lastName", client.getLastName());
            json.put("car", client.getCar());
            json.put("phoneNumber", client.getPhoneNumber());
            json.put("info", client.getInfo());

            HttpClient.sendRequest("/clients", "POST", json.toString());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании клиента", e);
        }
    }

    public List<Client> findAll() {
        try {
            String response = HttpClient.sendRequest("/clients", "GET", null);
            JSONArray jsonArray = new JSONArray(response);
            List<Client> clients = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                clients.add(mapJsonToClient(obj));
            }

            return clients;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении клиентов", e);
        }
    }

    public void update(Client client) {
        try {
            JSONObject json = new JSONObject();
            json.put("firstName", client.getFirstName());
            json.put("lastName", client.getLastName());
            json.put("car", client.getCar());
            json.put("phoneNumber", client.getPhoneNumber());
            json.put("info", client.getInfo());

            HttpClient.sendRequest("/clients/" + client.getId(), "PUT", json.toString());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении клиента", e);
        }
    }

    public void delete(long id) {
        try {
            HttpClient.sendRequest("/clients/" + id, "DELETE", null);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении клиента", e);
        }
    }

    private Client mapJsonToClient(JSONObject obj) {
        Client client = new Client();
        client.setId(obj.getLong("id"));
        client.setFirstName(obj.getString("firstName"));
        client.setLastName(obj.getString("lastName"));
        client.setCar(obj.getString("car"));
        client.setPhoneNumber(obj.getString("phoneNumber"));
        client.setInfo(obj.getString("info"));
        return client;
    }
}