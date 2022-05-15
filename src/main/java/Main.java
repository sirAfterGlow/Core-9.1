import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static final String SERVICE_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static CloseableHttpClient httpClient() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5_000)
                        .setSocketTimeout(30_000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        return httpClient;
    }

    public static List<CatFact> serialiseCatFacts(CloseableHttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<CatFact> catFacts = new ArrayList<>();
        catFacts = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<CatFact>>() {});

        return catFacts;
    }

    public static void main(String[] args) {

        HttpGet request = new HttpGet(SERVICE_URL);
        List<CatFact> recivedFacts = new ArrayList<>();


        try {
            CloseableHttpResponse response = httpClient().execute(request);
            recivedFacts = serialiseCatFacts(response);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        List<CatFact> factsWithUpvotes = recivedFacts.stream()
                        .filter(upvotes -> upvotes.getUpvotes() != null)
                        .collect(Collectors.toList());

        factsWithUpvotes.forEach(System.out::println);
    }
}
