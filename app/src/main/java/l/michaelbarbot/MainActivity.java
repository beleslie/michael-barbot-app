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
import org.ros.address.InetAddressFactory;
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
    private String selectedDrink;
    private String selectedSize;

    private Button submit;

    private int blue;
    private int white;
    private int gray;

    // ROS nodes
    private DrinkTalker drinkTalker;
    private DrinkListener drinkListener;

    private static final String masterURI = "ws://robonaut.cs.washington.edu:9090";

    public MainActivity() {
        // The RosActivity constructor configures the notification title and ticker messages.
        super("Michael the BarBot", "Michael the BarBot");
    }

    @Override
    public void startMasterChooser() {
        super.startMasterChooser();
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

        // initialize the selected drink
        selectedDrink = "";

        // initialize the selected size
        selectedSize = "";

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

            // color selected button blue
            button.setBackgroundColor(blue);
            button.setTextColor(white);
        }
    }

    public void selectSize(View view) {
        // get selected size
        Button button = (Button) view;
        String newSelection = button.getText().toString();

        // check if size already selected
        if (selectedSize.equals(newSelection)) {
            // color the button white
            button.setBackgroundColor(white);
            button.setTextColor(gray);

            // remove selection
            selectedSize = "";

        } else {
            // set selected drink to new selection
            selectedSize = newSelection;

            // color all buttons white (instead of figuring out which was most recently colored blue)
            size1.setBackgroundColor(white);
            size1.setTextColor(gray);
            size2.setBackgroundColor(white);
            size2.setTextColor(gray);
            size3.setBackgroundColor(white);
            size3.setTextColor(gray);

            // color selected button blue
            button.setBackgroundColor(blue);
            button.setTextColor(white);
        }
    }

    public void submit(View view) {
        // check if there is a selection
        if (selectedDrink.equals("")) {
            // raise a flag saying "Please select a drink"
            Toast toast = Toast.makeText(this, "Please select a drink.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (selectedSize.equals("")) {
            // raise a flag saying "Please select a size"
            Toast toast = Toast.makeText(this, "Please select a size.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // send drink order to robot and set dialog message
            if (drinkTalker.orderDrink(selectedDrink, selectedSize)) {
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
        }
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        // initialize the publisher and subscriber nodes
        drinkTalker = new DrinkTalker();
        drinkListener = new DrinkListener();

        try {
            URI master = URI.create(masterURI);

            // set up the node configuration
            NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(
                    getRosHostname()); // TODO: figure out host
            nodeConfiguration.setMasterUri(master);

            // execute the nodes
            nodeMainExecutor.execute(drinkTalker, nodeConfiguration);
            nodeMainExecutor.execute(drinkListener, nodeConfiguration);
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
