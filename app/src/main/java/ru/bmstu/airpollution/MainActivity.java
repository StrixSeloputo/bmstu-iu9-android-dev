package ru.bmstu.airpollution;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.InputMismatchException;
import java.util.Scanner;

import ru.bmstu.airpollution.model.Toxin;
import ru.bmstu.airpollution.model.ToxinType;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    Toxin toxin = new Toxin();
    Dialog infoDialog;

    private void setToxinInfo(ToxinType type) {
        toxin.setType(type);

        TextView toxinSubscriptView = findViewById(R.id.scrolled_toxin_text);
        toxinSubscriptView.setText(toxin.getSubscript());

        TextView toxinLabel = findViewById(R.id.toxic_gas_label);
        toxinLabel.setText(toxin.getLabel());

        ImageView imageView = findViewById(R.id.android_img);

        switch (toxin.getType()) {
            case CO:
                imageView.setImageResource(R.drawable.carbon_monoxide);
                break;
            case SO2:
                imageView.setImageResource(R.drawable.sulfur_dioxide);
                break;
        }
    }

    void initDialog() {
        infoDialog = new Dialog(MainActivity.this);
        infoDialog.setTitle("Application info");
        infoDialog.setContentView(R.layout.dialog_view);
        TextView text = infoDialog.findViewById(R.id.dialog_text);
        text.setText("This application finds pollution data for specified location in 7,8km radius.\n");
        text.append("Extreme pressure and mixin ratio values will marked with red color.\n");
        text.append("Take care of your health!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToxinInfo(ToxinType.CO);

        initDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id) {

            case R.id.toxin_co:
                changeToxin(ToxinType.CO);
                return true;

            case R.id.toxin_so2:
                changeToxin(ToxinType.SO2);
                return true;

            case R.id.menu_about:
                infoDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeToxin(ToxinType toxinType) {
        setToxinInfo(toxinType);
    }

    public void startListOfPollutionData(View view) {
        double x = 0, y = 10;
        EditText user_location = findViewById(R.id.user_location);
        String location = user_location.getText().toString();
        if (!location.equals("")) {
            try (Scanner sc = new Scanner(user_location.getText().toString())) {
                x = sc.nextDouble();
                y = sc.nextDouble();
            } catch (InputMismatchException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        Log.i(TAG, "startListOfPlacesActivity: search: " + toxin.getType().toString() + "; " + x + "; " + y);

        Intent listDataIntent = new Intent(this, ListOfPollutionDataActivity.class);
        listDataIntent.putExtra(ListOfPollutionDataActivity.EXTRA_TOXIN_TYPE_VALUE, toxin.getType().toString());
        listDataIntent.putExtra(ListOfPollutionDataActivity.EXTRA_SEARCH_X_VALUE, Double.toString(x));
        listDataIntent.putExtra(ListOfPollutionDataActivity.EXTRA_SEARCH_Y_VALUE, Double.toString(y));

        startActivity(listDataIntent);
    }
}
