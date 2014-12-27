package android.com.semantica.inr;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Random;


public class index extends ActionBarActivity {
    final static double min = 0.6;
    final static double max = 4.0;
    DrawerLayout drawerLayout;
    dbAdapter helper = new dbAdapter(this);
    FrameLayout fL;
    ListView drawerList, drawerListR;
    ImageButton calc, suggest;
    TextView result;
    ActionBarDrawerToggle dToggle;
    lvAdapter adapter;
    String[] menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        menu = getResources().getStringArray(R.array.menu);
        adapter = new lvAdapter(this);
        fL = (FrameLayout) findViewById(R.id.mContent);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        calc = (ImageButton) findViewById(R.id.calculate);
        suggest = (ImageButton) findViewById(R.id.suggest);
        result = (TextView) findViewById(R.id.result);
        drawerList = (ListView) findViewById(R.id.drawer);
        drawerListR = (ListView) findViewById(R.id.drawerRight);
        drawerListR.setAdapter(new ArrayAdapter<String>(index.this, android.R.layout.simple_list_item_1,
                new String[]{"Önerileri görmek için lütfen ana ekrandaki tuşa basınız."}));
        dToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerList.setAdapter(adapter);
        drawerLayout.setDrawerListener(dToggle);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

                if (position == 2) {
                    if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

                        int requestCode = 10;
                        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, index.this, requestCode);
                        dialog.show();

                    } else {
                        Intent i = new Intent(index.this, mapActivity.class);
                        i.putExtra("type", "hospital");
                        startActivity(i);
                    }
                } else if (position == 1) {
                    if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

                        int requestCode = 10;
                        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, index.this, requestCode);
                        dialog.show();

                    } else {
                        Intent i = new Intent(index.this, mapActivity.class);
                        i.putExtra("type", "pharmacy");
                        startActivity(i);
                    }

                }
                if (position == 3) {


                    Intent i = new Intent(index.this, graph.class);
                    startActivity(i);


                }
                selectItem(position);
            }
        });
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rdm = new Random();
                Double r = min + (max - min) * rdm.nextDouble();
                result.setText(new DecimalFormat("##.##").format(r));
                try {
                    helper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                DecimalFormat formatter = new DecimalFormat("0.00");
                String result = formatter.format(r).toString().replace(",", ".");
                r = Double.parseDouble(result);
                helper.insert(r);

                helper.close();
                View root = fL.getRootView();
                if (r <= 3.5 && r > 2.5) {
                    root.setBackgroundColor(Color.YELLOW);
                } else if (r <= 2.5 && r > 1.2) {
                    root.setBackgroundColor(Color.YELLOW);
                } else if (r < 1.2) {
                    root.setBackgroundColor(Color.GREEN);
                } else if (r > 3.5) {
                    root.setBackgroundColor(Color.RED);
                }

            }
        });
        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat formatter = new DecimalFormat("0.00");
                double res = 0;
                int count = 0;
                String[] resultArr = new String[7];

                resultArr[0] = "ÖNERİ SAYFASI";
                resultArr[1] = "Son kan değerleriniz";
                resultArr[2] = "için yapacağımız öneri";
                resultArr[3] = "aşağıdadır.";
                resultArr[4] = "";
                resultArr[5] = "";
                try {
                    helper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Cursor c = helper.getAllRecords();

                c.moveToLast();
                while (count < 10 || c.moveToPrevious()) {
                    res = res + c.getDouble(1);
                    count++;
                    c.moveToPrevious();
                }

                res = res / count;
                helper.close();
                String resultToScreen = formatter.format(res).toString().replace(",", ".");

                if (res < 3.5 && res > 2.5) {
                    resultArr[6] = "" + resultToScreen + ":Biraz yemeğinize dikkat etmelisiniz!";
                } else if (res <= 2.5 && res > 1.2) {
                    resultArr[6] = "" + resultToScreen + ":Değerleriniz normal seviyede, özel bir önerimiz yok!";
                } else if (res <= 1.2 && res < 0.5) {
                    resultArr[6] = "" + resultToScreen + ":Değerleriniz çok düşük, gıda takviyesi alın!";
                } else if (res > 3.5) {
                    resultArr[6] = "" + resultToScreen + ":Değerleriniz çok yüksek, hemen doktorunuzla görüşün";
                } else {
                    resultArr[6] = "" + resultToScreen + ":Değerleriniz aşırı seviyede düşük, doktorunuza başvurun";
                }


                drawerListR.setAdapter(new ArrayAdapter<String>(index.this, android.R.layout.simple_list_item_1, resultArr));
                drawerLayout.openDrawer(drawerListR);

            }
        });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        dToggle.syncState();
    }

    private void selectItem(int position) {
        drawerList.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_index, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (dToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class lvAdapter extends BaseAdapter {

    Context context;
    index c = new index();
    String[] menu;
    int[] image = {R.drawable.home, R.drawable.pharmacy, R.drawable.hospital, R.drawable.graph, R.drawable.about};
    ;

    public lvAdapter(Context context) {
        this.context = context;
        menu = context.getResources().getStringArray(R.array.menu);
    }

    @Override
    public int getCount() {
        return menu.length;
    }

    @Override
    public Object getItem(int position) {
        return menu[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.drawer_layout, parent, false);
            ImageView images = (ImageView) row.findViewById(R.id.dLImage);
            TextView title = (TextView) row.findViewById(R.id.dLtxt);
            title.setText(menu[position]);
            images.setImageResource(image[position]);
        } else {
            row = convertView;
        }

        return row;
    }

}