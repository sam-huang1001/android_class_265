package com.example.user.simpleui;

import android.app.Fragment;
import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {

    TextView note;
    TextView storeInfo;
    TextView menuResults;
    ImageView photo;
    ImageView mapImageView;

    String storeName;
    String address;
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        note = (TextView) findViewById(R.id.note);
        storeInfo = (TextView) findViewById(R.id.storeInfo);
        menuResults = (TextView) findViewById(R.id.menuResults);
        photo = (ImageView) findViewById(R.id.photoImageView);
        mapImageView = (ImageView) findViewById(R.id.mapImageView);

        Intent intent = getIntent();
        note.setText(intent.getStringExtra("note"));
        storeInfo.setText(intent.getStringExtra("storeInfo"));

        String[] storeinfo_arr = intent.getStringExtra("storeInfo").split(", ");
        storeName = storeinfo_arr[0];
        address = storeinfo_arr[1];

        String results = intent.getStringExtra("menuResults");
        String resultsText = "";
        try {
            JSONArray mJsonArray = new JSONArray(results);
            for(int i=0; i<mJsonArray.length(); i++){
                JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                resultsText += mJsonObject.getString("drinkName") + ": 大杯" + mJsonObject.getString("lCupCount") + "杯  中杯" + mJsonObject.getString("mCupCount") + "杯\r\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        menuResults.setText(resultsText);

        String url = intent.getStringExtra("photoURL");
        if(!url.equals("")){
            //Picasso.with(this).load(url).into(photo); //別人寫的工具
            (new ImageLoadingTask(photo)).execute(url);
            //(new GeoCodingTask(photo)).execute("台北市松山區八德路四段803號");
        }


        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.googleMapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) { //真正要的google map
                (new GeoCodingTask(googleMap)).execute(address);
            }
        });

        /*for(int i=0; i<10; i++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        SystemClock.sleep(1000);
                    }
                }
            });
        }*/
    }

    //private static class GeoCodingTask extends  AsyncTask<String, Void, Bitmap>{ //為了吐圖片才回傳Bitmap
    //static class 拿不到外面的變數
    private static class GeoCodingTask extends  AsyncTask<String, Void, double[]>{
        GoogleMap googleMap;
        private ArrayList<Polyline> polylines;

        @Override
        protected double[] doInBackground(String... params) {
            String address = params[0];
            double[] latlng = Utils.addressToLatLng(address);
            return latlng;
        }

        @Override
        protected void onPostExecute(double[] latlng){
            LatLng storeLoc = new LatLng(latlng[0], latlng[1]);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLoc, 17)); //args:經緯度, 放大倍率
            googleMap.addMarker(new MarkerOptions().position(storeLoc)); //標記位置

            LatLng start = new LatLng(25.019449, 121.541404);
            Routing routing = new Routing.Builder() //規劃路徑
                            .travelMode(AbstractRouting.TravelMode.WALKING) //交通方式
                            .waypoints(start, storeLoc) //起點, 目的
                            .withListener(new RoutingListener() {
                                @Override
                                public void onRoutingFailure(RouteException e) {

                                }

                                @Override
                                public void onRoutingStart() {

                                }

                                @Override
                                public void onRoutingSuccess(ArrayList<Route> routes, int index) { //回傳Route檔
                                    if (polylines != null) {
                                        for (Polyline poly : polylines) {
                                            poly.remove();
                                        }
                                    }

                                    polylines = new ArrayList<>();
                                    //add route(s) to the map.
                                    for (int i = 0; i < routes.size(); i++) {

                                        //In case of more than 5 alternative routes

                                        PolylineOptions polyOptions = new PolylineOptions();
                                        polyOptions.color(Color.GREEN); //設定顏色
                                        polyOptions.width(10 + i * 3); //設定寬度
                                        polyOptions.addAll(routes.get(i).getPoints()); //把route的每個點加進去
                                        Polyline polyline = googleMap.addPolyline(polyOptions);
                                        polylines.add(polyline);

//            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ routes.get(i).getDistanceValue()+": duration - "+ routes.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onRoutingCancelled() {

                                }
                            }).build();
            routing.execute();
        }

        public GeoCodingTask(GoogleMap googleMap){ this.googleMap = googleMap; }
    }

    private static class ImageLoadingTask extends AsyncTask<String, Void, Bitmap> {
        ImageView mImageView;
        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            byte[] bytes = Utils.urlToBytes(url);
            if(bytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                return bitmap;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            if(bitmap != null){
                mImageView.setImageBitmap(bitmap);
            }
        }

        //不是static就要有建構子?????
        public ImageLoadingTask(ImageView imageView){
            this.mImageView = imageView;
        }
    }
}
