package com.example.user.simpleui;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class OrderDetailActivity extends AppCompatActivity {

    TextView note;
    TextView storeInfo;
    TextView menuResults;
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        note = (TextView) findViewById(R.id.note);
        storeInfo = (TextView) findViewById(R.id.storeInfo);
        menuResults = (TextView) findViewById(R.id.menuResults);
        photo = (ImageView) findViewById(R.id.photoImageView);

        Intent intent = getIntent();
        note.setText(intent.getStringExtra("note"));
        storeInfo.setText(intent.getStringExtra("storeInfo"));
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
            //(new ImageLoadingTask(photo)).execute(url);
            (new GeoCodingTask(photo)).execute("台北市松山區八德路四段803號");
        }
    }

    private static class GeoCodingTask extends  AsyncTask<String, Void, Bitmap>{ //為了吐圖片才回傳Bitmap
        ImageView mImageView;

        @Override
        protected Bitmap doInBackground(String... params) {
            String address = params[0];
            double[] latlng = Utils.addressToLatLng(address);

            return Utils.getStaticMap(latlng);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            if(bitmap != null){
                mImageView.setImageBitmap(bitmap);
            }
        }

        public GeoCodingTask(ImageView imageView){
            this.mImageView = imageView;
        }
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
