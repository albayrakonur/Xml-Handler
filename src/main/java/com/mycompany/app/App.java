package com.mycompany.app;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App {
    public static ArrayList<String> search(String name,String surname) {
        System.out.println("inside search");
        ArrayList<String> result = new ArrayList<>();
        try {
            File inputFile = new File("EEAS.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("NAME");
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
               
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if(eElement.getElementsByTagName("FIRSTNAME").item(0).getTextContent().equals(name)
                        || eElement.getElementsByTagName("LASTNAME").item(0).getTextContent().equals(surname)) {
                            result.add(eElement.getAttribute("Id"));
                            result.add(eElement.getElementsByTagName("FIRSTNAME").item(0).getTextContent());
                            result.add(eElement.getElementsByTagName("LASTNAME").item(0).getTextContent());
                            System.out.println("Entity ID: " 
                            + eElement.getAttribute("Id"));
                        System.out.println("First Name : " 
                            + eElement
                            .getElementsByTagName("FIRSTNAME")
                            .item(0)
                            .getTextContent());
                        System.out.println("Last Name : " 
                            + eElement
                            .getElementsByTagName("LASTNAME")
                            .item(0)
                            .getTextContent());
                    }
                }
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
         return result;
    }

    public static void main(String[] args) {
        port(getHerokuAssignedPort());

        get("/", (req, res) -> "Hello, World");

        post("/compute", (req, res) -> {
            // System.out.println(req.queryParams("input1"));
            // System.out.println(req.queryParams("input2"));


            String input1 = req.queryParams("input1").replaceAll("\\s", "");
            System.out.println(input1);

            String input2 = req.queryParams("input2").replaceAll("\\s", "");
            System.out.println(input2);

            ArrayList<String> result = App.search(input1,input2);

            Map map = new HashMap();
            map.put("result", result.toString());
            return new ModelAndView(map, "compute.mustache");
        }, new MustacheTemplateEngine());

        get("/compute", (rq, rs) -> {
            Map map = new HashMap();
            map.put("result", "not computed yet!");
            return new ModelAndView(map, "compute.mustache");
        }, new MustacheTemplateEngine());
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
    }
}
