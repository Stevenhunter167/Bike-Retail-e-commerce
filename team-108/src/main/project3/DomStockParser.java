package main.project3;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import main.DB;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomStockParser {
    private String xmlFile;
    private HashMap<String, Integer> stores;
    private HashMap<String, Integer> products;
    private HashSet<Pair<Integer, Integer>> stocks;
    Document dom;

    DomStockParser(String xmlFile) {
        this.xmlFile = xmlFile;

        stores = new HashMap<>();
        JSONObject storesFromDB = DB.listAccess("SELECT store_id, store_name FROM stores;");
        for (int i = 0; i < storesFromDB.size(); ++i) {
            String sname = ((JSONObject) (storesFromDB.get(i))).get("store_name").toString();
            Integer sid =  Integer.valueOf(((JSONObject) (storesFromDB.get(i))).get("store_id").toString());
            stores.put(sname, sid);
        }

        products = new HashMap<>();
        JSONObject productsFromDB = DB.listAccess("SELECT product_id, product_name FROM products;");
        for (int i = 0; i < productsFromDB.size(); ++i) {
            String pname = ((JSONObject) (productsFromDB.get(i))).get("product_name").toString();
            Integer pid =  Integer.valueOf(((JSONObject) (productsFromDB.get(i))).get("product_id").toString());
            products.put(pname, pid);
        }

        stocks = new HashSet<>();
    }

    public void run() {
        //parse the xml file and get the dom object
        parseXmlFile();

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


    private void storeInDB() {
        StringBuilder insertSQL = new StringBuilder(8069600);
        insertSQL.append("INSERT INTO stocks(store_id, product_id, quantity) VALUES");
        Element docEle = dom.getDocumentElement();
        NodeList nl = docEle.getElementsByTagName("store");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                //get the store element
                Element storeEl = (Element) nl.item(i);

                String sname = ParserUtils.getTextValue(storeEl, "name");
                // if there is no such store in db
                if (!stores.containsKey(sname)) {
                    System.out.println("There is no store: '" + sname + "' in db.");
                    continue;
                }
                Element stockEl = ParserUtils.getSubEl(storeEl, "stock");
                if (stockEl == null) {
                    continue;
                }
                NodeList productsNl = stockEl.getElementsByTagName("product");
                if (productsNl != null && productsNl.getLength() > 0) {
                    for (int j = 0; j < productsNl.getLength(); ++j) {
                        Element productEl = (Element) productsNl.item(j);
                        String pname = ParserUtils.getTextValue(productEl, "pname");
                        int quantity = ParserUtils.getIntValue(productEl, "quantity");
                        // if there is no such product in db
                        if (!products.containsKey(pname)) {
                            System.out.println("There is no product: '" + pname + "' in db.");
                            continue;
                        }
                        // if the <store_id, product_id> already added
                        if (stocks.contains(new Pair<Integer, Integer>(stores.get(sname), products.get(pname)))) {
                            System.out.println("Already added Store: '" + sname + "' and product '" + pname + "'.");
                            continue;
                        }
                        insertSQL.append("(" + stores.get(sname) + ", " + products.get(pname) + ", " + quantity + "),");
                        stocks.add(new Pair<Integer, Integer>(stores.get(sname), products.get(pname)));
                    }
                }

            }
        }
        if (insertSQL.length() > 0) {
            insertSQL.deleteCharAt(insertSQL.length() - 1);
            insertSQL.append(";");
        }
        DB.update(insertSQL.toString());
    }

    public static void main(String[] args) {
        DomStockParser dsp = new DomStockParser("Stock.xml");
        dsp.run();
    }


}
