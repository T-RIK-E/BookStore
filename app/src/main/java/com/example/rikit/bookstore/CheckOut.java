package com.example.rikit.bookstore;
//https://stackoverflow.com/questions/11275650/how-to-increase-heap-size-of-an-android-application

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CheckOut extends AppCompatActivity {
    ArrayList<Book> bookList = MainActivity.bookList;
    private double totalPrice = 0;
    private int totalBooks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarCost);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void onStart(){
        super.onStart();
        ViewGroup ll = (ViewGroup)findViewById(R.id.checkOutLinLay);
        for(int i = 0; i < bookList.size(); i++)
        {
            if(bookList.get(i).isCheckedOut()) {
                addLayout(ll, i);
            }
        }
        addTotal(ll);
    }

    private void addLayout(ViewGroup viewGroup, int index){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.checkout_layout,null);
        viewGroup.addView(ll);

        ImageView img = (ImageView) ll.getChildAt(0);
        img.setImageResource(bookList.get(index).getImgID());

        LinearLayout ll2 = (LinearLayout) ll.getChildAt(1);
        TextView bookTitle = (TextView) ll2.getChildAt(0);
        bookTitle.setText(bookList.get(index).getTitle());
        TextView bookAuthor = (TextView) ll2.getChildAt(1);
        bookAuthor.setText(bookList.get(index).getAuthor());

        LinearLayout ll3 = (LinearLayout) ll2.getChildAt(2);
        final TextView bookPrice = (TextView) ll3.getChildAt(0);
        double price = 0;
        if(bookList.get(index).isHardCover()) {
            price = bookList.get(index).getPriceHard();
            String temp = "$" + Double.toString(price);
            bookPrice.setText(temp);
        }
        else{
            price = bookList.get(index).getPriceSoft();
            String temp = "$" + Double.toString(price);
            bookPrice.setText(temp);
        }
        ImageButton up = (ImageButton)ll3.getChildAt(3);
        up.setTag(price);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAdd((ImageButton) v);
            }
        });
        ImageButton down = (ImageButton) ll3.getChildAt(1);
        down.setTag(price);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubtract((ImageButton) v);
            }
        });
        TextView quantity = (TextView) ll3.getChildAt(2);
        quantity.setTag(1);
        quantity.setText(Integer.toString(1));

    }

    private void addTotal(ViewGroup viewGroup){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.checkout_total_display,null);
        viewGroup.addView(ll);

        TextView itemNum = (TextView)findViewById(R.id.itemNum);
        TextView price = (TextView)findViewById(R.id.price);

        DecimalFormat df = new DecimalFormat("$#.00");
        for(int i = 0; i < bookList.size(); i++)
        {
            if(bookList.get(i).isCheckedOut()) {
                totalBooks++;
                if(bookList.get(i).isHardCover()) {
                    totalPrice += bookList.get(i).getPriceHard();
                }
                else {
                    totalPrice += bookList.get(i).getPriceSoft();
                }
            }
        }
        String temp = Integer.toString(totalBooks) + " item(s)";
        itemNum.setText(temp);
        temp =  df.format(totalPrice);
        price.setText(temp);
    }

    public void onProceed(View v){
        Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
        for(int i = 0; i < bookList.size(); i++)
        {
            bookList.get(i).setCheckedOut(false);
        }
    }

    public void onClickAdd(ImageButton view){
        //https://stackoverflow.com/questions/17879743/get-parents-view-from-a-layout
        LinearLayout ll = (LinearLayout) view.getParent();
        TextView quantity = (TextView)ll.getChildAt(2);
        Integer tag = (Integer)quantity.getTag();
        quantity.setTag(tag+1);
        quantity.setText(Integer.toString(tag+1));
        totalBooks++;
        totalPrice += (Double) view.getTag();
        TextView totalPrices = (TextView)findViewById(R.id.price);
        DecimalFormat df = new DecimalFormat("$#.00");
        String temp = df.format(totalPrice);
        totalPrices.setText(temp);
        TextView totalBook = (TextView)findViewById(R.id.itemNum);
        temp = Integer.toString(totalBooks) + " Item(s)";
        totalBook.setText(temp);
    }

    public void onClickSubtract(ImageButton view){
        LinearLayout ll = (LinearLayout) view.getParent();
        TextView quantity = (TextView)ll.getChildAt(2);
        Integer tag = (Integer)quantity.getTag();
        if(tag >= 0) {
            quantity.setTag(tag-1);
            quantity.setText(Integer.toString(tag-1));
            totalBooks--;
            totalPrice -= (Double) view.getTag();
            TextView totalPrices = (TextView)findViewById(R.id.price);
            DecimalFormat df = new DecimalFormat("$#.00");
            String temp = df.format(totalPrice);
            totalPrices.setText(temp);
            TextView totalBook = (TextView)findViewById(R.id.itemNum);
            temp = Integer.toString(totalBooks) + " Item(s)";
            totalBook.setText(temp);
        }
        else{
            Toast.makeText(this, "Already Zero", Toast.LENGTH_SHORT).show();
        }
    }
}
