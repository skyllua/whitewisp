package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class FillingThread implements Runnable {
    private Item item;
    private Document doc;

    public FillingThread(Item item) {
        this.item = item;
    }

    @Override
    public void run() {
        try {
            doc = Jsoup.connect(item.getLink()).get();

            item.setBrand(doc.getElementsByClass("sc-1ybtkva-0 duhYnn").select("img[alt]").attr("alt"));
            item.setProductName(doc.getElementsByAttributeValue("data-test-id", "ProductName").get(0).text());

            // get price
            String price = doc.select(".sc-1kqkfaq-0").select(".x3voc9-0").get(0).text();
            if (price.split(" ")[0].equals("ab")) {
                item.setPrice(Float.parseFloat(price.split(" ")[1].replace(",", ".")));
                item.setCurrency(price.split(" ")[2]);
            } else {
                item.setPrice(Float.parseFloat(price.split(" ")[0].replace(",", ".")));
                item.setCurrency(price.split(" ")[1]);
            }

            // get colors
            Elements elemColors = doc.getElementsByAttributeValue("data-test-id", "ColorVariantColorInfo");
            for (Element elemColor : elemColors) {
                item.addColor(elemColor.text());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private float getPrice(Elements elements) {
        try {
            return Float.parseFloat(elements.text().split(" ")[0].replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println(item.getLink());
        }

        return 0;
    }

    private String getCurrency(Elements elements) {
        return elements.text().split(" ")[1];
    }
}
