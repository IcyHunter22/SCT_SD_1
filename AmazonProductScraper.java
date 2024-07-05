import com.opencsv.CSVWriter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.paapi5.Paapi5Client;
import software.amazon.awssdk.services.paapi5.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AmazonProductScraper {

    public static void main(String[] args) {
        String accessKey = "YOUR_ACCESS_KEY";
        String secretKey = "YOUR_SECRET_KEY";
        String associateTag = "YOUR_ASSOCIATE_TAG";
        String[] productIds = {"B08N5WRWNW", "B07FZ8S74R"}; // Replace with actual ASINs

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        Paapi5Client client = Paapi5Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.US_EAST_1)
                .build();

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Name", "Price", "Rating"});

        try {
            GetItemsRequest itemsRequest = GetItemsRequest.builder()
                    .itemIds(productIds)
                    .partnerTag(associateTag)
                    .partnerType(PartnerType.ASSOCIATES)
                    .build();

            GetItemsResponse response = client.getItems(itemsRequest);

            for (Item item : response.itemsResult().items()) {
                String name = item.itemInfo().title().displayValue();
                String price = item.offers().listings().get(0).price().displayAmount();
                String rating = item.itemInfo().customerReviews().starRating().toString();

                data.add(new String[]{name, price, rating});
            }

            writeDataToCSV("amazon_products.csv", data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeDataToCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(data);
            System.out.println("Data successfully written to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}