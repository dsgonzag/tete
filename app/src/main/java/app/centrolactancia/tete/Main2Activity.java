package app.centrolactancia.tete;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import  java.sql.Connection;
import  java.sql.DriverManager;
import  java.sql.PreparedStatement;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tete.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_LONG;

public class Main2Activity extends AppCompatActivity {
    public final static String hostSaveUser = "https://tetelactanciamaterna.herokuapp.com/SaveUser";
    public final static String hostUsers = "https://tetelactanciamaterna.herokuapp.com/listUsers";
    private boolean visible=false;

    private EditText edtNombreR,edtUsuarioR,edtCorreoR,edtContraseñaR;

    private ProgressBar proRegis;
    private UnToast toast;
    Button btnRegR, btnS2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        proRegis= (ProgressBar) findViewById(R.id.pro2);
        edtNombreR= (EditText) findViewById(R.id.nombreRegistro);
        edtContraseñaR= (EditText) findViewById(R.id.contraseñaRegistro);
        edtUsuarioR= (EditText) findViewById(R.id.usuarioRegistro);
        edtCorreoR= (EditText) findViewById(R.id.correoElectronicoRegistro);
        btnRegR= (Button) findViewById(R.id.btnRegistro2);


        /*btnRegR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });*/

    }


    public void registrar(View v) {
        boolean camposLlenos = fullFields();
        boolean internetEnabled = internetActive();

        //  Intent intent= new Intent(this, MainActivity.class);
        if (edtUsuarioR.getText().toString().isEmpty() && edtContraseñaR.getText().toString().isEmpty() && edtNombreR.getText().toString().isEmpty() && edtCorreoR.getText().toString().isEmpty()){
            toast.show(this, "Faltan datos por completar", Toast.LENGTH_LONG);
    } else if(camposLlenos){//comprobar que ningun campo este vacio
            if(internetEnabled){//comprobar que este conectado a internet
                new TaskRegistrer().execute();

            }
            else{
               toast.show(this, "¡Error, sin conexión!", Toast.LENGTH_LONG);
            }

        }

    }



    private boolean fullFields() {

        String name = edtNombreR.getText().toString().trim();
        String email = edtCorreoR.getText().toString().trim();
        String password = edtContraseñaR.getText().toString().trim();
        String user = edtUsuarioR.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(Main2Activity.this,"¡Upps no ingresaste tu nombre!", LENGTH_LONG).show();
            return false;
        }

        if(TextUtils.isEmpty(user)){
            Toast.makeText(Main2Activity.this,"¡Upps no ingresaste tu nombre de usuario!", LENGTH_LONG).show();
            return false;
        }

        if(TextUtils.isEmpty(email)){
            Toast.makeText(Main2Activity.this,"¡Upps no ingresaste tu correo!", LENGTH_LONG).show();
            return false;
        }


        if(TextUtils.isEmpty(password)){
            Toast.makeText(Main2Activity.this,"¡Upps no ingresaste contraseña!", LENGTH_LONG).show();
            return false;
        }

       /* if(TextUtils.isEmpty(user)){
            Toast.makeText(Main2Activity.this,"¡Upps no ingresaste tu usuario!", LENGTH_LONG).show();
            return false;
        }*/




        return true;
    }


    private boolean internetActive() {
        ConnectivityManager active = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (active != null)
        {
            NetworkInfo[] res = active.getAllNetworkInfo();
            if (res != null)
                for (int i = 0; i < res.length; i++)
                    if (res[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    class TaskRegistrer extends AsyncTask<String,Void,String> {


        @Override
        protected void onPreExecute() {
            proRegis.setVisibility(View.VISIBLE);
            btnRegR.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            //simular tiempo
            @SuppressLint("WrongThread") String useroh =  edtUsuarioR.getText().toString();
            try {
                return getData(useroh);//comprobar que usuario no exista devuelve true si no existe

                // Thread.sleep(5000);

            }catch (Exception e){
                e.printStackTrace();
            }

            //return strings[0];
            return null;
        }



        @Override
        protected void onPostExecute(String s) {
            Log.d("Trae String",s);
            if(s.equals("")){
                new HTTPAsyncTask().execute();
                Toast.makeText(Main2Activity.this,"¡Inicia Sesión!", LENGTH_LONG).show();
                proRegis.setVisibility(View.INVISIBLE);
                btnRegR.setEnabled(true);
                Intent intent= new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(Main2Activity.this,"¡Upps Usuario ya existe!", LENGTH_LONG).show();
                Intent intent= new Intent(Main2Activity.this, Main2Activity.class);
                startActivity(intent);
            }

        }





        public String getData(String useri){
            String sql = "https://tetelactanciamaterna.herokuapp.com/listUsers";
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

                while((inputLine = in.readLine()) != null){
                    response.append(inputLine);
                }

                json = response.toString();

                JSONArray jsonArr = null;

                jsonArr = new JSONArray(json);

                int cont=0;
                for(int i = 0;i<jsonArr.length();i++){
                    cont++;
                    JSONObject jsonObject = jsonArr.getJSONObject(i);

                    Log.d("Usuario",jsonObject.optString("nom_user"));
                    mensaje = jsonObject.optString("nom_user");
                    if(useri.equals(mensaje)){
                        Log.d("Usuario prueba 1",mensaje);

                        return mensaje;
                    }

                    if(cont==jsonArr.length()){
                        mensaje = "";
                        return mensaje;
                    }
                }

                //sal.setText(mensaje);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mensaje = "";
            return mensaje;
        }
        //enviar datos para guardar usuario
        private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... urls) {
                // params comes from the execute() call: params[0] is the url.
                try {
                    try {
                        return HttpPost(hostSaveUser);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return "Error!";
                    }
                } catch (IOException e) {
                    return "Se ha detectado un error.";
                }
            }
            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {
                Toast.makeText(Main2Activity.this,"¡Hecho!", LENGTH_LONG).show();
            }
        }
        private String HttpPost(String myUrl) throws IOException, JSONException {
            String result = "";

            URL url = new URL(myUrl);

            // 1. create HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // 2. build JSON object
            JSONObject jsonObject = buidJsonObject();

            // 3. add JSON content to POST request body
            setPostRequestContent(conn, jsonObject);

            // 4. make POST request to the given URL
            conn.connect();

            // 5. return response message
            return conn.getResponseMessage()+"";

        }

        private JSONObject buidJsonObject() throws JSONException {

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("nombre", edtNombreR.getText().toString());
            jsonObject.accumulate("mail",  edtCorreoR.getText().toString());
            jsonObject.accumulate("nom_user",  edtUsuarioR.getText().toString());
            jsonObject.accumulate("pass",edtContraseñaR.getText().toString());

            return jsonObject;
        }

        private void setPostRequestContent(HttpURLConnection conn,
                                           JSONObject jsonObject) throws IOException {

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonObject.toString());
            Log.i(MainActivity.class.toString(), jsonObject.toString());
            writer.flush();
            writer.close();
            os.close();
        }

    }

  /*  public void validate(){
        edtNombreR = (EditText)findViewById(R.id.nombreRegistro);
        edtUsuarioR = (EditText)findViewById(R.id.usuarioRegistro);
        edtCorreoR = (EditText)findViewById(R.id.correoElectronicoRegistro);
        edtContraseñaR = (EditText)findViewById(R.id.contraseñaRegistro);


        nombre=edtNombreR.getText().toString();
        usuario=edtUsuarioR.getText().toString();
        correo=edtCorreoR.getText().toString();
        contraseña=edtContraseñaR.getText().toString();

        String  error= "";

        if (nombre.isEmpty()||nombre.length()>50){
         //   edtNombreR.setError("Por Favor Ingrese Nombre Valido");
            error="Por Favor Ingrese Nombre Valido";
        }else if (usuario.isEmpty()||usuario.length()>50){
         //   edtUsuarioR.setError("Por Favor Ingrese Usuario Valido");
            error="Por Favor Ingrese Usuario Valido";
        }else if (correo.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
         //   edtCorreoR.setError("Por Favor Ingrese el Correo Valido");
            error="Por Favor Ingrese el Correo Valido";
        }else if (contraseña.isEmpty()){
         //   edtContraseñaR.setError("Por Favor Ingrese Contraseña");
            error="Por Favor Ingrese Contraseña";
        }
        if (error==""){
            RegUser();
        }else {
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
        }

    }


    public Connection conexionBD(){
        Connection conexion=null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:jtds:sqlserver://52.232.165.138;databaseName=Tete;user=Tete;password=tete;");

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return conexion;
    }

    public void RegUser() {
        edtNombreR = (EditText)findViewById(R.id.nombreRegistro);
        edtUsuarioR = (EditText)findViewById(R.id.usuarioRegistro);
        edtCorreoR = (EditText)findViewById(R.id.correoElectronicoRegistro);
        edtContraseñaR = (EditText)findViewById(R.id.contraseñaRegistro);


        nombre=edtNombreR.toString();
        usuario=edtUsuarioR.toString();
        correo=edtCorreoR.toString();
        contraseña=edtContraseñaR.toString();

        try {

            PreparedStatement pst = conexionBD().prepareStatement("insert into Tbl_Users Values(?,?,?,?)");

            pst.setString(1,edtUsuarioR.getText().toString());
            pst.setString(2,edtContraseñaR.getText().toString());
            pst.setString(3,edtCorreoR.getText().toString());
            pst.setString(4,edtNombreR.getText().toString());
            pst.executeUpdate();
            Toast.makeText(getApplicationContext(), "GUARDADO", Toast.LENGTH_SHORT).show();
            Intent ListSong = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(ListSong);

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }*/

}

