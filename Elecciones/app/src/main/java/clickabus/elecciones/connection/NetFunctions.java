package clickabus.elecciones.connection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NetFunctions {

    private JSONParser jsonParser;

    // PRUEBAS GEOLOCALIZACIÓN.
    private static final String ANDALUCIA_LAT = "37.59019029917172";
    private static final String ANDALUCIA_LONG = "-4.08345225000005";
    private static final String TERUEL_LAT = "40.34501235694547";
    private static final String TERUEL_LONG = "-1.1009648500000822";

    // Métodos para hacer la petición.
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    // Dirección donde está el servidor 000webhost.
    private static final String SERVER_URL_000 = "http://collapseit.site90.net/serverelecciones/index.php";
    private static final String SERVER_POST_URL_000 = "http://collapseit.site90.net/serverelecciones/addscore.php";

    // Dirección donde está el servidor Hostinger.
    private static final String SERVER_URL_HOSTINGER = "http://collapseit.hol.es/serverelecciones/index.php";
    private static final String SERVER_POST_URL_HOSTINGER = "http://collapseit.hol.es/serverelecciones/addscore.php";

    // Dirección donde está el servidor Hostinger.
    private static final String SERVER_URL_BYETHOST = "http://collapseit.byethost7.com/serverelecciones/index.php";
    private static final String SERVER_POST_URL_BYETHOST = "http://collapseit.byethost7.com/serverelecciones/addscore.php";

    // Dirección donde está el servidor Hostinger.
    private static final String SERVER_URL_2FREE = "http://collapseit.honor.es/serverelecciones/index.php";
    private static final String SERVER_POST_URL_2FREE = "http://collapseit.honor.es/serverelecciones/addscore.php";

    private int server_num = 0;
    private int server_post_num = 0;
    private static final String[] SERVERS = {SERVER_URL_HOSTINGER, SERVER_URL_000, SERVER_URL_BYETHOST, SERVER_URL_2FREE};

    private static final String[] SERVERS_POST = {SERVER_POST_URL_HOSTINGER, SERVER_POST_URL_000, SERVER_POST_URL_BYETHOST, SERVER_POST_URL_2FREE};

    // Dirección donde está el servidor.
    private String SERVER_URL = "http://collapseit.hol.es/serverelecciones/index.php";
    private String SERVER_POST_URL = "http://collapseit.hol.es/serverelecciones/addscore.php";
    // Dirección API Google.
    private static final String API_LOCATION = "http://maps.googleapis.com/maps/api/geocode/json";

    // Tags a mandar al servidor.
    private static final String GET_ALL_TAG = "get_all";
    private static final String GET_DATABASE_VERSION = "get_database_version";
    private static final String POST_SCORE = "add_score";

    // constructor
    public NetFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * Método que obtiene un JSON con toda la información de la base de datos.
     * @return JSON.
     */
    public JSONObject getAll(){
        // Parámetros a mandar al servidor.
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", GET_ALL_TAG));
        JSONObject json = jsonParser.makeHttpRequest(SERVER_URL, METHOD_GET, params);
        while (json==null){
            server_num++;
            if(server_num>SERVERS.length-1) return null;
            SERVER_URL = SERVERS[server_num];
            json = jsonParser.makeHttpRequest(SERVER_URL, METHOD_GET, params);
        }
        return json;
    }

    public JSONObject getDatabaseVersion(){
        // Parámetros a mandar al servidor.
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", GET_DATABASE_VERSION));
        JSONObject json = jsonParser.makeHttpRequest(SERVER_URL, METHOD_GET, params);
        while (json==null){
            server_num++;
            if(server_num>SERVERS.length-1) return null;
            SERVER_URL = SERVERS[server_num];
            json = jsonParser.makeHttpRequest(SERVER_URL, METHOD_GET, params);
        }
        return json;
    }

    public String getAdministrativeArea(String lat, String lng){
        String short_name = "";
        List<NameValuePair> params = new ArrayList<>();
        String latlng = lat + "," + lng;
        params.add(new BasicNameValuePair("latlng", latlng));
        params.add(new BasicNameValuePair("sensor", "true"));
        JSONObject json = jsonParser.makeHttpRequest(API_LOCATION, METHOD_GET, params);
        try {
            JSONArray Results = json.getJSONArray("results");
            JSONObject zero = Results.getJSONObject(0);
            JSONArray address_components = zero.getJSONArray("address_components");

            for (int i = 0; i < address_components.length(); i++)
            {
                JSONObject zero2 = address_components.getJSONObject(i);
                JSONArray mtypes = zero2.getJSONArray("types");
                String Type = mtypes.getString(0);

                if (Type.equalsIgnoreCase("administrative_area_level_1"))
                {
                    short_name = zero2.getString("short_name");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return short_name;
    }

    public JSONObject addScore(int party_id, int admin_area_id){
        // Parámetros a mandar al servidor.
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", POST_SCORE));
        params.add(new BasicNameValuePair("party_id", String.valueOf(party_id)));
        params.add(new BasicNameValuePair("admin_area_id", String.valueOf(admin_area_id)));
        JSONObject json = jsonParser.makeHttpRequest(SERVER_POST_URL, METHOD_POST, params);
        while (json==null){
            server_post_num++;
            if(server_post_num>SERVERS_POST.length-1) return null;
            SERVER_POST_URL = SERVERS_POST[server_post_num];
            json = jsonParser.makeHttpRequest(SERVER_POST_URL, METHOD_POST, params);
        }
        return json;
    }

}
