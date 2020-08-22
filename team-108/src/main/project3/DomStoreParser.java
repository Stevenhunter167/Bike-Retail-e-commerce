package main.project3;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import main.DB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomStoreParser {
    private String xmlFile;
    private HashSet<String> names;
    private ArrayList<Store> stores;
    private ArrayList<String> duplicateNames;
    private int missingName;
    Document dom;

    DomStoreParser(String xmlFile) {
        this.xmlFile = xmlFile;
        stores = new ArrayList<>();
        names = new HashSet<>();
        duplicateNames = new ArrayList<>();
        missingName = 0;
    }

    public void run() {
        //parse the xml file and get the dom object
        parseXmlFile();

        //get each employee element and create a Store object
        parseDocument();

        //Iterate through the list and print the data
        printData();

        //Store the data into db
        storeInDB();
    }


    private void parseXmlFile() {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse(xmlFile);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    private void parseDocument() {
        //get the root elememt
        Element docEle = dom.getDocumentElement();

        //get a nodelist of <store> elements
        NodeList nl = docEle.getElementsByTagName("store");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the store element
                Element el = (Element) nl.item(i);

                //get the store object
                Store e = getStore(el);

                if (e == null) {
                    continue;
                }

                // check duplicate
                if (names.contains(e.getName())) {
                    duplicateNames.add(e.getName());
                    continue;
                }

                //add it to list
                if (e != null) {
                    stores.add(e);
                    names.add(e.getName());
                }
            }
        }
    }

    /**
     * I take an store element and read the values in, create
     * an Store object and return it
     *
     * @param storeEl
     * @return
     */
    private Store getStore(Element storeEl) {

        //for each <store> element get text or int values of
        //name ,phone, email and address
        String name = ParserUtils.getTextValue(storeEl, "name");
        String phone = ParserUtils.getTextValue(storeEl, "phone");
        String email = ParserUtils.getTextValue(storeEl, "email");
        String address = ParserUtils.getTextValue(storeEl, "address");

        if (name == null) {
            missingName++;
            return null;
        }

        //Create a new Store with the value read from the xml nodes
        Store s = new Store(name, phone, email, address);

        return s;
    }


    private void printData() {
        Iterator<Store> it = stores.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
        System.out.println("No of Store '" + stores.size() + "'.");
        System.out.println();
        System.out.println("Duplicate Names:");
        for (int i = 0; i < duplicateNames.size(); ++i) {
            System.out.println(duplicateNames.get(i));
        }
        System.out.println();
        System.out.println("# of missing data entries: " + missingName);
    }

    private void storeInDB() {
        StringBuilder insertSQL = new StringBuilder(3000000);
        insertSQL.append("INSERT INTO stores(store_name, phone, email, address) VALUES");
        for (int i = 0; i < stores.size(); ++i) {
            insertSQL.append("('" + stores.get(i).getName() + "', '" +
                    stores.get(i).getPhone() + "', '" +
                    stores.get(i).getEmail() + "', '" +
                    stores.get(i).getAddress() + "')");
            if (i != stores.size()-1) {
                insertSQL.append(", ");
            } else {
                insertSQL.append(";");
            }
        }

        DB.update(insertSQL.toString());
    }

    public static void main(String[] args) {
        DomStoreParser dsp = new DomStoreParser("Stores.xml");
        dsp.run();
    }

}
