package ishai21.edu.uic.cs478.project_2;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int MENU_VIEW_POSTER = 1;
    private static final int MENU_VIEW_ROTTEN_TOMATOES = 2;
    private static final int MENU_VIEW_STREAMING = 3;

    private GridView gridView;

    // Movie data (titles, images, and links) in the order specified by the project
    private static final String[] MOVIE_TITLES = new String[]{
            "Kung Fu Panda",
            "Avatar",
            "The Lion King",
            "How to Train Your Dragon",
            "Toy Story",
            "Monsters, Inc.",
            "Ice Age",
            "Shrek",
            "Cars"
    };

    // TODO: Replace drawable placeholders with your actual poster thumbnail resources.
    // Make sure the drawable resource names exist in res/drawable.
    private static final int[] MOVIE_IMAGE_RES_IDS = new int[]{
            R.drawable.kung_fu_panda,
            R.drawable.avatar,
            R.drawable.the_lion_king,
            R.drawable.how_to_train_your_dragon,
            R.drawable.toy_story,
            R.drawable.monsters_inc,
            R.drawable.ice_age,
            R.drawable.shrek,
            R.drawable.cars
    };

    private static final String[] ROTTEN_TOMATOES_URLS = new String[]{
            "https://www.rottentomatoes.com/m/kung_fu_panda",
            "https://www.rottentomatoes.com/m/avatar",
            "https://www.rottentomatoes.com/m/the_lion_king",
            "https://www.rottentomatoes.com/m/how_to_train_your_dragon",
            "https://www.rottentomatoes.com/m/toy_story",
            "https://www.rottentomatoes.com/m/monsters_inc",
            "https://www.rottentomatoes.com/m/ice_age",
            "https://www.rottentomatoes.com/m/shrek",
            "https://www.rottentomatoes.com/m/cars"
    };

    private static final String[] IMDB_URLS = new String[]{
            "https://www.imdb.com/title/tt0441773/",
            "https://www.imdb.com/title/tt0499549/",
            "https://www.imdb.com/title/tt0110357/",
            "https://www.imdb.com/title/tt0892791/",
            "https://www.imdb.com/title/tt0114709/",
            "https://www.imdb.com/title/tt0198781/",
            "https://www.imdb.com/title/tt0268380/",
            "https://www.imdb.com/title/tt0126029/",
            "https://www.imdb.com/title/tt0317219/"
    };

    private static final String[] STREAMING_URLS = new String[]{
            "https://www.amazon.com/Kung-Fu-Panda-Jack-Black/dp/B001F7008E",
            "https://www.disneyplus.com/movies/avatar/1770000948",
            "https://www.disneyplus.com/movies/the-lion-king/1770000951",
            "https://www.amazon.com/How-Train-Your-Dragon-Jay-Baruchel/dp/B0040Z7KCS",
            "https://www.disneyplus.com/movies/toy-story/1770000958",
            "https://www.disneyplus.com/movies/monsters-inc/1770000955",
            "https://www.disneyplus.com/movies/ice-age/1770000963",
            "https://www.amazon.com/Shrek-Mike-Myers/dp/B004G7S6KW",
            "https://www.disneyplus.com/movies/cars/1260017229"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridViewMovies);

        MovieGridAdapter adapter = new MovieGridAdapter(
                this,
                MOVIE_TITLES,
                MOVIE_IMAGE_RES_IDS
        );
        gridView.setAdapter(adapter);

        // Short click: show full poster in a new activity
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPosterActivity(position);
            }
        });

        // Long click: show context menu
        registerForContextMenu(gridView);
    }

    private void openPosterActivity(int position) {
        Intent intent = new Intent(this, PosterActivity.class);
        intent.putExtra(PosterActivity.EXTRA_TITLE, MOVIE_TITLES[position]);
        intent.putExtra(PosterActivity.EXTRA_IMAGE_RES_ID, MOVIE_IMAGE_RES_IDS[position]);
        intent.putExtra(PosterActivity.EXTRA_IMDB_URL, IMDB_URLS[position]);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.gridViewMovies) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            int position = info.position;
            menu.setHeaderTitle(MOVIE_TITLES[position]);

            menu.add(0, MENU_VIEW_POSTER, 0, "View full poster");
            menu.add(0, MENU_VIEW_ROTTEN_TOMATOES, 1, "Open Rotten Tomatoes");
            menu.add(0, MENU_VIEW_STREAMING, 2, "Open streaming site");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (info == null) {
            return super.onContextItemSelected(item);
        }

        int position = info.position;

        switch (item.getItemId()) {
            case MENU_VIEW_POSTER:
                openPosterActivity(position);
                return true;

            case MENU_VIEW_ROTTEN_TOMATOES:
                openUrl(ROTTEN_TOMATOES_URLS[position]);
                return true;

            case MENU_VIEW_STREAMING:
                openUrl(STREAMING_URLS[position]);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void openUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request.", Toast.LENGTH_SHORT).show();
        }
    }
}

