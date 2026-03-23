package ishai21.edu.uic.cs478.project3;

import android.os.Bundle;

import androidx.annotation.Nullable;

public class RestaurantsActivity extends BaseBrowseActivity {

    private static final String[] RESTAURANT_NAMES = {
            "Alinea",
            "Girl & the Goat",
            "Lou Malnati's Pizzeria",
            "Portillo's",
            "The Berghoff Restaurant",
            "Superdawg Drive-In"
    };

    private static final String[] RESTAURANT_URLS = {
            "https://www.alinearestaurant.com/",
            "https://www.girlandthegoat.com/",
            "https://www.loumalnatis.com/",
            "https://www.portillos.com/",
            "https://berghoff.com/",
            "https://www.superdawg.com/"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.screen_title_restaurants);
    }

    @Override
    protected String[] getPlaceNames() {
        return RESTAURANT_NAMES;
    }

    @Override
    protected String[] getPlaceUrls() {
        return RESTAURANT_URLS;
    }
}
