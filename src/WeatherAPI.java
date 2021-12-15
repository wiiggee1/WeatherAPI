import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import javax.xml.parsers.*; // XML packages and parsers modifying and data
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.io.File;
import java.io.IOException;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// Step 1: Parse the XML data from the website

// Step 2: Store and modify desired data in an array or hashmap

// Step 3: Create a GUI for showcasing data (under progress)

interface WeatherApp {

    Document load_xml() throws IOException, ParserConfigurationException, SAXException;

    HashMap<String, String> get_node_data() throws IOException, ParserConfigurationException, SAXException;

}

public class WeatherAPI implements WeatherApp {

    String url_adress;
    String file_path;
    String file_name;
    File file;

    ArrayList<String> info_list = new ArrayList<String>(); // Array for storing and modifying API data
    HashMap <String, String> time_data = new HashMap<String, String>();
    StringBuilder string_data = new StringBuilder();

    public WeatherAPI(){
        url_adress = "https://www.yr.no/place/Sweden/Norrbotten/Luleå/forecast.xml";
        file_path = "C:\\Users\\vikto\\IdeaProjects\\Portfolio\\src\\forecast.xml";
        file_name = "forecast.xml";
        file = new File(file_path);

    }
    //Under progress for solving formating issues!
    public static String toString(Document d) throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        return ""+""+"";
    }

    @Override
    public Document load_xml() throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            return factory.newDocumentBuilder().parse(new URL(url_adress).openStream());
        }catch (FileNotFoundException e){
            return factory.newDocumentBuilder().parse(file_path);
        }
    }

    @Override
    public HashMap<String, String> get_node_data() throws IOException, ParserConfigurationException, SAXException {
        //Document gives access to root data of the document tree & loading file from class init and crete new parse object
        Document xml_doc = load_xml();
        xml_doc.getDocumentElement().normalize();

        NodeList xml_list_node = xml_doc.getElementsByTagName("time"); //list node
        NodeList xml_meta = xml_doc.getElementsByTagName("meta");
        Node meta = xml_meta.item(0);
        Element last_update = (Element) meta;
        System.out.println("\n"+"Last update and next update: "+last_update.getTextContent());

        for (int i=0; i< xml_list_node.getLength(); i++){
            Node time_list = xml_list_node.item(i);
            if (time_list.getNodeType()==Node.ELEMENT_NODE){
                Element time_element = (Element) time_list;

                System.out.println("--------------------------------");
                System.out.println(time_element.getTagName()+": "+time_element.getAttribute("from"));

                NodeList time_detail = time_list.getChildNodes();
                info_list.clear();
                for (int j=0; j<time_detail.getLength(); j++){
                    Node item_detail = time_detail.item(j);
                    if (item_detail.getNodeType()==Node.ELEMENT_NODE){ //hasAttribute(String name)
                        Element item_element = (Element) item_detail;

                        info_list.add(item_element.getTagName()+": " +item_element.getAttribute("value") +" "+item_element.getAttribute("unit")
                                +item_element.getAttribute("mps")+" "+item_element.getAttribute("name")+"\n");

                        System.out.println(item_element.getTagName()+": " +item_element.getAttribute("value") +" "+item_element.getAttribute("unit")
                                +item_element.getAttribute("mps")+" "+item_element.getAttribute("name")+";");
                        }
                    }
                //hashmap for key-value pair:
                time_data.put(time_element.getAttribute("from"), String.valueOf(info_list));
                }
            }
        return time_data;
        }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        WeatherAPI api = new WeatherAPI();
        String xml_file_name = args[0]; //optional way to load data

        api.load_xml();
        api.get_node_data();

        System.out.println("\n"+"Stored forecast dates (keys): "+"\n"+api.time_data.keySet());
        //System.out.println(Arrays.toString(api.time_data.get("2021-12-14T07:00:00").split(",")));
        //System.out.println(api.time_data.values());
    }
}
