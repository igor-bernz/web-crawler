package com.likhina;

import com.likhina.exception.InvalidArgumentException;
import com.likhina.exception.OriginElementNotFound;
import com.likhina.exception.TargetElementNotFound;
import com.likhina.service.SearchElementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebCrawler {
    private static Logger LOGGER = LoggerFactory.getLogger(WebCrawler.class);

    public static void main(String[] args) throws InvalidArgumentException, OriginElementNotFound, TargetElementNotFound {
        if (args.length != 2) {
            LOGGER.error("Invalid input parameters [{}]", args);
            throw new InvalidArgumentException();
        }

        var searchElementService = new SearchElementService();
        var targetElementPath = searchElementService.searchTargetElementPathByOriginElement(args[0], args[1]);
        System.out.println(String.format("Found element XML path: [%s]", targetElementPath));
    }
}
