package app.centrolactancia.tete;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tete.R;

import app.ejemplo.holaa.InformacionDonante;


public class Main3Activity extends AppCompatActivity {
    TextView showUser, showMail;
    View view;
    String Id;
    Button btnEdit;
    Button btnS3, btnS4, btnMap;
    private Context activity;
    private TextView usuario1;
    private static final String TAG = "Main3Activity";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        /*Id = getFromSharedPreferences("id");
        Log.d("Soy el usuario", Id);
        showUser = (TextView) view.findViewById(R.id.User);
        showMail = (TextView) view.findViewById(R.id.Mail);*/
        TextView  usuario2 =findViewById(R.id.User);
        Bundle bundle = getIntent().getExtras();
        String usuario = getIntent().getStringExtra("usuario");
        usuario2.setText(usuario);



        btnMap = (Button) findViewById(R.id.btnDonar);
        btnMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"BIENVENIDO", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), InformacionDonante.class);
                startActivityForResult(intent, 0);
            }
        });

        btnS4 = (Button) findViewById(R.id.btnSolicitar);
        btnS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Main4Activity.class);
                startActivityForResult(intent, 0);
            }
        });

      //  fillProfile(Id);

    }
  /*  private void fillProfile(String Id) {
        getData(Id);
    }

    private String getFromSharedPreferences(String nom_user) {
        SharedPreferences sharedPre = getActivity().getSharedPreferences("login_preference", Context.MODE_PRIVATE);
        return sharedPre.getString(nom_user, "");
    }


    public void getData(String id) {
        String sql = "https://tetelactanciamaterna.herokuapp.com/listUser/"+id;
        String mensaje = "";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(sql);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            String json = "";

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            json = response.toString();

            JSONArray jsonArr = null;

            jsonArr = new JSONArray(json);

            int cont = 0;
            for (int i = 0; i < jsonArr.length(); i++) {
                cont++;
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                String uUser, mMail;
                Log.d("El id es", jsonObject.optString("id"));
                uUser = jsonObject.optString("nom_user");
                mMail = jsonObject.optString("mail");


                showUser.setText("@" + uUser);
                showMail.setText(mMail);


            }

            //sal.setText(mensaje);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public Context getActivity() {
        return activity;
    }*/
}
