package com.taxiapp.thetaxicompany.json;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by Sjoerd Brauer on 7-5-2016.
 */
public class JSONArrayUtil extends AsyncTask<String, Void, JSONArray> {
    public Action action;
    private String idUrl;


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
    public  JSONObject getJsonById(String url, int id){
        String idUrl = url.concat("/").concat(""+id);
        return getJson(idUrl);
    }

    public  void removeJson(String url, int id){
        //TODO
    }

    /**
     * Gets an array of json objects in a model.
     * @param url
     * @return
     */
    public JSONArray getJsonArray(String url, String model){
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
            return arr;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  void updateJson(String url, String json){
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
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
     * Does specific operations based on action.
     * HINT : the first parameter should be the url, the second action, when filtering
     * the third parameter should be column and the fourth should be the value.
     * When only retrieving or removing an object by id then the third parameter should be the id.
     * @param params
     * @return
     */
    @Override
    protected JSONArray doInBackground(String... params) {
        String url = params[0];
        String action = params[1];
        String toFilter = params[2];
//        String value = params[3];

//        if (action.equals(Action.Filter.name())) return filter(url, toFilter, params[3]);
//        else if(action.equals(Action.GetById.name())) return  getJsonById(url, Integer.parseInt(toFilter));
//        else if(action.equals(Action.Remove.name())) removeJson(url, Integer.parseInt(toFilter));
//        else if(action.equals(Action.Get.name())) return getJson(url);
//        else if(action.equals(Action.Register.name())){
//            idUrl = params[2];
//            post(params);
//        }
        if(action.equals(Action.GetArray.name())) return getJsonArray(url, params[2]);
        else if(action.equals(Action.Update.name())) updateJson(params[0], params[2]);

        return null;
    }

    public JSONObject filter(String url, String key, String value){
        String filterUrl = url.concat("?filter[where][").concat(key).concat("]").concat("="+value);
        return getJson(filterUrl);
    }
}
