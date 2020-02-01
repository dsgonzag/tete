package app.centrolactancia.tete;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tete.R;

public class Main3Activity extends AppCompatActivity {
Button btnS3, btnS4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        btnS3 = (Button) findViewById(R.id.btnDonar);
        btnS4 = (Button) findViewById(R.id.btnSolicitar);

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
                Intent intent = new Intent (v.getContext(), MapsActivity.class);
                startActivityForResult(intent, 0);

            }
        });
    }
}
