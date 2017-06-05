package l.michaelbarbot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

// ROS imports
import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;

public class MainActivity extends RosActivity {
    private Button drink1;
    private Button drink2;
    private Button size1;
    private Button size2;
    private Button size3;
    private Button selectedDrink;
    private Button selectedSize;

    private Button submit;

    private int blue;
    private int white;
    private int gray;

    // ROS nodes
    private DrinkTalker drinkTalker;
    private DrinkListener drinkListener;

    private static final String hostName = "128.208.1.207";
    private static final String masterURI = "http://robonaut.cs.washington.edu:9090";
    // "128.208.1.207"
    // "http://robonaut.cs.washington.edu:11311";

    public MainActivity() {
        // The RosActivity constructor configures the notification title and ticker messages.
        super("Michael the BarBot", "Michael the BarBot", URI.create(masterURI));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // initialize the drink buttons
        drink1 = (Button) findViewById(R.id.drink1_button);
        drink2 = (Button) findViewById(R.id.drink2_button);

        // initialize the size buttons
        size1 = (Button) findViewById(R.id.size1_button);
        size2 = (Button) findViewById(R.id.size2_button);
        size3 = (Button) findViewById(R.id.size3_button);

        // initialize the submit button
        submit = (Button) findViewById(R.id.submit_button);

        // initialize the selected drink
        selectedDrink = null;

        // initialize the selected size
        selectedSize = null;

        // define color values
        blue = ResourcesCompat.getColor(getResources(), R.color.blue, null);
        white = ResourcesCompat.getColor(getResources(), R.color.white, null);
        gray = ResourcesCompat.getColor(getResources(), R.color.gray, null);
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
        // get selected drink
        Button newSelection = (Button) view;

        // check if drink already selected
        if (selectedDrink != null && selectedDrink.equals(newSelection)) {
            // color the button white
            selectedDrink.setBackgroundColor(white);
            selectedDrink.setTextColor(gray);

            // remove selection
            selectedDrink = null;

        } else {
            // color previously selected drink white
            if (selectedDrink != null) {
                selectedDrink.setBackgroundColor(white);
                selectedDrink.setTextColor(gray);
            }

            // set selected drink to new selection
            selectedDrink = newSelection;

            // color selected button blue
            selectedDrink.setBackgroundColor(blue);
            selectedDrink.setTextColor(white);
        }
    }

    public void selectSize(View view) {
        // get selected size
        Button newSelection = (Button) view;

        // check if size already selected
        if (selectedSize != null && selectedSize.equals(newSelection)) {
            // color the button white
            selectedSize.setBackgroundColor(white);
            selectedSize.setTextColor(gray);

            // remove selection
            selectedSize = null;

        } else {
            // color previously selected size white and gray
            if (selectedSize != null) {
                selectedSize.setBackgroundColor(white);
                selectedSize.setTextColor(gray);
            }

            // set selected drink to new selection
            selectedSize = newSelection;

            // color selected button blue
            selectedSize.setBackgroundColor(blue);
            selectedSize.setTextColor(white);
        }
    }

    public void submit(View view) {
        // check if there is a selection
        if (selectedDrink == null) {
            // raise a flag saying "Please select a drink"
            Toast toast = Toast.makeText(this, "Please select a drink.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (selectedSize == null) {
            // raise a flag saying "Please select a size"
            Toast toast = Toast.makeText(this, "Please select a size.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // send drink order to robot and set dialog message
            if (drinkTalker.orderDrink(selectedDrink.getText().toString(),
                    selectedSize.getText().toString())) {
                // bring up a flag that says the order was submitted
                builder.setMessage(R.string.submit_message).setTitle(R.string.submit_title);
            } else {
                // bring up a flag with an error message
                builder.setMessage(R.string.submit_error_message).setTitle(R.string.submit_error_title);
            }

            // finish building and display the dialog
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            // clear the selections
            selectedDrink.setBackgroundColor(white);
            selectedDrink.setTextColor(gray);
            selectedDrink = null;

            selectedSize.setBackgroundColor(white);
            selectedSize.setTextColor(gray);
            selectedSize = null;
        }
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        // initialize the publisher and subscriber nodes
        drinkTalker = new DrinkTalker();
        // drinkListener = new DrinkListener();

        try {
            // set up the node configuration
            NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(hostName); // TODO: figure out host
            nodeConfiguration.setMasterUri(getMasterUri());

            // execute the nodes
            nodeMainExecutor.execute(drinkTalker, nodeConfiguration);
            // nodeMainExecutor.execute(drinkListener, nodeConfiguration);
        } catch (Exception e) {
            // post message saying drink orders cannot be submitted right now
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.ros_error_message).setTitle(R.string.ros_error_title);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            // make all buttons unclickable
            drink1.setClickable(false);
            drink2.setClickable(false);
            size1.setClickable(false);
            size2.setClickable(false);
            size3.setClickable(false);
            submit.setClickable(false);
        }
    }
}
