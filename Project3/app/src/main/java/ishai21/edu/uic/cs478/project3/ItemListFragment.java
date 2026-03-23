package ishai21.edu.uic.cs478.project3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ItemListFragment extends Fragment {

    public interface OnPlaceSelectedListener {
        void onPlaceSelected(int index);
    }

    private ListView listView;
    private OnPlaceSelectedListener listener;

    public static ItemListFragment newInstance() {
        return new ItemListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(requireActivity() instanceof OnPlaceSelectedListener)) {
            throw new IllegalStateException("Host must implement OnPlaceSelectedListener");
        }
        listener = (OnPlaceSelectedListener) requireActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.place_list);
        BaseBrowseActivity activity = (BaseBrowseActivity) requireActivity();
        String[] names = activity.getPlaceNamesForList();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_list_item_activated_1,
                        names);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                (parent, v, position, id) -> {
                    listView.setItemChecked(position, true);
                    if (listener != null) {
                        listener.onPlaceSelected(position);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listView == null) {
            return;
        }
        BaseBrowseActivity activity = (BaseBrowseActivity) requireActivity();
        int idx = activity.getSelectedIndex();
        if (idx >= 0 && idx < listView.getCount()) {
            listView.setItemChecked(idx, true);
        }
    }

    public void clearSelection() {
        if (listView != null) {
            listView.clearChoices();
            listView.requestLayout();
        }
    }
}
