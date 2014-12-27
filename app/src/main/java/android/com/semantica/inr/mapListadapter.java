package android.com.semantica.inr;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by ozcan on 12/1/2014.
 */
public class mapListadapter extends ArrayAdapter<String> {
    public final Activity context;
    public String[] names = null;
    public String[] addresses = null;
    public String[] latitude = null;
    public String[] longitude = null;

    public mapListadapter(Activity context, String[] names, String[] addresses, String[] latitude, String[] longitude) {
        super(context, R.layout.list_layout, names);
        this.context = context;
        this.names = names;
        this.addresses = addresses;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;

        LayoutInflater inflater = context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.list_layout, null, true);
        TextView txtName = (TextView) rowView.findViewById(R.id.listName);
        TextView txtAddress = (TextView) rowView.findViewById(R.id.listAdress);
        TextView lat = (TextView) rowView.findViewById(R.id.latitude);
        TextView longi = (TextView) rowView.findViewById(R.id.longitude);

        lat.setText(latitude[position]);
        longi.setText(longitude[position]);
        txtName.setText(names[position]);
        txtAddress.setText(addresses[position]);

        return rowView;
    }
}
