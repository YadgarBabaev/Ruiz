package app.ruiz;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//Adapter class extends with BaseAdapter and implements with OnClickListener
public class LazyImageLoadAdapter extends BaseAdapter{

    private List<Integer> id, like_count;
    private List<String> name;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public LazyImageLoadAdapter(Activity a, List<Integer> i, List<String> s, List<Integer> l) {
        id = i;
        name = s;
        like_count = l;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create ImageLoader object to download and show image in list
        // Call ImageLoader constructor to initialize FileCache
        imageLoader = new ImageLoader(a.getApplicationContext());
    }

    public int getCount() {
        return id.size();
    }

    public Object getItem(int position) {return position;}

    public long getItemId(int position) {return position;}

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView text;
        public TextView text1;
        public ImageView image;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi=convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate item file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.listview_row, null);

            /****** View Holder Object to contain item file elements ******/
            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.text);
            holder.text1=(TextView)vi.findViewById(R.id.like_count);
            holder.image=(ImageView)vi.findViewById(R.id.image);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else holder=(ViewHolder)vi.getTag();

        holder.text.setText(name.get(position));
        holder.text1.setText(String.valueOf(like_count.get(position)));
        ImageView image = holder.image;

        //DisplayImage function from ImageLoader Class

        imageLoader.DisplayImage("http://baas.hol.es/images/" + id.get(position) + ".jpg", image);
        return vi;
    }
}