package android.com.semantica.inr;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.ValueDependentColor;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class graph extends ActionBarActivity {
    ListView lv;
    dbAdapter helper = new dbAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        int i = 1;
        int counter = 0;
        try {
            helper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor c = helper.getAllRecords();
        c.moveToFirst();
        while (c.moveToNext()) {
            counter++;
        }
        counter++;
        GraphView.GraphViewData[] allvalues = new GraphView.GraphViewData[counter];
        c.moveToFirst();
        for (i = 0; i < counter; i++) {
            allvalues[i] = new GraphView.GraphViewData(i, Double.parseDouble(c.getString(1)));
            c.moveToNext();
        }
        GraphViewSeries.GraphViewSeriesStyle seriesStyle = new GraphViewSeries.GraphViewSeriesStyle();
        seriesStyle.setValueDependentColor(new ValueDependentColor() {
            @Override
            public int get(GraphViewDataInterface data) {
                if (data.getY() <= 3.5 && data.getY() > 2.5) {
                    return Color.parseColor("#FF6600");

                } else if (data.getY() <= 2.5 && data.getY() > 1.2) {

                    return Color.YELLOW;
                } else if (data.getY() < 1.2) {

                    return Color.GREEN;
                } else {
                    return Color.RED;
                }

            }
        });
        GraphViewSeries series = new GraphViewSeries("a",seriesStyle,allvalues);

        GraphView graphView = new BarGraphView(
                this // context
                , "Kayıtlar" // heading
        );

        graphView.addSeries(series); // data
        graphView.setViewPort(1, 10);
        graphView.setScrollable(true);
        graphView.setScalable(true);
        graphView.scrollToEnd();
        graphView.getGraphViewStyle().setTextSize(15);
        graphView.getGraphViewStyle().setNumVerticalLabels(5);
        graphView.getGraphViewStyle().setVerticalLabelsWidth(10);
        graphView.getGraphViewStyle().setNumHorizontalLabels(4);
        graphView.setManualYAxisBounds(4.0, 0.0);
        LinearLayout layout = (LinearLayout) findViewById(R.id.graphlayout);
        layout.addView(graphView);
        fillListView();
        helper.close();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kayıtlar");
    }

    private void fillListView() {


        final Cursor c = helper.getAllRecords();
        lv = (ListView) findViewById(R.id.listView2);
        String[] fieldNames = new String[]{
                dbAdapter.KEY_VALUE, dbAdapter.KEY_DATE
        };
        int[] viewIDs = new int[]{
                R.id.txtValue, R.id.txtDate
        };

        final SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.database_layout,
                c,
                fieldNames,
                viewIDs
        );

        lv.setAdapter(cursorAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
