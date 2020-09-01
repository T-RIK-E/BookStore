package com.example.rikit.bookstore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Book> bookList = new ArrayList<Book>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://guides.codepath.com/android/using-the-app-toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarmain);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageButton imageButton = (ImageButton)toolbar.findViewById(R.id.cartButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartClick();
            }
        });


        readJSON(loadJSON());

        ViewGroup ll = (ViewGroup)findViewById(R.id.linlayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        int pxtopMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        layoutParams.setMargins(0, pxtopMargin, 0, 0);
        LinearLayout linearLayout = null;
        for(int i = 0; i < bookList.size(); i++) {
            if(i%3==0){
                linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                ll.addView(linearLayout);
                addLayout(linearLayout,i);
            }
            else{
                addLayoutwithMargin(linearLayout,i);
            }
        }

    }

    protected void onStart(){
        super.onStart();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarmain);
        TextView cartNum = (TextView)toolbar.findViewById(R.id.cartNum);
        int checkedOut = 0;
        for(int i = 0; i < bookList.size(); i++) {
            if(bookList.get(i).isCheckedOut()){
                checkedOut++;
            }
        }
        if(checkedOut>0){
            cartNum.setVisibility(View.VISIBLE);
            cartNum.setText(Integer.toString(checkedOut));
        }
        else{
            cartNum.setVisibility(View.INVISIBLE);
        }
    }

    private void addLayout(ViewGroup viewGroup, int index){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.custom_layout,null);
        viewGroup.addView(ll);

        ImageButton img = (ImageButton)ll.getChildAt(0);
        TextView title = (TextView)ll.getChildAt(1);
        title.setText(bookList.get(index).getTitle());
        TextView author = (TextView) ll.getChildAt(2);
        author.setText(bookList.get(index).getAuthor());
        img.setTag(bookList.get(index).getImgID());
        //setting img resource: https://stackoverflow.com/questions/5089300/how-can-i-change-the-image-of-an-imageview
        img.setImageResource(bookList.get(index).getImgID());

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton img = (ImageButton)v;
                onBookClick(img);
            }
        });

    }
    private void addLayoutwithMargin(ViewGroup viewGroup, int index){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.custom_layout,null);
        int pxRightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams textViewLayoutParams =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLayoutParams.setMargins(0, 0, pxRightMargin, 0);
        ll.setLayoutParams(textViewLayoutParams);
        viewGroup.addView(ll);

        ImageButton img = (ImageButton)ll.getChildAt(0);
        TextView title = (TextView)ll.getChildAt(1);
        title.setText(bookList.get(index).getTitle());
        TextView author = (TextView) ll.getChildAt(2);
        author.setText(bookList.get(index).getAuthor());
        //setting img resource: https://stackoverflow.com/questions/5089300/how-can-i-change-the-image-of-an-imageview
        img.setImageResource(bookList.get(index).getImgID());
        img.setTag(bookList.get(index).getImgID());

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton img = (ImageButton)v;
                onBookClick(img);
            }
        });

    }

    //https://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
    public String loadJSON(){
        String json = "";
        try{
            InputStream is = getAssets().open("book_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return json;
    }

    //https://www.tutorialspoint.com/android/android_json_parser.htm
    public void readJSON(String file){
        String author, title, img, description, url;
        double priceSoft, priceHard;
        int publishYear;
        try {
            JSONObject reader = new JSONObject(file);

            for(int i = 0; i < 15;i++) {
                JSONObject book = reader.getJSONObject("book" + i);
                author = book.getString("author");
                title = book.getString("title");
                img = book.getString("imgName");
                //https://stackoverflow.com/questions/8642823/using-setimagedrawable-dynamically-to-set-image-in-an-imageview
                int imgID = getResources().getIdentifier("com.example.rikit.bookstore:drawable/" + img, null, null);
                publishYear = book.getInt("publishYear");
                description = book.getString("description");
                priceSoft = book.getDouble("priceSoft");
                priceHard = book.getDouble("priceHard");
                url = book.getString("url");

                bookList.add(new Book(title, author, imgID, publishYear, description, priceSoft, priceHard, url));
            }
        }
        catch (Exception e){

        }
    }

    //https://stackoverflow.com/questions/13032333/droid-how-to-get-button-id-from-onclick-method-described-in-xml
    public void onBookClick(ImageButton img){
        Intent intent = new Intent(this, book_description.class);
        for(int i = 0; i< bookList.size();i++){
            //https://stackoverflow.com/questions/26370993/how-to-get-image-resource
            Integer id = (Integer)img.getTag();
            if(id == bookList.get(i).getImgID()){
                intent.putExtra(book_description.SENT_BOOK, bookList.get(i));
            }
        }
        startActivity(intent);
    }

    public void onCartClick(){
        Intent intent = new Intent(this, CheckOut.class);
        startActivity(intent);
    }
}
