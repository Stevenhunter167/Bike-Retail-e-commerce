package main.project3;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import main.DB;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomBicycleParser {
    private String xmlFile;
    private HashSet<String> names;
    private ArrayList<Bicycle> bicycles;
    private HashMap<String, Integer> brands;
    private HashMap<String, Integer> categories;
    private ArrayList<String> duplicateNames;
    private HashSet<String> newBrands;
    private int missingName;
    Document dom;

    DomBicycleParser(String xmlFile) {
        this.xmlFile = xmlFile;
        bicycles = new ArrayList<>();
        names = new HashSet<>();
        duplicateNames = new ArrayList<>();
        newBrands = new HashSet<>();
        missingName = 0;

        // get all the brands from db
        brands = new HashMap<>();
        JSONObject brandsFromDB = DB.listAccess("SELECT * FROM brands;");
        for (int i = 0; i < brandsFromDB.size(); ++i) {
            String bname = ((JSONObject) (brandsFromDB.get(i))).get("brand_name").toString();
            Integer bid =  Integer.valueOf(((JSONObject) (brandsFromDB.get(i))).get("brand_id").toString());
            brands.put(bname, bid);
        }

        // get all the categories from db
        categories = new HashMap<>();
        JSONObject categoriesFromDB = DB.listAccess("SELECT * FROM categories;");
        for (int i = 0; i < categoriesFromDB.size(); ++i) {
            String cname = ((JSONObject) (categoriesFromDB.get(i))).get("category_name").toString();
            Integer cid =  Integer.valueOf(((JSONObject) (categoriesFromDB.get(i))).get("category_id").toString());
            categories.put(cname, cid);
        }
    }

    public void run() {
        //parse the xml file and get the dom object

        parseXmlFile();

        //get each employee element and create a Bicycle object
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

        //get a nodelist of <bicycle> elements
        NodeList nl = docEle.getElementsByTagName("bicycle");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the store element
                Element el = (Element) nl.item(i);

                //get the store object
                Bicycle e = getBicycle(el);

                if (e == null) {
                    continue;
                }

                if (!brands.containsKey(e.getBrand())) {
                    newBrands.add(e.getBrand());
                }
                if (!categories.containsKey(e.getCategory())) {
                }


                // check duplicate
                if (names.contains(e.getName())) {
                    duplicateNames.add(e.getName());
                    continue;
                }

                //add it to list
                if (e != null) {
                    bicycles.add(e);
                    names.add(e.getName());
                }
            }
        }
    }

    /**
     * I take an bicycle element and read the values in, create
     * an Bicycle object and return it
     *
     * @param bikeEl
     * @return
     */
    private Bicycle getBicycle(Element bikeEl) {

        String name = ParserUtils.getTextValue(bikeEl, "name");
        String brand = ParserUtils.getTextValue(bikeEl, "brand");
        String category = ParserUtils.getTextValue(bikeEl, "cat");
        int year = ParserUtils.getIntValue(bikeEl, "year");
        double price = ParserUtils.getDoubleValue(bikeEl, "price");
        double rating = ParserUtils.getDoubleValue(bikeEl, "rating");
        int vote = ParserUtils.getIntValue(bikeEl, "vote");

        if (name == null) {
            missingName++;
            return null;
        }

        //Create a new Bicycle with the value read from the xml nodes
        Bicycle b = new Bicycle(name, brand, category, year, price, rating, vote);

        return b;
    }


    private void printData() {
        Iterator<Bicycle> it = bicycles.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
        System.out.println("No of Bicycle '" + bicycles.size() + "'.");
        System.out.println();
        System.out.println("Duplicate names:");
        for (int i = 0; i < duplicateNames.size(); ++i) {
            System.out.println(duplicateNames.get(i));
        }
        System.out.println();
        System.out.println("New Brands:");
        for (String b: newBrands) {
            System.out.println(b);
        }
        System.out.println();
        System.out.println("# of missing data entries: " + missingName);
    }

    private void storeInDB() {
        StringBuilder insertSQL = new StringBuilder(6500000);
        insertSQL.append("INSERT INTO products(product_name, brand_id, category_id, model_year, list_price) VALUES");
        for (int i = 0; i < bicycles.size(); ++i) {
            insertSQL.append("('" + bicycles.get(i).getName() + "', ");

            String bname = bicycles.get(i).getBrand();
            if (brands.containsKey(bname)) {
                // if brand name already in db
                insertSQL.append(brands.get(bname) + ", ");
            } else {
                // else update the database
                DB.update("INSERT INTO brands(brand_name) VALUES('" + bname + "');");
                Integer newBid = Integer.valueOf( ((JSONObject) (DB.listAccess("SELECT brand_id FROM brands " +
                        "WHERE brand_name = '" + bname + "';").get(0))).get("brand_id").toString());
                // update the local brands
                brands.put(bname, newBid);
                insertSQL.append(newBid + ", ");
            }

            String cname = bicycles.get(i).getCategory();
            if (categories.containsKey(cname)) {
                // if category name already in db
                insertSQL.append(categories.get(cname) + ", ");
            } else {
                // else update the database
                DB.update("INSERT INTO categories(category_name) VALUES('" + cname + "');");
                Integer newCid = Integer.valueOf( ((JSONObject) (DB.listAccess("SELECT category_id FROM categories " +
                        "WHERE category_name = '" + cname + "';").get(0))).get("category_id").toString());
                // update the local brands
                categories.put(cname, newCid);
                insertSQL.append(newCid + ", ");
            }

            insertSQL.append(bicycles.get(i).getYear() + ", " +
                    bicycles.get(i).getPrice() + ")");

            if (i != bicycles.size()-1) {
                insertSQL.append(", ");
            } else {
                insertSQL.append(";");
            }
        }
        DB.update(insertSQL.toString());

        int pId = Integer.parseInt( ((JSONObject) (DB.listAccess("SELECT product_id FROM products " +
                "WHERE product_name = '" + bicycles.get(0).getName() + "';").get(0))).get("product_id").toString());

        StringBuilder insertRatingSQL = new StringBuilder(4500000);
        insertRatingSQL.append("INSERT INTO ratings(bike_id, rating, numVotes) VALUES");
        for (int i = 0; i < bicycles.size(); ++i) {

            insertRatingSQL.append("(" + pId + ", " + bicycles.get(i).getRating() + ", " + bicycles.get(i).getVote() + ")");
            ++pId;
            if (i != bicycles.size()-1) {
                insertRatingSQL.append(", ");
            } else {
                insertRatingSQL.append(";");
            }
        }
        DB.update(insertRatingSQL.toString());
    }

    public static void main(String[] args) {
        DomBicycleParser dbp = new DomBicycleParser("Bicycles.xml");
        dbp.run();
    }
}
