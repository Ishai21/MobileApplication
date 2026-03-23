package ishai21.edu.uic.cs478.project3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public abstract class BaseBrowseActivity extends AppCompatActivity
        implements ItemListFragment.OnPlaceSelectedListener {

    private static final String STATE_SELECTED_INDEX = "state_selected_index";

    private FrameLayout listContainer;
    private FrameLayout detailContainer;

    private String[] urls;
    private int selectedIndex = -1;

    private OnBackPressedCallback backCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listContainer = findViewById(R.id.fragment_list);
        detailContainer = findViewById(R.id.fragment_detail);

        urls = getPlaceUrls();

        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(STATE_SELECTED_INDEX, -1);
        }

        backCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                clearDetailSelection();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, backCallback);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_list, ItemListFragment.newInstance(), "list")
                    .commitNow();
        }

        getSupportFragmentManager().executePendingTransactions();
        if (selectedIndex >= 0) {
            applySplitLayout(true);
            WebDetailFragment detail =
                    (WebDetailFragment) getSupportFragmentManager().findFragmentByTag("detail");
            if (detail == null) {
                detail = WebDetailFragment.newInstance(urls[selectedIndex]);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_detail, detail, "detail")
                        .commitNow();
            } else {
                detail.loadUrl(urls[selectedIndex]);
            }
        } else {
            applySplitLayout(false);
        }

        updateBackCallbackState();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_INDEX, selectedIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guide_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_attractions) {
            if (!(this instanceof AttractionsActivity)) {
                startActivity(new Intent(this, AttractionsActivity.class));
                finish();
            }
            return true;
        }
        if (id == R.id.action_restaurants) {
            if (!(this instanceof RestaurantsActivity)) {
                startActivity(new Intent(this, RestaurantsActivity.class));
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public String[] getPlaceNamesForList() {
        return getPlaceNames();
    }

    protected abstract String[] getPlaceNames();

    protected abstract String[] getPlaceUrls();

    @Override
    public void onPlaceSelected(int index) {
        selectedIndex = index;
        applySplitLayout(true);
        WebDetailFragment detail =
                (WebDetailFragment) getSupportFragmentManager().findFragmentByTag("detail");
        if (detail == null) {
            detail = WebDetailFragment.newInstance(urls[index]);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_detail, detail, "detail")
                    .commitNow();
        } else {
            detail.loadUrl(urls[index]);
        }
        updateBackCallbackState();
    }

    public void clearDetailSelection() {
        selectedIndex = -1;
        ItemListFragment list =
                (ItemListFragment) getSupportFragmentManager().findFragmentByTag("list");
        if (list != null) {
            list.clearSelection();
        }
        WebDetailFragment detail =
                (WebDetailFragment) getSupportFragmentManager().findFragmentByTag("detail");
        if (detail != null) {
            getSupportFragmentManager().beginTransaction().remove(detail).commitNow();
        }
        applySplitLayout(false);
        updateBackCallbackState();
    }

    private void applySplitLayout(boolean split) {
        LinearLayout.LayoutParams listLp = (LinearLayout.LayoutParams) listContainer.getLayoutParams();
        LinearLayout.LayoutParams detailLp = (LinearLayout.LayoutParams) detailContainer.getLayoutParams();
        if (split) {
            detailContainer.setVisibility(View.VISIBLE);
            listLp.weight = 1f;
            detailLp.weight = 2f;
        } else {
            detailContainer.setVisibility(View.GONE);
            listLp.weight = 1f;
            detailLp.weight = 0f;
        }
        listContainer.setLayoutParams(listLp);
        detailContainer.setLayoutParams(detailLp);
    }

    private void updateBackCallbackState() {
        backCallback.setEnabled(selectedIndex >= 0);
    }
}
