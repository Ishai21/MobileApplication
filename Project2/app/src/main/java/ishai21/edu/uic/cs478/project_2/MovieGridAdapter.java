package ishai21.edu.uic.cs478.project_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieGridAdapter extends BaseAdapter {

    private final Context context;
    private final String[] titles;
    private final int[] imageResIds;
    private final LayoutInflater inflater;

    public MovieGridAdapter(Context context, String[] titles, int[] imageResIds) {
        this.context = context;
        this.titles = titles;
        this.imageResIds = imageResIds;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item_movie, parent, false);
            holder = new ViewHolder();
            holder.posterImageView = convertView.findViewById(R.id.imageViewPosterThumb);
            holder.titleTextView = convertView.findViewById(R.id.textViewTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.posterImageView.setImageResource(imageResIds[position]);
        holder.titleTextView.setText(titles[position]);

        return convertView;
    }

    private static class ViewHolder {
        ImageView posterImageView;
        TextView titleTextView;
    }
}

