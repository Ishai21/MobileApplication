package ishai21.edu.uic.cs478.project3;

import android.os.Bundle;

import androidx.annotation.Nullable;

public class AttractionsActivity extends BaseBrowseActivity {

    private static final String[] ATTRACTION_NAMES = {
            "Lincoln Park Zoo",
            "Navy Pier",
            "Museum of Science and Industry",
            "Art Institute of Chicago",
            "TILT at 360 CHICAGO"
    };

    private static final String[] ATTRACTION_URLS = {
            "https://www.lpzoo.org/",
            "https://navypier.org/",
            "https://www.msichicago.org/",
            "https://www.artic.edu/",
            "https://360chicago.com/tilt/"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.screen_title_attractions);
    }

    @Override
    protected String[] getPlaceNames() {
        return ATTRACTION_NAMES;
    }

    @Override
    protected String[] getPlaceUrls() {
        return ATTRACTION_URLS;
    }
}
