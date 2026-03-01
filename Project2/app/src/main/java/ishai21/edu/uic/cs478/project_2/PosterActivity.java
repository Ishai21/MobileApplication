package ishai21.edu.uic.cs478.project_2;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class PosterActivity extends Activity {

    public static final String EXTRA_TITLE = "ishai21.edu.uic.cs478.project_2.EXTRA_TITLE";
    public static final String EXTRA_IMAGE_RES_ID = "ishai21.edu.uic.cs478.project_2.EXTRA_IMAGE_RES_ID";
    public static final String EXTRA_IMDB_URL = "ishai21.edu.uic.cs478.project_2.EXTRA_IMDB_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        ImageView posterImageView = findViewById(R.id.imageViewPosterFull);

        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        int imageResId = intent.getIntExtra(EXTRA_IMAGE_RES_ID, 0);
        String imdbUrl = intent.getStringExtra(EXTRA_IMDB_URL);

        if (title != null) {
            setTitle(title);
        }

        if (imageResId != 0) {
            posterImageView.setImageResource(imageResId);
        }

        posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imdbUrl != null && !imdbUrl.isEmpty()) {
                    openImdbPage(imdbUrl);
                } else {
                    Toast.makeText(PosterActivity.this, "No IMDb URL available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openImdbPage(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request.", Toast.LENGTH_SHORT).show();
        }
    }
}

