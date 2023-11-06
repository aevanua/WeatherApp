package com.example.background.services;

import com.example.background.entities.PartOfDayForecast;
import com.example.background.entities.PeipsiForecast;
import com.example.background.entities.PlaceForecast;
import com.example.background.repositories.PartOfDayForecastRepository;
import com.example.background.repositories.PeipsiForecastRepository;
import com.example.background.repositories.PlaceForecastRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDate;

@Service
public class ProcessService {
    private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);
    private final PartOfDayForecastRepository partOfDayForecastRepository;
    private final PeipsiForecastRepository peipsiForecastRepository;
    private final PlaceForecastRepository placeForecastRepository;
    private final RestTemplate restTemplate;

    private final String UrlOfData = "https://www.ilmateenistus.ee/ilma_andmed/xml/forecast.php?lang=eng";

    @Autowired
    public ProcessService(PartOfDayForecastRepository partOfDayForecastRepository, PeipsiForecastRepository peipsiForecastRepository, PlaceForecastRepository placeForecastRepository,  RestTemplate restTemplate)
    {
        this.partOfDayForecastRepository = partOfDayForecastRepository;
        this.peipsiForecastRepository = peipsiForecastRepository;
        this.placeForecastRepository = placeForecastRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void fetchWeatherData()
    {
        ResponseEntity<String> response = restTemplate.getForEntity(UrlOfData, String.class);
        if (response.getStatusCode() == HttpStatus.OK)
        {
            partOfDayForecastRepository.deleteAll();
            peipsiForecastRepository.deleteAll();
            placeForecastRepository.deleteAll();

            String xmlData = response.getBody();
            processWeatherData(xmlData);
        }
        else
        {
            logger.error("Error fetching weather data from {} - status {}", UrlOfData, response.getStatusCode());
        }
    }

    public void processWeatherData(String xmlData) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(xmlData)));

            NodeList forecastNodes = doc.getElementsByTagName("forecast");
            for (int i = 0; i < forecastNodes.getLength(); i++) {
                processForecastNode(forecastNodes.item(i));
            }
        } catch (Exception e) {
            logger.error("Error processing weather data", e);
        }
    }

    private void processForecastNode(Node forecastNode) {
        if (forecastNode.getNodeType() == Node.ELEMENT_NODE) {
            Element forecastElement = (Element) forecastNode;
            LocalDate date = LocalDate.parse(forecastElement.getAttribute("date"));
            processPartOfDayForecast(
                    (Element) forecastElement.getElementsByTagName("night").item(0), "night", date);
            processPartOfDayForecast(
                    (Element) forecastElement.getElementsByTagName("day").item(0), "day", date);
        }
    }

    private void processPartOfDayForecast(Element element, String partOfDay, LocalDate date) {
        String phenomenon = element.getElementsByTagName("phenomenon").item(0).getTextContent();
        int tempMin = Integer.parseInt(element.getElementsByTagName("tempmin").item(0).getTextContent());
        int tempMax = Integer.parseInt(element.getElementsByTagName("tempmax").item(0).getTextContent());
        String text = element.getElementsByTagName("text").item(0).getTextContent();

        PartOfDayForecast partOfDayForecast = new PartOfDayForecast(partOfDay, date, phenomenon, text, tempMin, tempMax);

        NodeList placeNodes = element.getElementsByTagName("place");
        for (int j = 0; j < placeNodes.getLength(); j++) {
            processPlaceForecast((Element) placeNodes.item(j), partOfDay, date);
        }

        if (element.getElementsByTagName("peipsi").getLength() > 0) {
            processPeipsiForecast((Element) element.getElementsByTagName("peipsi").item(0), partOfDay, date);
        }

        partOfDayForecastRepository.save(partOfDayForecast);
    }

    private void processPlaceForecast(Element placeElement, String partOfDay, LocalDate date) {
        placeForecastRepository.save(new PlaceForecast(
                partOfDay,
                date,
                placeElement.getElementsByTagName("name").item(0).getTextContent(),
                placeElement.getElementsByTagName("phenomenon").item(0).getTextContent(),
                getIntegerValue(placeElement, "tempmin"),
                getIntegerValue(placeElement, "tempmax")
        ));
    }

    private Integer getIntegerValue(Element element, String tagName) {
        NodeList nodes = element.getElementsByTagName(tagName);
        return nodes.getLength() > 0 ? Integer.parseInt(nodes.item(0).getTextContent()) : null;
    }

    private void processPeipsiForecast(Element element, String partOfDay, LocalDate date) {
        peipsiForecastRepository.save(new PeipsiForecast(
                partOfDay,
                date,
                element.getTextContent()
        ));
    }

}
