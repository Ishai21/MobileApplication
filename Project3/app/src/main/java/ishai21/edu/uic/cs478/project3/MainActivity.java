package ishai21.edu.uic.cs478.project3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button attractionsButton = findViewById(R.id.button_attractions);
        Button restaurantsButton = findViewById(R.id.button_restaurants);

        attractionsButton.setOnClickListener(v -> {
            Toast.makeText(this, R.string.toast_attractions, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AttractionsActivity.class));
        });

        restaurantsButton.setOnClickListener(v -> {
            Toast.makeText(this, R.string.toast_restaurants, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, RestaurantsActivity.class));
        });
    }
}
