package org.apache.airavata.client.registry.tools;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GenAppUtils {
    public static List<String> GetModuleList(){
        List<String> modulesList = new ArrayList<String>();
        JSONParser parser = new JSONParser();
        try {        
            JSONObject menuJ = (JSONObject) parser.parse(new FileReader(
                    "menu.json"));
            JSONArray menu = (JSONArray) menuJ.get("menu");
            Iterator<JSONObject> menuObject = menu.iterator();
            while(menuObject.hasNext()){
                JSONObject mod = menuObject.next();
                JSONArray modules = (JSONArray) mod.get("modules") ;
                Iterator<JSONObject> moduleObject = modules.iterator();
                while(moduleObject.hasNext()){
                    JSONObject modJSON = moduleObject.next();
                    String modName = (String) modJSON.get("id");
                    modulesList.add(modName);
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modulesList;
    }
    
    public static String GetExectuablePath(){
        JSONParser parser = new JSONParser();
        String exec_path = "";
        try {
            JSONObject directiveJ = (JSONObject) parser.parse(new FileReader(
                    "directives.json"));
            JSONObject exec = (JSONObject) directiveJ.get("executable_path");
            exec_path = (String) exec.get("html5");
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return exec_path;
    }
}
