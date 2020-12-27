package com.erayerdem.calories;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class Contr {

    @Autowired
    private WebClient webClient;


    @Value("${apiUrl}")
    private String googleApiUrl;

    @Value("${xpath}")
    private String xpath;



    private static Map<String ,Double > cache = new HashMap<>();

    @PostMapping
    public Resp api(@RequestBody Req req )  {

        AtomicReference<Double> sum  = new AtomicReference<>(0.0);

        Map<String,Double> objects = new HashMap<>();


        req.getFoods().forEach(foodName ->  {

            Double aDouble = cache.get(foodName);
            if ( aDouble !=null) {

                sum.updateAndGet(v -> v + aDouble);
                objects.put(foodName,aDouble);

            }

            else  {
            String query = foodName + " kaç kalori ";
            String format = String.format(googleApiUrl, query);

            HtmlPage page = null;
            try {
                page = webClient.getPage(format);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Pattern pattern = Pattern.compile("span>\\d{2,4}<");
            Matcher matcher = pattern.matcher(page.getWebResponse().getContentAsString());

            if (matcher.find()) {

                String group = matcher.group(0);
                String replace = group.replace("span>", "");
                replace = replace.replace("<","");
                String finalReplace = replace;
                double v1 = Double.parseDouble(finalReplace);

                sum.updateAndGet(v -> v + v1);
                objects.put(foodName,v1);
                cache.put(foodName,v1);

            }}



        });

        Resp resp = new Resp();
        resp.setSumOfCalorie(sum.get());
        resp.setFoodCalorie(objects);

        return resp;
    }
}
