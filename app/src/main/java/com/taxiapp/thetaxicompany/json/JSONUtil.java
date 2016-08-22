package com.taxiapp.thetaxicompany.json;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpDelete;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

/**
 * A class that provides functionality for posting, retrieving, updating and removing of data.
 * Created by merve on 21.03.2016.
 */
public class JSONUtil extends AsyncTask<String, Void, JSONObject>{

    public Action action;
    private String idUrl;

    public JSONUtil(){}

    private JSONObject getJsonObject(String... params) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ID", generateID());
        if(params.length % 2 != 0) throw  new Exception("invalid key value pairs");
        try {
            for(int i = 0; i < params.length; i = i+2){
                System.out.println(params[i]);
                jsonObject.put(params[i], params[i+1]);
            }
            System.out.println("Object string representation : "+jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Not generic, works only for accounts.
     * Needs to be changed later.
     * @return
     */
    private int generateID(){
        try {
            JSONObject jO = null;
            int id = 0;
            if(idUrl != null){
                jO = getJson(idUrl);
                id = jO.getInt("ID")+1;
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Inserts new data into a model.
     * @param
     */
    public void post(String... params){
        JSONObject jsonObject = null;
        try {
            String[] keyValue = Arrays.copyOfRange(params, 3, params.length);
            jsonObject = getJsonObject(keyValue);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(params[0]);

            HttpResponse response;

            StringEntity entity = new StringEntity(jsonObject.toString());
            entity.setContentType("application/json");
            post.setEntity(entity);
            response = client.execute(post);



            client.getConnectionManager().shutdown();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCarWithID(int id){
        HttpClient client = new DefaultHttpClient();
        String uri = "http://cabtogo.eu-gb.mybluemix.net/api/taxis/" + id;
        HttpDelete delete = new HttpDelete(uri);
        try {
            client.execute(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets an array of json objects in a model.
     * @param url
     * @return
     */
    public JSONObject getJsonArray(String url, String model){
        JSONArray arr;
        URL u;
        try {
            u = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(u.openStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line + " ");
            }
            u.openStream().close();
            arr = new JSONArray(sb.toString());
            JSONObject obj = new JSONObject();
            obj.put(model, arr);
            return obj;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a specific json object.
     * HINT: if the result is an array, it will always return the first element.
     * @param url
     * @return
     */
    public JSONObject getJson(String url){
        JSONObject obj;
        URL u;
        try {
            u = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(u.openStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line + " ");
            }
            u.openStream().close();
            JSONArray arr = new JSONArray(sb.toString());
            obj = new JSONObject();
            //obj.put(model, arr);
            obj = arr.getJSONObject(0);

            return obj;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets a json object by providing the id.
     * @param url
     * @param id
     * @return
     */
    public  JSONObject getJsonById(String url, int id){
        String idUrl = url.concat("/").concat(""+id);
        return getJson(idUrl);
    }

    public  void removeJson(String url, String id) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpDelete delete = new HttpDelete(String.format(url+"/"+id));
            HttpResponse response;
            System.out.println(String.format(url+"/"+id));
            response = client.execute(delete);


            client.getConnectionManager().shutdown();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void updateJson(String url, String json){
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPut post = new HttpPut(url);
            HttpResponse response;

            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");
            post.setEntity(entity);

            response = client.execute(post);

           client.getConnectionManager().shutdown();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Filters for a specific object by appending the filter condition to the URL.
     * @param url url of the model (use ModelUrl here)
     * @param key column
     * @param value
     * @return
     */
    public JSONObject filter(String url, String key, String value){
        String filterUrl = url.concat("?filter[where][").concat(key).concat("]").concat("="+value);
        return getJson(filterUrl);
    }


    /**
     * Filtering with several key value pairs (-> several conditions).
     * @param url
     * @param params
     * @return
     */
    public JSONObject filter(String url, String... params){
        if(params.length % 2 != 0) throw new IllegalArgumentException("key or value missing");
        if(params.length == 2) return filter(url, params[0], params[1]);//switch method for a single condition
        String filterUrl = url+"?filter";

        for(int i = 0; i < (params.length / 2); i+=2){
            if(i>0) filterUrl.concat("[where][and]["+i+"]["+params[i+1]+"]="+params[i+2]);
            else filterUrl.concat("[where][and]["+i+"]["+params[i]+"]="+params[i+1]);
        }
        return getJson(filterUrl);
    }

    /**
     * Does specific operations based on action.
     * HINT : the first parameter should be the url, the second action, when filtering
     * the third parameter should be column and the fourth should be the value.
     * When only retrieving or removing an object by id then the third parameter should be the id.
     * @param params
     * @return
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        String url = params[0];
        String action = params[1];
       String toFilter = params[2];
//        String value = params[3];

        if (action.equals(Action.Filter.name())) return filter(url, toFilter, params[3]);
        else if(action.equals(Action.GetById.name())) return  getJsonById(url, Integer.parseInt(params[2]));
        else if(action.equals(Action.Remove.name())) removeJson(url, params[2]);
        else if(action.equals(Action.Remove.name())) removeJson(url, params[1]);
        else if(action.equals(Action.Remove.name())) removeJson(url, toFilter);
        else if(action.equals(Action.Get.name())) return getJson(url);
        else if(action.equals(Action.Register.name())){
            idUrl = params[2];
            post(params);
        }
        else if(action.equals(Action.GetArray.name())) return getJsonArray(url, params[2]);
        else if(action.equals(Action.Update.name())) updateJson(params[0], params[2]);

        return null;
    }

    public enum Action {
        Filter,
        Register,
        GetById,
        Remove,
        Update,
        Get,
        GetArray;

    }

}


