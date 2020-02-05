package app.centrolactancia.tete;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import  java.sql.Connection;
import  java.sql.DriverManager;
import  java.sql.PreparedStatement;
import  java.sql.ResultSet;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tete.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.centrolactancia.tete.Database.clsDatabase;

public class MainActivity extends AppCompatActivity {

    EditText edtNombre, edtPass;
    Button btnLogin, btnReg , btnMap;

    Button btnS1;
    private  EditText user, pass;
    private UnToast toast;
    private ProgressBar pro;
    private Button btnValidar;
    private clsDatabase loDatabase;
    private SQLiteDatabase loExecute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        String sesioniciada = getFromSharedPreferencesNull("usuario");
        if (sesioniciada == null) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            loDatabase= new clsDatabase(this);
            user = ( EditText) findViewById(R.id.usuario);
            pass = ( EditText) findViewById(R.id.contraseña);
            pro = (ProgressBar) findViewById(R.id.pro1);
            btnValidar = (Button) findViewById(R.id.btnInicioSesion);
        } else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);

            btnReg = (Button) findViewById(R.id.btnRegistro);

       /*     btnReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2;
                    intent2 = new Intent(v.getContext(), Main2Activity.class);
                    startActivity(intent2);
                }

        });*/

            btnS1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (v.getContext(), Main2Activity.class);
                    startActivityForResult(intent, 0);

                }
            });
        }

        lLlenarLugares();

    }

    private void lLlenarLugares()
    {
        try{
            loExecute = loDatabase.getWritableDatabase();
            loExecute.delete("tbListaLocales", null, null);
            ContentValues values = new ContentValues();
            values.put("nombre_establecimiento", "Hospital General Guasmo Sur");
            values.put("direccion", "Avenida Cacique Tomalá");
            values.put("latitud", -2.278339);
            values.put("longitud", -79.895838);
            loExecute.insert("tbListaLocales", null, values);

            values = null;
            values = new ContentValues();
            values.put("nombre_establecimiento", "Hospital Universitario");
            values.put("direccion", "Avenida 43 NO, BODEGAS FERCONSA");
            values.put("latitud", -2.096141);
            values.put("longitud", -79.946781);
            loExecute.insert("tbListaLocales", null, values);

            values = null;
            values = new ContentValues();
            values.put("nombre_establecimiento", "Hospital del Niño, Dr Fransisco de Icaza Bustamante");
            values.put("direccion", "Avenida Quito");
            values.put("latitud", -2.2032727);
            values.put("longitud", -79.8953945);
            loExecute.insert("tbListaLocales", null, values);

            values = null;
            values = new ContentValues();
            values.put("nombre_establecimiento", "Hospital del IESS Los Ceibos");
            values.put("direccion", "Avenida del Bombero");
            values.put("latitud", -2.1762186);
            values.put("longitud", -79.9407821);
            loExecute.insert("tbListaLocales", null, values);

            values = null;
            values = new ContentValues();
            values.put("nombre_establecimiento", "Hospital de Niños Dr. Roberto Gilbert E.");
            values.put("direccion", "Avenida Roberto Gilbert y, Nicasio Safadi");
            values.put("latitud", -2.1778518);
            values.put("longitud", -79.8850402);
            loExecute.insert("tbListaLocales", null, values);

            values = null;
            values = new ContentValues();
            values.put("nombre_establecimiento", "Hospital De Niños Leon Becerra");
            values.put("direccion", "Eloy Alfaro y, Bolivia");
            values.put("latitud", -2.1289721);
            values.put("longitud", -79.9645355);
            loExecute.insert("tbListaLocales", null, values);

        }catch (Exception ex)
        {
            Toast.makeText(MainActivity.this,"Error al momento de llenar los lugares en el metodo lLlenarLugares()",Toast.LENGTH_LONG).show();
        }
    }

    public void logear(View v){
        boolean camposLlenos= fullFields();
        boolean internetEnabled = internetActive();
        if(!user.getText().toString().isEmpty() && !pass .getText().toString().isEmpty()){
            if(camposLlenos){
                if(internetEnabled){
                    new Task1().execute();
                }
                else{
                  //  toast.show(this, "¡Error, sin conexión!", Toast.LENGTH_LONG);
                }
            }
        }else{
           // toast.show(this, "Complete sus datos", Toast.LENGTH_LONG);
        }
    }
    private String getFromSharedPreferences(String usuario) {
        SharedPreferences sharedPre = getPreferences(Context.MODE_PRIVATE);
        return  sharedPre.getString(usuario,null);

    }
    private String getFromSharedPreferencesNull(String usuario) {
        usuario=null;
        SharedPreferences sharedPre = getPreferences(Context.MODE_PRIVATE);
        return  sharedPre.getString(usuario,null);
    }
    private boolean fullFields() {
        String usuario = user.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if(TextUtils.isEmpty(usuario)){
            Toast.makeText(MainActivity.this,"¡Upps no ingresaste tu Usuario!",Toast.LENGTH_LONG).show();
            return false;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this,"¡Upps no ingresaste contraseña!",Toast.LENGTH_LONG).show();
            return false;
        }

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
    public void registrar(View v){
        Intent intent2= new Intent(this, Main2Activity.class);
        startActivity(intent2);
    }

//hilos
    class Task1 extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            pro.setVisibility(View.VISIBLE);
            btnValidar.setEnabled(false);
        }
        @Override
        protected String doInBackground(String... strings) {
           @SuppressLint("WrongThread") String userLock = user.getText().toString();
            @SuppressLint("WrongThread") String passwordLock = pass.getText().toString();
            //simular tiempo
            try {
                // Thread.sleep(5000);
                return getData(userLock,passwordLock);//comprobar que usuario no exista devuelve true si no existe
            }catch (Exception  e){
                e.printStackTrace();
            }

            //return strings[0];
            return null;
        }

       @Override
        protected void onPostExecute(String s) {
            Log.d("Trae String",s);
            String idPrueba=s;
            if(s.equals("")){
                Toast.makeText(MainActivity.this,"¡Usuario no existe!",Toast.LENGTH_LONG).show();
                Intent intent= new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
            else if(s.equals("fail pass")){
                Toast.makeText(MainActivity.this,"¡Contraseña Incorrecta!",Toast.LENGTH_LONG).show();
                Intent intent= new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                final String USER_SHARED = idPrueba;
                saveLoginSharedPreferences(USER_SHARED);
                Toast.makeText(MainActivity.this,"¡Sesión Iniciada!",Toast.LENGTH_LONG).show();
                pro.setVisibility(View.INVISIBLE);
                btnValidar.setEnabled(true);
                Intent intent= new Intent(MainActivity.this, Main3Activity.class);
                intent.putExtra("usuario", user.getText()+ "");
                startActivity(intent);
            }

        }


        public String getData(String useri,String passweri){
            String sql = "https://tetelactanciamaterna.herokuapp.com/listUsers";
            String mensaje = "";
            String mensajeContrasena = "";
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
                    mensajeContrasena = jsonObject.optString("pass");
                    if(useri.equals(mensaje)){
                        Log.d("Usuario prueba 1",mensaje);
                        if(passweri.equals(mensajeContrasena)){
                            mensaje = jsonObject.optString("id");
                            return mensaje;
                        }
                        else {
                            mensaje = "fail pass";
                            return mensaje;
                        }
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

    }

    private void saveLoginSharedPreferences(String nom_user) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", nom_user);
        editor.commit();
    }
    private void saveLoginSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id","");
        editor.commit();
    }








        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user= (EditText) findViewById(R.id.usuario);
        pass= (EditText) findViewById(R.id.contraseña);
        btnLogin = (Button) findViewById(R.id.btnInicioSesion);

        btnMap = (Button) findViewById(R.id.btnMap);*/

/*
     btnLogin.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            //Toast.makeText(getApplicationContext(),"BIENVENIDO", Toast.LENGTH_SHORT).show();
            ingresoUser();
        }
    });*/


/*
       btnMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"BIENVENIDO", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivityForResult(intent,0);
            }
        });*/

    /*public void btnReg(View v){

        Intent intent2= new Intent(this, Main2Activity.class);
        startActivity(intent2);
    }
*/
/*
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
    public void ingresoUser() {
        int n;
        try {

            PreparedStatement pst = conexionBD().prepareStatement("SELECT Id_User FROM Tbl_Users WHERE Nom_User ='"+edtNombre.getText().toString() +"' and Pass='"+edtPass.getText().toString() +"'");
            ResultSet a =pst.executeQuery();
            if(a.next()) {
               // n=a.getInt(1);
               // guardarpreferencias(n);
                Toast.makeText(getApplicationContext(),"BIENVENIDO", Toast.LENGTH_SHORT).show();
                Intent ListSong = new Intent(getApplicationContext(), Main3Activity.class);
                startActivity(ListSong);
            }else{
                Toast.makeText(getApplicationContext(), "USUARIO O CONTRASEÑA ERRONEA", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }*/

    /*public void guardarpreferencias(int x){

        String Nombr="";
        String Use="";
        String Ape="";
        String Mail="";
        String Edad="";

        SharedPreferences myPreferences
                = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        try {

            PreparedStatement pst = conexionBD().prepareStatement("SELECT Nombre FROM Ingreso WHERE UserId ="+x+"");
            ResultSet a =pst.executeQuery();
            if(a.next()) {
                Nombr=a.getString(1);
                myEditor.putString("Nombre", Nombr);
            }
            PreparedStatement pst1 = conexionBD().prepareStatement("SELECT Usuario FROM Ingreso WHERE UserId ="+x+"");
            ResultSet a1 =pst1.executeQuery();
            if(a1.next()) {
                Use=a1.getString(1);
                myEditor.putString("Usuario",Use);
            }
            PreparedStatement pst2 = conexionBD().prepareStatement("SELECT Apellido FROM Ingreso WHERE UserId ="+x+"");
            ResultSet a2 =pst2.executeQuery();
            if(a2.next()) {
                Ape=a2.getString(1);
                myEditor.putString("Apellido",Ape);
            }
            PreparedStatement pst3 = conexionBD().prepareStatement("SELECT Correo FROM Ingreso WHERE UserId ="+x+"");
            ResultSet a3 =pst3.executeQuery();
            if(a3.next()) {
                Mail=a3.getString(1);
                myEditor.putString("Mail",Mail);
            }
            PreparedStatement pst4 = conexionBD().prepareStatement("SELECT Edad FROM Ingreso WHERE UserId ="+x+"");
            ResultSet a4 =pst4.executeQuery();
            if(a4.next()) {
                int ed=a4.getInt(1);
                Edad=Integer.toString(ed);
                myEditor.putString("Edad",Edad);
            }
            myEditor.commit();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public String CarNomb(){
        SharedPreferences myPreferences
                = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        String Nomb=myPreferences.getString("Nombre","unknown");

        return Nomb;
    }
    public String CarUser(){
        SharedPreferences myPreferences
                = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        String UserI=myPreferences.getString("Usuario","unknown");

        return UserI;
    }
    public String CarApe(){
        SharedPreferences myPreferences
                = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        String Ape=myPreferences.getString("Apellido","unknown");

        return Ape;
    }
    public String CarEdad(){
        SharedPreferences myPreferences
                = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        String Edad=myPreferences.getString("Edad","unknown");

        return Edad;
    }
    public String CarMail(){
        SharedPreferences myPreferences
                = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        String Mail=myPreferences.getString("Mail","unknown");

        return Mail;
    }*/

}




