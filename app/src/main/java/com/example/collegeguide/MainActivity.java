package com.example.collegeguide;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.view.View;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private WebView webView;
    private TabHost tabs;


    // I used the TabDemo as a basis so a lot does not need to be gone over like the basics of setting up the tabs

    @Override
    public void onCreate(@Nullable Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        tabs=(TabHost)findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec;



        // here I set up my map fragment in order to get the functionality to work

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize a TabSpec for tab1 and add it to the TabHost
        spec=tabs.newTabSpec("tag1");	//create new tab specification
        spec.setContent(R.id.tab1);    //add tab view content
        spec.setIndicator("Map");    //put text on tab
        tabs.addTab(spec);             //put tab in TabHost container



        //-------------------------------------------------------------------------------------

        // Initialize a TabSpec for tab2 and add it to the TabHost
        spec=tabs.newTabSpec("tag2");		//create new tab specification
        spec.setContent(R.id.tab2);			//add view tab content
        spec.setIndicator("Web");
        tabs.addTab(spec);					//put tab in TabHost container

        // here I set up a simple webView to use for tab 2

        webView = (WebView)findViewById(R.id.web);

        //intercept URL loading and load in widget
        webView.setWebViewClient(new WebViewClient(){

            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }

        });



        //-------------------------------------------------------------------------------------

        // Initialize a TabSpec for tab3 and add it to the TabHost
        spec=tabs.newTabSpec("tag3");		//create new tab specification
        spec.setContent(R.id.tab3);			//add tab view content
        spec.setIndicator("Pull Down");			//put text on tab
        tabs.addTab(spec); 					//put tab in TabHost container

        // first I set up my Spinner and then I use an ArrayAdapter connecting to the strings.xml values file to get my list of options for the spinner

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // I connect the adapter and set up a listener which basically checks for each option and sends the user to the right url on the second tab which is the WebView
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.pulldown_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Restaurants")){
                    webView.loadUrl("https://www.bonappetit.com/story/nyc100");
                    tabs.setCurrentTab(1);
                } else if(parent.getItemAtPosition(position).equals("Bakeries")){
                    webView.loadUrl("https://www.timeout.com/newyork/restaurants/best-bakeries-in-nyc");
                    tabs.setCurrentTab(1);
                } else if(parent.getItemAtPosition(position).equals("Museums")){
                    webView.loadUrl("https://shershegoes.com/best-museums-in-nyc-guide/");
                    tabs.setCurrentTab(1);
                } else if(parent.getItemAtPosition(position).equals("Attractions")){
                    webView.loadUrl("https://www.nycgo.com/things-to-do/attractions/");
                    tabs.setCurrentTab(1);
                } else if(parent.getItemAtPosition(position).equals("Subway Map")){
                    webView.loadUrl("https://new.mta.info/maps");
                    tabs.setCurrentTab(1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tabs.setCurrentTab(2);
            }
        });
    }

    // here I set up my toasts and redirects for the markers, simply using a listener and checking for each case before sending the user on their way
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, marker.getTitle(), duration);
                toast.show();
                if(marker.getTitle().equals("Fashion Institute of Technology")) {
                    webView.loadUrl("https://www.fitnyc.edu/");
                } else if(marker.getTitle().equals("Manhattan College")) {
                    webView.loadUrl("https://manhattan.edu/");
                } else if(marker.getTitle().equals("Columbia University")) {
                    webView.loadUrl("https://www.columbia.edu/");
                } else if(marker.getTitle().equals("New York University")){
                    webView.loadUrl("https://www.nyu.edu/");
                }
                tabs.setCurrentTab(1);
                return true;
            }
        });
        // here I create my markers using data I got online for locations
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.7469087, -73.9942748))
                .title("Fashion Institute of Technology"));
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.8897709, -73.9019155))
                .title("Manhattan College"));
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.8175378, -73.9568702))
                .title("Columbia University"));
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.7294287, -73.9972178))
                .title("New York University"));
    }
    // here I set up the functionality to press the back key just like in assignment 2
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView WebView = (WebView) findViewById(R.id.web);
        if ((keyCode == KeyEvent.KEYCODE_BACK) && WebView.canGoBack()) {
            WebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}