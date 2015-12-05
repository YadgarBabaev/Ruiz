package app.ruiz;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> name;
    private final List<Integer> imageId;
    private final List<Integer> likes;
    public CustomList(Activity context, List<String> name, List<Integer> imageId, List<Integer> likes) {
        super(context, R.layout.listview_row, name);
        this.context = context;
        this.name = name;
        this.imageId = imageId;
        this.likes = likes;
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.listview_row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.text);
        TextView txtLikes = (TextView) rowView.findViewById(R.id.like_count);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        txtTitle.setText(name.get(position));
        txtLikes.setText(String.valueOf(likes.get(position)));
        String url = "http://baas.hol.es/images/" + imageId.get(position) + ".jpg";
        ImageManager man = new ImageManager();
        man.fetchImage(context, 60, url, imageView);
        // imageView.setImageResource(imageId[position]);

        return rowView;
    }
}