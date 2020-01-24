package app.centrolactancia.tete;

import androidx.appcompat.app.AppCompatActivity;

import  java.sql.Connection;
import  java.sql.DriverManager;
import  java.sql.PreparedStatement;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tete.R;

public class Main2Activity extends AppCompatActivity {

    private EditText edtNombreR,edtUsuarioR,edtCorreoR,edtContraseñaR;
    private String nombre,usuario,correo,contraseña,newid;
    Button btnRegR, btnS2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        btnRegR = (Button) findViewById(R.id.btnRegistro2);

        btnRegR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        btnS2 = (Button) findViewById(R.id.btnSig2);
        btnS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), Main3Activity.class);
                startActivityForResult(intent, 0);
            }
        });
    }


    public void validate(){
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
    }

}

