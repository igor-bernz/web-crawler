package com.likhina.service;

import com.likhina.exception.OriginElementNotFound;
import com.likhina.exception.TargetElementNotFound;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.likhina.util.DocumentUtil.findElementById;
import static com.likhina.util.DocumentUtil.findElementsAttributeValue;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class SearchElementService {

    private static final String PATH_DELIMITER = " > ";
    private static final String TARGET_ELEMENT_ID = "make-everything-ok-button";
    private static final String ATTR_TITLE = "title";
    private static final String ATTR_CLASS = "class";
    private static final String ATTR_HREF = "href";

    public String searchTargetElementPathByOriginElement(String pathOriginFile, String pathTargetFile) throws OriginElementNotFound, TargetElementNotFound {
        var originElement = getOriginElementById(pathOriginFile, TARGET_ELEMENT_ID);

        var originElementAttrs = originElement.attributes().asList().stream()
                .collect(toMap(Attribute::getKey, Attribute::getValue));

        var targetElementOpt = getTargetElementByAttr(pathTargetFile, originElementAttrs);
        var elementPathOpt = targetElementOpt.map(this::getElementPath);
        return elementPathOpt.orElseThrow(() -> new TargetElementNotFound());
    }

    private Element getOriginElementById(String pathOriginFile, String id) throws OriginElementNotFound {
        return findElementById(new File(pathOriginFile), TARGET_ELEMENT_ID)
                .orElseThrow(() -> new OriginElementNotFound(TARGET_ELEMENT_ID));
    }

    private Optional<Element> getTargetElementByAttr(String pathTargetFile, Map<String, String> originElementAttrs) {
        var elements = originElementAttrs.entrySet().stream()
                .map(entry -> findElementsAttributeValue(new File(pathTargetFile), entry.getKey(), entry.getValue()))
                .flatMap(Collection::stream)
                .collect(toList());

        return elements.stream()
                .filter(element -> filterByAttributes(element, originElementAttrs))
                .findFirst();
    }

    private boolean filterByAttributes(Element element, Map<String, String> originElementAttrs) {
        var targetElementAttributes = element.attributes().asList().stream()
                .collect(toMap(Attribute::getKey, Attribute::getValue));

        var containsTitle = elementContainsOriginValueByAttr(targetElementAttributes, originElementAttrs, ATTR_TITLE);
        var containsClass = elementContainsOriginValueByAttr(targetElementAttributes, originElementAttrs, ATTR_CLASS);
        var containsHref = elementContainsOriginValueByAttr(targetElementAttributes, originElementAttrs, ATTR_HREF);

        if (containsTitle && containsClass) return true;
        if (containsHref && containsClass) return true;
        if (containsTitle && containsHref) return true;
        if (containsClass) return true;
        if (containsTitle) return true;
        if (containsHref) return true;
        return false;
    }

    private boolean elementContainsOriginValueByAttr(Map<String, String> targetElementAttrs,
                                                     Map<String, String> originElementAttrs, String attr) {
        return originElementAttrs.get(attr).equals(targetElementAttrs.get(attr));
    }

    private String getElementPath(Element element) {
        var parentPath = element.parents().stream()
                .map(Element::nodeName)
                .collect(toList());
        Collections.reverse(parentPath);
        parentPath.add(element.nodeName());

        return String.join(PATH_DELIMITER, parentPath);
    }
}
