package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

public class UserClient {
    public String userImg = "img/user/";
    public String userDefaultImage = "img/user/default.jpg";


    public String api = "https://blyatblyatqisen.nasihosting.com/QiSen/api.php";
    public String img_api = "https://blyatblyatqisen.nasihosting.com/QiSen/";
//    public String api = "http://192.168.137.1/QiSen/api.php";
//    public String img_api = "http://192.168.137.1/QiSen/";

    public UserClient()
    {

    }

    public String get(String destination) throws  Exception
    {
        String res = "";
        URL url = new URL(destination);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setDoInput(true);
        con.setDoOutput(true);
        con.setConnectTimeout(270000);
        con.setReadTimeout(270000);


        int statusCode = con.getResponseCode();

        if( statusCode == HTTP_OK)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while((line = br.readLine()) != null)
            {
                res += line;
            }
            br.close();
        }else
        {
            res = "";
        }

        return res;
    }

    public String post( String destination, HashMap<String, String> param) throws Exception
    {
        String res = "";

        URL url = new URL(destination);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);

        con.setReadTimeout(270000);
        con.setConnectTimeout(270000);

        OutputStream os = con.getOutputStream();

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

        bw.write(getParam(param));
        bw.flush();
        bw.close();

        os.close();

        int statusCode = con.getResponseCode();
        if( statusCode == HTTP_OK)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while((line = br.readLine()) != null)
            {
                res += line;
            }
        }else
        {
            res = "";
        }

        return res;
    }

    public String getParam(HashMap<String, String> param)
    {
        StringBuilder sb = new StringBuilder();

        boolean status = true;
        try{
            for(Map.Entry<String, String> map : param.entrySet())
            {
                if( status )
                    status = false;
                else
                    sb.append("&");
                sb.append(URLEncoder.encode(map.getKey(), "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(map.getValue(), "UTF-8"));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
