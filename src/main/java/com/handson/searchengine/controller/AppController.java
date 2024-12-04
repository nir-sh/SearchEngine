package com.handson.searchengine.controller;


import com.handson.searchengine.crawler.Crawler;
import com.handson.searchengine.model.CrawlStatus;
import com.handson.searchengine.model.CrawlStatusOut;
import com.handson.searchengine.model.CrawlerRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class AppController {

    protected final Log logger = LogFactory.getLog(getClass());
    private static final int ID_LENGTH = 6;
    private Random random = new Random();
    @Autowired
    Crawler crawler;

    @RequestMapping(value = "/crawl", method = RequestMethod.POST)
    public CrawlStatusOut crawl(@RequestBody CrawlerRequest request) throws IOException, InterruptedException {
        try {
            String crawlId = generateCrawlId();
            if (!request.getUrl().startsWith("http")) {
                request.setUrl("https://" + request.getUrl());
            }
            CrawlStatus res = crawler.crawl(crawlId, request);
            return CrawlStatusOut.of(res);

        } catch (IOException e) {
            logger.error("IOException occurred during crawling", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CrawlStatusOut()).getBody();
        } catch (InterruptedException e) {
            logger.error("InterruptedException occurred during crawling", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CrawlStatusOut()).getBody();
        } catch (Exception e) {
            // Generic catch-all to ensure you catch unexpected exceptions
            logger.error("Unexpected error occurred during crawling", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CrawlStatusOut()).getBody();
        }
    }

    private String generateCrawlId() {
        String charPool = "ABCDEFHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < ID_LENGTH; i++) {
            res.append(charPool.charAt(random.nextInt(charPool.length())));
        }
        return res.toString();
    }
}
