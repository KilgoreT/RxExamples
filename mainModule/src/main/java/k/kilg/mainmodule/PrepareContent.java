package k.kilg.mainmodule;


import android.annotation.TargetApi;
import android.os.Build;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class PrepareContent {

    private static final String PATH_TO_OPERATORS = "rxModule/src/main/java/k/kilg/rxmodule/Operators";
    private static final String PATH_TO_XML = "mainModule/src/main/res/values/content.xml";
    private static final String REGEX_CLASS = "public class (.+?) ";
    private static final String REGEX_TITLE = "(//@tagName: )(.+)";
    private static final String REGEX_CATEGORY = "(//@tagCategory: )(.+)";
    private static final String REGEX_CODE = "return\\s(.+?);";
    private static final String REGEX_DOCS = "\\/*\\*.+?\\*\\/";

    private static final String CLASS_NAME_KEY = "class_name_key";
    private static final String TITLE_KEY = "title_key";
    private static final String CATEGORY_KEY = "category_key";
    private static final String DOCS_KEY = "docs_key";
    private static final String CODE_KEY = "code_key";

    private static DocumentBuilder docBuilder;
    private static Document document;
    private static Element root;


    public static void main(String[] args) {

        List<String> operatorNames = new ArrayList<>();
        List<String> files = new ArrayList<>();
        String path = BuildConfig.PATH_ROOT + PATH_TO_OPERATORS;
        File folder = new File(path);
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                files.add(getFileContent(file));
            }
        }

        root = createDoc();

        for (String file : files) {
            Map<String, String> properties = new HashMap<>();
            operatorNames.add(getTitle(file));
            properties.put(CLASS_NAME_KEY, getClassName(file));
            properties.put(TITLE_KEY, getTitle(file));
            properties.put(CATEGORY_KEY, getCategory(file));
            properties.put(DOCS_KEY, getDocs(file));
            properties.put(CODE_KEY, getCode(file));
            addOperatorItem(properties);
        }

        addOperatorList(operatorNames);
        System.out.println(printDoc());
        saveDoc();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static String getFileContent(File file) {
        byte[] encoded = null;
        try {
            encoded = Files.readAllBytes(Paths.get(file.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded);
    }

    private static String getClassName(String file) {
        Pattern pattern = Pattern.compile(REGEX_CLASS);
        Matcher matcher = pattern.matcher(file);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private static String getTitle(String file) {
        Pattern pattern = Pattern.compile(REGEX_TITLE);
        Matcher matcher = pattern.matcher(file);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return "";
    }

    private static String getCategory(String file) {
        Pattern pattern = Pattern.compile(REGEX_CATEGORY);
        Matcher matcher = pattern.matcher(file);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return "";
    }

    private static String getCode(String file) {
        Pattern pattern = Pattern.compile(REGEX_CODE, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(file);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private static String getDocs(String file) {
        Pattern pattern = Pattern.compile(REGEX_DOCS, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(file);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    private static DocumentBuilder getDocBuilger() {
        if (docBuilder == null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                docBuilder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        return docBuilder;
    }

    private static Element createDoc() {

        document = getDocBuilger().newDocument();
        Element root = document.createElement("resources");
        document.appendChild(root);
        return root;

    }

    private static void addOperatorItem(Map<String, String> properties) {

        Element array = document.createElement("string-array");
        Attr arrayAttr = document.createAttribute("name");
        arrayAttr.setValue(properties.get(TITLE_KEY));
        array.setAttributeNode(arrayAttr);
        root.appendChild(array);

        Element className = document.createElement("item");
        className.appendChild(document.createTextNode(properties.get(CLASS_NAME_KEY)));
        array.appendChild(className);

        Element category = document.createElement("item");
        category.appendChild(document.createTextNode(properties.get(CATEGORY_KEY)));
        array.appendChild(category);

        Element docs = document.createElement("item");
        docs.appendChild(document.createTextNode(properties.get(DOCS_KEY)));
        array.appendChild(docs);

        Element code = document.createElement("item");
        code.appendChild(document.createTextNode(properties.get(CODE_KEY)));
        array.appendChild(code);
    }

    private static void addOperatorList(List<String> operators) {

        Element array = document.createElement("string-array");
        Attr arrayAttr = document.createAttribute("name");
        arrayAttr.setValue("operators");
        array.setAttributeNode(arrayAttr);
        root.appendChild(array);

        for (String name : operators) {
            Element operatorName = document.createElement("item");
            operatorName.appendChild(document.createTextNode(name));
            array.appendChild(operatorName);
        }
    }

    private static String printDoc() {

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {

            transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.getBuffer().toString();

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static void saveDoc() {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;

        try {

            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(BuildConfig.PATH_ROOT + PATH_TO_XML));
            transformer.transform(source, result);

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }
}
