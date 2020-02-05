package app.centrolactancia.tete;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tete.R;

import app.ejemplo.holaa.InformacionDonante;

public class Main3Activity extends AppCompatActivity {
Button btnS3, btnS4;
    private Context activity;
    private TextView usuario1;
    private static final String TAG = "Main3Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        btnS3 = (Button) findViewById(R.id.btnDonar);
        btnS4 = (Button) findViewById(R.id.btnSolicitar);


        TextView  usuario2 =findViewById(R.id.User);
        Bundle bundle = getIntent().getExtras();
        String usuario = getIntent().getStringExtra("usuario");
        usuario2.setText(usuario);

        btnS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), Main4Activity.class);
                startActivityForResult(intent, 0);
            }
        });

        btnS3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), InformacionDonante.class);
                startActivityForResult(intent, 0);

            }
        });
    }
}
