package Smart_Carpooling.demo.Service;
import org.springframework.web.client.RestTemplate;
import org.json.*;
import org.springframework.stereotype.Service;

@Service
public class GeocodingService {
    private static final String NOMINATIM_URL =
            "https://nominatim.openstreetmap.org/search?q=%s&format=json&limit=1";

    public double[] getCoordinates(String address) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = String.format(NOMINATIM_URL, address.replace(" ", "+"));

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.add("User-Agent", "spring-boot-app");
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            org.springframework.http.ResponseEntity<String> response =
                    restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);

            JSONArray results = new JSONArray(response.getBody());

            if (results.isEmpty()) {
                throw new RuntimeException("Location not found: " + address);
            }

            JSONObject location = results.getJSONObject(0);
            double lat = Double.parseDouble(location.getString("lat"));
            double lon = Double.parseDouble(location.getString("lon"));

            return new double[]{lat, lon};
        } catch (Exception e) {
            throw new RuntimeException("Error fetching coordinates: " + e.getMessage(), e);
        }
    }
}
