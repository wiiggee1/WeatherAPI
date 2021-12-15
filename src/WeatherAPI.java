import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import javax.xml.parsers.*; // XML packages and parsers modifying and data
import java.io.File;
import java.io.IOException;


import org.w3c.dom.*;
import org.xml.sax.SAXException;

// Step 1: Parse the XML data from the website

// Step 2: Store and modify desired data in an array or hashmap

// Step 3: Create a GUI for showcasing data (under progress)

interface WeatherApp {

    void load_xml() throws IOException, ParserConfigurationException, SAXException;

    HashMap<String, String> get_time_data() throws IOException, ParserConfigurationException, SAXException;

}

public class WeatherAPI implements WeatherApp {

    String url_adress;
    String file_path;
    String file_name;
    File file;
    Document xml_doc;

    ArrayList<String> info_list = new ArrayList<>(); // Array for storing and modifying API data
    HashMap <String, String> time_data = new HashMap<>();
    HashMap <String, String> element_data = new HashMap<>();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


    public WeatherAPI() throws ParserConfigurationException {
        url_adress = "https://www.yr.no/place/Sweden/Norrbotten/Luleå/forecast.xml";
        file_path = "C:\\Users\\vikto\\IdeaProjects\\Portfolio\\src\\forecast.xml";
        file_name = "forecast.xml";
        file = new File(file_path);
    }
    //Under progress for solving formating issues!
    public String toString() {
        return "";
    }

    //setter
    @Override
    public void load_xml() throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilder builder = factory.newDocumentBuilder();
        try{

            this.xml_doc = builder.parse(new URL(url_adress).openStream());
            this.xml_doc.normalize();
        }catch (FileNotFoundException e){
            System.out.println("URL wasn't found!!!");

        }try{

            System.out.println("XML was found locally!");
            this.xml_doc = builder.parse(file_path);
            this.xml_doc.normalize();

        } catch (Exception e) {
            System.out.println("Something went wrong!!!");
        }
    }

    //getter that return the current XML document object
    public Document get_xml_doc() {
        return this.xml_doc;
    }

    public void show_last_update(){
        Document xml_update = get_xml_doc();
        NodeList xml_meta = xml_update.getElementsByTagName("meta");
        Node meta = xml_meta.item(0);
        Element last_update = (Element) meta;
        System.out.println("\n"+"Last update and next update: "+last_update.getTextContent());
    }

    @Override
    public HashMap<String, String> get_time_data(){

        Document xml_data = get_xml_doc(); //Document for access to root data of XML tree

        NodeList xml_list = xml_data.getElementsByTagName("time"); //list node for time data

        for (int i=0; i<xml_list.getLength(); i++){
            Node time_list = xml_list.item(i);
            if (time_list.getNodeType()==Node.ELEMENT_NODE){
                Element time_element = (Element) time_list;
                System.out.println("--------------------------------");
                System.out.println(time_element.getTagName()+": "+time_element.getAttribute("from"));


                NodeList time_detail = time_list.getChildNodes();
                info_list.clear();
                //looping through the attributes for the different tags and corresponding value
                for (int j=0; j<time_detail.getLength(); j++){
                    Node item_detail = time_detail.item(j);
                    if (item_detail.getNodeType()==Node.ELEMENT_NODE){ //hasAttribute(String name)
                        Element item_element = (Element) item_detail;

                        element_data.put(item_element.getTagName(), item_element.getAttribute("value") +" "+item_element.getAttribute("unit")
                                +item_element.getAttribute("mps")+" "+item_element.getAttribute("name"));

                        System.out.println(item_element.getTagName()+": "+item_element.getAttribute("value") +" "+item_element.getAttribute("unit")
                                +item_element.getAttribute("mps")+" "+item_element.getAttribute("name"));
                        }
                    }
                //hashmap for key-value pair:
                time_data.put(time_element.getAttribute("from"), String.valueOf(element_data));
                }
            }
        return time_data;
        }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        WeatherAPI api = new WeatherAPI();
        String xml_file_name = args[0]; //optional way to load data

        api.load_xml();
        api.show_last_update();
        api.get_time_data();

        System.out.println("\n"+"Stored forecast dates (keys): "+"\n"+api.time_data.keySet()+"\n");
        //System.out.println(Arrays.toString(api.time_data.get("2021-12-14T07:00:00").split(",")));
        //System.out.println(api.time_data.values());
        //System.out.println(api.time_data);
    }
}

//1. change format on XML date to something more easy
//2. loop through the keys in the hashmap and format the string (f-string)