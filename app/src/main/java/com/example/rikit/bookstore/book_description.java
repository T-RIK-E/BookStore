package com.example.rikit.bookstore;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class book_description extends AppCompatActivity {
    public static final String SENT_BOOK = "book";
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        ImageButton imageButton = (ImageButton)toolbar.findViewById(R.id.detailCart);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartClick();
            }
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        book = (Book) getIntent().getParcelableExtra(SENT_BOOK);
        setScreen(book);
        final TextView price = (TextView) findViewById(R.id.price);
        //https://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
        final Spinner spinner = (Spinner)findViewById(R.id.coverSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItem().equals("HardCover")) {
                    String temp = "$" + book.getPriceHard();
                    price.setText(temp);
                }
                else {
                    String temp = "$" + book.getPriceSoft();
                    price.setText(temp);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void setScreen(Book book) {
        ImageView img = (ImageView) findViewById(R.id.imageView);
        img.setImageResource(book.getImgID());

        TextView title = (TextView) findViewById(R.id.bookTitle);
        title.setText(book.getTitle());
        TextView author = (TextView) findViewById(R.id.author);
        author.setText(book.getAuthor());
        TextView publishYear = (TextView)findViewById(R.id.publishYear);
        String concatString = "Published Year:" + book.getPublishYear();
        publishYear.setText(concatString);
        TextView description = (TextView)findViewById(R.id.description);
        description.setText(book.getDescription());
        TextView price = (TextView)findViewById(R.id.price);
        concatString = "$" + book.getPriceHard();
        price.setText(concatString);
    }

    public void onClickBook(View v){
        if(!book.isCheckedOut()) {
            int num = 0;
            int index = 0;
            for (int i = 0; i < MainActivity.bookList.size(); i++) {
                if (MainActivity.bookList.get(i).getImgID() == book.getImgID()) {
                    MainActivity.bookList.get(i).setCheckedOut(true);
                    index = i;
                }
                if(MainActivity.bookList.get(i).isCheckedOut()){
                    num++;
                }
            }
            Spinner spinner = (Spinner)findViewById(R.id.coverSpinner);
            if(String.valueOf(spinner.getSelectedItem()).equals("HardCover")){
                MainActivity.bookList.get(index).setHardCover(true);
            }
            else {
                MainActivity.bookList.get(index).setHardCover(false);
            }

            book.setCheckedOut(true);
            Toast.makeText(this, "The Book has been added to the list", Toast.LENGTH_SHORT).show();
            android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarDetail);


            TextView cartNum = (TextView)toolbar.findViewById(R.id.detailNum);
            cartNum.setVisibility(View.VISIBLE);
            cartNum.setText(Integer.toString(num));
            //https://stackoverflow.com/questions/3500197/how-to-display-toast-in-android
        }
        else {
            Toast.makeText(this, "Sorry, you already checked this book out.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onStart(){
        super.onStart();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarDetail);
        TextView cartNum = (TextView)toolbar.findViewById(R.id.detailNum);
        int checkedOut = 0;
        for(int i = 0; i < MainActivity.bookList.size(); i++) {
            if(MainActivity.bookList.get(i).isCheckedOut()){
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

    public void onCartClick(){
        Intent intent = new Intent(this, CheckOut.class);
        startActivity(intent);
    }

    public void sendToPage(View view){
        //https://www.concretepage.com/android/android-implicit-intent-example-open-url-in-browser-make-phone-call
        Intent implicit = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getUrl()));
        startActivity(implicit);
    }
}
