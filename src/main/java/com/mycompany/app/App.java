package com.mycompany.app;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App {
    public static ArrayList<String> search(String name,String surname) {
        System.out.println("inside search");
        java.util.Scanner xmlreader = null;
        try {
            xmlreader = new java.util.Scanner("EEAS.xml");
        }catch (Exception e) {
            System.out.println("File Cannot Read!");
        }
        while(xmlreader.hasNextLine()) {
            System.out.println(xmlreader.nextLine());
        }
        return null;
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
            System.out.println(input1);

            ArrayList<String> result = App.search(input1,input2);

            Map map = new HashMap();
            map.put("result", result);
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
