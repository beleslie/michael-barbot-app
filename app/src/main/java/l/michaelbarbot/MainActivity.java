package l.michaelbarbot;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button drink1;
    private Button drink2;
    private Button drink3;
    private String selectedDrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize the drink buttons
        drink1 = (Button) findViewById(R.id.drink1_button);
        drink2 = (Button) findViewById(R.id.drink2_button);
        drink3 = (Button) findViewById(R.id.drink3_button);

        // initialize the selected drink
        selectedDrink = "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectDrink(View view) {
        // define color values
        int blue = ResourcesCompat.getColor(getResources(), R.color.blue, null);
        int white = ResourcesCompat.getColor(getResources(), R.color.white, null);
        int gray = ResourcesCompat.getColor(getResources(), R.color.gray, null);

        // get selected drink
        Button button = (Button) view;
        String newSelection = button.getText().toString();

        // check if drink already selected
        if (selectedDrink.equals(newSelection)) {
            // color the button white
            button.setBackgroundColor(white);
            button.setTextColor(gray);

            // remove selection
            selectedDrink = "";

        } else {
            // set selected drink to new selection
            selectedDrink = newSelection;

            // color all buttons white (instead of figuring out which was most recently colored blue)
            drink1.setBackgroundColor(white);
            drink1.setTextColor(gray);
            drink2.setBackgroundColor(white);
            drink2.setTextColor(gray);
            drink3.setBackgroundColor(white);
            drink3.setTextColor(gray);

            // color selected button blue
            button.setBackgroundColor(blue);
            button.setTextColor(white);
        }
    }

    public void submit(View view) {
        // check if there is a selection
        if (selectedDrink.equals("")) {
            // post a flag saying "Please select a drink"
            Toast toast = Toast.makeText(this, "Please select a drink.",
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            // send drink order to robot
            // bring up a flag that says the order was submitted?
            // switch to a waiting screen with animation of the robot pouring the drink?
            // until the drink is prepared?
        }
    }
}
