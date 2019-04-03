package org.geosolutions.datexgml;

import java.io.File;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class GmlConverterTest {

    GmlConverter getConverter() {
        try {
            return new GmlConverter(
                    new File(this.getClass().getResource("datex_2.3.xsd").toURI()),
                    Arrays.asList("Situation"),
                    GmlConverter.DATEX_NS);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSituationNode() {
        GmlConverter conv = getConverter();
        Node situationNode = conv.getSituationRootNode();
        assertTrue(situationNode != null);
    }

    /** Generates an output xml, use this for to get a file and check */
    @Test
    public void testOutputFile() throws Exception {
        GmlConverter conv = getConverter();
        conv.convert();
        Document doc = conv.getResultDoc();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.getBuffer().toString();
        //        FileUtils.writeStringToFile(new File("/home/fernando/Documents/datex_result.xsd"),
        // output,
        //                StandardCharsets.UTF_8);
        // System.out.print(output);
    }

    @Test
    public void testSituationToMap() throws Exception {
        GmlConverter conv = getConverter();
        conv.treeToComplexMap("Situation");
        Map<String, ComplexType> map = conv.getComplexMap();
        assertEquals(42, map.size());
    }
}
