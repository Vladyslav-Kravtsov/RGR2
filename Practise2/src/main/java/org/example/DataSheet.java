package org.example;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class DataSheet {
    private Document doc;
    public DataSheet(Document doc) {
        super();
        this.doc = doc;
    }

    public DataSheet() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        doc = docBuilder.newDocument();
        doc.appendChild(doc.createElement("dataSheetClassTest"));
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public int numData() {
        return doc.getDocumentElement().getElementsByTagName("data").getLength();
    }

    public String getDate(int pos) {
        return doc.getElementsByTagName("data").item(pos).getAttributes().item(0).getTextContent();
    }

    public void setDate(String date, int pos) {
        doc.getElementsByTagName("data").item(pos).getAttributes().item(0).setTextContent(String.valueOf(date));
    }

    public double getX(int pos) {
        return Double.parseDouble(doc.getDocumentElement().getElementsByTagName("x").item(pos).getTextContent());
    }

    public void setX(int pos, double val) {
        doc.getDocumentElement().getElementsByTagName("x").item(pos).setTextContent(String.valueOf(val));
    }

    public double getY(int pos) {
        return Double.parseDouble(doc.getDocumentElement().getElementsByTagName("y").item(pos).getTextContent());
    }

    public void setY(int pos, double val) {
        doc.getDocumentElement().getElementsByTagName("y").item(pos).setTextContent(String.valueOf(val));
    }

    public Element newElement(String date, double x, double y) {
        Element data = doc.createElement("data");
        Attr attr = doc.createAttribute("date");
        attr.setValue(date.trim());
        data.setAttributeNode(attr);
        Element elemX = doc.createElement("x");
        elemX.appendChild(doc.createTextNode(String.valueOf(x)));
        data.appendChild(elemX);
        Element elemY = doc.createElement("y");
        elemY.appendChild(doc.createTextNode(String.valueOf(y)));
        data.appendChild(elemY);
        return data;
    }

    public Element newElement() {
        Element data = doc.createElement("data");
        Attr attr = doc.createAttribute("date");
        attr.setValue("");
        data.setAttributeNode(attr);
        Element elemX = doc.createElement("x");
        elemX.appendChild(doc.createTextNode("0"));
        data.appendChild(elemX);
        Element elemY = doc.createElement("y");
        elemY.appendChild(doc.createTextNode("0"));
        data.appendChild(elemY);
        return data;
    }

    public void addElement(Element data) {
        this.doc.getDocumentElement().appendChild(data);
    }

    public void removeElement(int pos) {
        Node el = doc.getDocumentElement().getElementsByTagName("data").item(pos);
        doc.getDocumentElement().removeChild(el);
    }

    public void insertElementAt(int pos, Node nd) {
        Node el = doc.getDocumentElement().getElementsByTagName("data").item(pos);
        doc.getDocumentElement().insertBefore(nd, el);
    }

    public void replaceElementAt(int pos, Node nd) {
        Node el = doc.getDocumentElement().getElementsByTagName("data").item(pos);
        doc.getDocumentElement().replaceChild(nd, el);
    }

    public void printAll() {
        System.out.println("Root element " + doc.getDocumentElement().getTagName());
        for (Node child = doc.getDocumentElement().getFirstChild(); child != null; child = child.getNextSibling()) {
            System.out.println(child.getNodeName() + " " + child.getAttributes().item(0).getNodeValue());
            System.out.println(child.getChildNodes().item(0).getNodeName() + " " + child.getChildNodes().item(0).getTextContent());
            System.out.println(child.getChildNodes().item(1).getNodeName() + " " + child.getChildNodes().item(1).getTextContent());
        }
    }


    public String getDataItem(int pos) {
        return doc.getElementsByTagName("data").item(pos).getAttributes().item(0).getTextContent();
    }

    public static Document SAXRead(File newFile) throws SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(newFile);
        } catch (ParserConfigurationException | SAXException | IOException er) {
            er.printStackTrace();
        }
        return doc;
    }

    public void saveDocument(File file) throws TransformerException {
        Transformer transformer = createTransformer();
        doc.setXmlStandalone(true);
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
        System.out.println("File saved!");
    }

    private Transformer createTransformer() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "Windows-1251");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        return transformer;
    }
}
