package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.List;

public class Runner {
    static final String link = "https://www.aboutyou.de/maenner/bekleidung";
    static final String itemXPath = "//a[@data-test-id='ProductTile' and @class='sc-1qheze-0 dgBQdu']";
    static Set<Item> elements;
    static ChromeDriver driver;
    static int pageHeight;

    public static void main(String[] args) throws AWTException {
        parseMainLink();

    }

    private static void parseMainLink() {
        System.setProperty("webdriver.chrome.driver", "chromedriver/chromedriver.exe");
        driver = new ChromeDriver();

        setSettings();
        elements = scrollPageDown();

        driver.quit();


        List<Thread> threads = new ArrayList<>();
        for (Item element : elements) {
            threads.add(new Thread(new FillingThread(element)));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        writeToFile();

        System.out.println("Amount of triggered HTTP requests: " + (elements.size() + 1));
        System.out.println("Amount of extracted products: " + elements.size());

    }

    private static void writeToFile() {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(writer, elements);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter fw = new FileWriter("result.json");
            fw.write(writer.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setSettings() {
        driver.manage().window().maximize();
        driver.get(link);
        zoomOut(7);
        pageHeight = Integer.parseInt(((Object)(driver.executeScript("return document.body.scrollHeight"))).toString());
    }

    private static Set<Item> scrollPageDown() {
        Set<Item> links = new HashSet<Item>();
        int step = 4000;
        int scrollTo = step;

        while (scrollTo < pageHeight) {
            try {
                for (WebElement element : driver.findElementsByXPath(itemXPath)) {
                    links.add(new Item(Integer.parseInt(element.getAttribute("id")), element.getAttribute("href")));
                }
            } catch (Exception e) {
                for (WebElement element : driver.findElementsByXPath(itemXPath)) {
                    links.add(new Item(Integer.parseInt(element.getAttribute("id")), element.getAttribute("href")));
                }
            }

            driver.executeScript("window.scrollTo(0, " + scrollTo + ");");
            scrollTo += step;
        }

        return links;
    }

    private static void zoomOut(int count) {
        try {
            Robot robot = new Robot();

            for (int i = 0; i < count; i++) {
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_SUBTRACT);
                robot.keyRelease(KeyEvent.VK_SUBTRACT);
                robot.keyRelease(KeyEvent.VK_CONTROL);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
