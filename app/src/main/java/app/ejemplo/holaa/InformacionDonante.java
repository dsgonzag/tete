package app.ejemplo.holaa;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tete.R;

public class InformacionDonante extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_donante);
        Button btnS4 = (Button) findViewById(R.id.btnsig4);
        btnS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), informaciondonantee.class);
                startActivityForResult(intent, 0);
            }
        });

    }
}
