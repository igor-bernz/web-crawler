package com.likhina;

import com.likhina.exception.InvalidArgumentException;
import com.likhina.exception.OriginElementNotFound;
import com.likhina.exception.TargetElementNotFound;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class WebCrawlerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testWebCrawlerSample1() throws InvalidArgumentException, OriginElementNotFound, TargetElementNotFound {
        var args = new String[]{"src/test/resources/samples/sample-0-origin.html", "src/test/resources/samples/sample-1-evil-gemini.html"};
        WebCrawler.main(args);
        assertEquals("Found element XML path: [html > body > div > div > div > div > div > div > a]\n", outContent.toString());
    }

    @Test
    public void testWebCrawlerSample2() throws InvalidArgumentException, OriginElementNotFound, TargetElementNotFound {
        var args = new String[]{"src/test/resources/samples/sample-0-origin.html", "src/test/resources/samples/sample-2-container-and-clone.html"};
        WebCrawler.main(args);
        assertEquals("Found element XML path: [html > body > div > div > div > div > div > div > div > a]\n", outContent.toString());
    }

    @Test
    public void testWebCrawlerSample3() throws InvalidArgumentException, OriginElementNotFound, TargetElementNotFound {
        var args = new String[]{"src/test/resources/samples/sample-0-origin.html", "src/test/resources/samples/sample-3-the-escape.html"};
        WebCrawler.main(args);
        assertEquals("Found element XML path: [html > body > div > div > div > div > div > div > a]\n", outContent.toString());
    }

    @Test
    public void testWebCrawlerSample4() throws InvalidArgumentException, OriginElementNotFound, TargetElementNotFound {
        var args = new String[]{"src/test/resources/samples/sample-0-origin.html", "src/test/resources/samples/sample-4-the-mash.html"};
        WebCrawler.main(args);
        assertEquals("Found element XML path: [html > body > div > div > div > div > div > div > a]\n", outContent.toString());
    }

    @Test
    public void testWebCrawlerInvalidParameters() {
        var args = new String[]{"src/test/resources/samples/sample-1-evil-gemini.html"};

        var exception = assertThrows(InvalidArgumentException.class, () -> WebCrawler.main(args));
        var expectedMessage = "App didn't get valid input parameters. It should be at least two (path to the files) parameters.";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void testWebCrawlerTargetElementNotFound() {
        var args = new String[]{"src/test/resources/samples/sample-0-origin.html", "src/test/resources/samples/sample-5.html"};

        var exception = assertThrows(TargetElementNotFound.class, () -> WebCrawler.main(args));
        var expectedMessage = "Target element wasn't found";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void testWebCrawlerInvalidParameterPath() {
        var args = new String[]{"test.html", "src/test/resources/samples/sample-1-evil-gemini.html"};

        var exception = assertThrows(OriginElementNotFound.class, () -> WebCrawler.main(args));
        var expectedMessage = "Origin element wasn't found by id make-everything-ok-button";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
