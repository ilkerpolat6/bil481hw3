package com.mycompany.app;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.HashMap;
import java.util.Map;

import com.mycompany.app.Person;
import com.mycompany.app.MyHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App {
    public static int stringDifference(String cs1, String cs2) {
        if (cs1.equals(cs2)) {
            return 0;
        }
        if (cs1.isEmpty() || cs2.isEmpty()) {
            return 999;
        }
        int i, diff = 0;
        if (Math.abs(cs1.length() - cs2.length()) > 3)
            return 999;
        diff = Math.abs(cs1.length() - cs2.length());
        for (i = 0; i < cs1.length() && i < cs2.length(); i++) {
            if (cs1.charAt(i) != cs2.charAt(i)) {
                diff++;
            }
        }
        return diff;
    }

    public static String search(String fName, String lName) {
        String results = "";
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            MyHandler handler = new MyHandler();
            saxParser.parse(new File("./EEAS.xml"), handler);
            List<Person> empList = handler.getEmpList();
            // print employee information
            /*
             * for (Person emp : empList){ results+=emp.toString(); }
             */

            if (lName.length() == 0) {
                for (Person emp : empList) {
                    // System.out.println(emp.getFirstname());
                    System.out.println(stringDifference(emp.getFirstname(), fName));
                    if (stringDifference(emp.getFirstname(), fName) < 3) {
                        results += emp.toString();
                    }
                }
            } else if (fName.length() == 0) {
                for (Person emp : empList) {
                    if (stringDifference(emp.getLastName(), lName) < 3) {
                        results += emp.toString();
                    }
                }
            } else if (fName.length() != 0 && lName.length() != 0) {
                for (Person emp : empList) {
                    if (stringDifference(emp.getLastName(), lName) < 3
                            && stringDifference(emp.getFirstname(), fName) < 3) {
                        results += emp.toString();
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(results);
        return results;
    }

    public static void main(String[] args) {
        port(getHerokuAssignedPort());

        get("/", (req, res) -> "Hello, World");

        post("/compute", (req, res) -> {
            // System.out.println(req.queryParams("input1"));
            // System.out.println(req.queryParams("input2"));

            String input1 = req.queryParams("First Name").replaceAll(" ", "");

            String input2 = req.queryParams("Last Name").replaceAll(" ", "");

            String result = App.search(input1, input2);
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
