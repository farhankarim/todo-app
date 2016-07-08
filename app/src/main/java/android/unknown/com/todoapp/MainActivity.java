package android.unknown.com.todoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //create variables to store objects
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    /*
    * First, we need to add the commons.io
     * library into our gradle file dependencies to make
     * reading and writing easier by modifying app/build.gradle:
    * BELOW ARE TWO METHODS READ AND WRITE TO READ FROM THE ARRAYLIST AND WRITE TO A TEXTFILE TODO.TEXT
    *
    * */
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Load items during onCreate()
        readItems();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //declare and assignment
        lvItems = (ListView) findViewById(R.id.lvItems);
        //items = new ArrayList<>();   IT IS REMOVED BECAUSE WE ARE INITIALIZING IT IN THE READ ITEMS METHOD YOU CAN USE OTHERWISE
        //basically says that
        // create an array adapter that deals with a simple list
        // one item per line
        // and use the array list created above
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        //set the adapter to the listview
        lvItems.setAdapter(itemsAdapter);
        //Invoke listener for longclick
        setupListViewListener();

    }

    // Attaches a long click listener to the listview
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        items.remove(pos);
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        //Save items when a list item is removed
                        writeItems();
                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }

                });
    }

    /* Action Listener that adds the item entered in
     edittext into the list when the button add is pressed*/
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        //the itemsadapter adds the entered edittext converted
        // to string into the arraylist and then empties the edit text
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        //Save items when a new list item is added
        writeItems();
    }

}