package edu.escuelaing.arem;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

/**
 * Clase encargada de ejecutar el programa.
 * @author Cristian Camilo Piñeros Arevalo
 * @version 1.0.  (24 de Agosto del 2021)
 */
public class App {

    /**
     * Metodo inicializador de nuestro proyecto, que despliega por medio de Spark los distintos servisios
     * @param args argumentos de entrada
     */
    public static void main(String[] args){
        staticFiles.location("/public");
        port(getPort());;
        get("/facadealpha", "application/json", (req, res) -> facadeAlpha(req,res));
        get("/facadeiex", "application/json", (req, res) -> facadeIex(req,res));
    }

    /**
     * Metodo que analiza si ya se esta usando un puerto, para que en caso de que no designar uno por defecto
     * @return Numero del ppuerto que se usara
     */
    static int getPort(){
        if (System.getenv("PORT")!=null){
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    /**
     * Metodo encargado de dirigir la secuencia para obtener la información de Alpha Ventage API
     * @param req Requisitos encomendados por la URL
     * @param res Responsabilidades impuestas por la URL
     * @return cadena que contiene el JSON de la API consultada
     */
    public static String facadeAlpha(Request req, Response res){
        String stock = req.queryParams("st");
        String response = "None";
        HttpStockService stockService = CurrentServiceInstance.getInstance().getServiceAlpha();
        if(stock!=null && stock!=""){
            stockService.setStock(stock);
        }
        try {
            response= stockService.TimeSeriesDaily();
        }catch (IOException ex){
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    /**
     * Metodo encargado de dirigir la secuencia para obtener la información de IEX Cloud API
     * @param req Requisitos encomendados por la URL
     * @param res Responsabilidades impuestas por la URL
     * @return cadena que contiene el JSON de la API consultada
     */
    public static String facadeIex(Request req, Response res){
        String stock = req.queryParams("st");
        String response = "None";
        HttpStockService stockService = CurrentServiceInstance.getInstance().getServiceIEX();
        if(stock!=null && stock!=""){
            stockService.setStock(stock);
        }
        try {
            response= stockService.TimeSeriesDaily();
        }catch (IOException ex){
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
}