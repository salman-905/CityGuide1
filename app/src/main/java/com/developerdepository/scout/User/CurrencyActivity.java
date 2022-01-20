package com.developerdepository.scout.User;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developerdepository.scout.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyActivity extends AppCompatActivity {
    Spinner sp1,sp2;
    EditText ed1 ;
    Button b1;
    TextView result ;
    ImageButton backbtn;
    double cfirst ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String API_KEY ="4f6a6f6ebccbd40d1aab051a";
        //StatusBar Color
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.dashboard_background));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        sp1 = findViewById(R.id.spfrom);
        sp2 = findViewById(R.id.spto);
        ed1 = findViewById(R.id.txtamt);
         result= findViewById(R.id.result1);
        backbtn = findViewById(R.id.back_arrow_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        String[] from = {"SAR","Indian Rupees","USD","EUR"};
        ArrayAdapter ad = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, from);
        sp1.setAdapter(ad);


        String[] to = {"SAR","Indian Rupees","USD","EUR"};
        ArrayAdapter ad1 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, to);
        sp2.setAdapter(ad1);

        b1 = findViewById(R.id.btn1);

        Ion.with(this)
                .load("https://v6.exchangerate-api.com/v6/"+API_KEY+"/latest/"+ sp1.getSelectedItem().toString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null){
                        Toast.makeText(CurrencyActivity.this, "SERVER ERROR", Toast.LENGTH_SHORT).show();
                        // do stuff with the result or error

                            }
                        else
                        {
                            String s22 = sp2.getSelectedItem().toString();
                            JsonObject curancyarr = result.get("conversion_rates").getAsJsonObject();
                             cfirst = curancyarr.getAsJsonObject().get(s22).getAsDouble();

//                            JsonArray curancyarr = result.get("conversion_rates").getAsJsonArray();
//                            cfirst = curancyarr.getAsJsonObject().get(String.valueOf(sp2.getSelectedItem())).getAsDouble();

                        }
                    }
                });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double tot ;

                double amt = Double.parseDouble(ed1.getText().toString());
                DecimalFormat  dU = new  DecimalFormat("0.00");
                NumberFormat nf=NumberFormat.getInstance(new Locale("en","EN"));

                if(sp1.getSelectedItem().toString().equals("SAR") && sp2.getSelectedItem().toString().equals("Indian Rupees"))
                {

                    tot = amt * cfirst  ;
                      String i = "₹";
                    String x = nf.format(tot);
                    result.setText( i + x  );
                }
                else if(sp1.getSelectedItem().toString().equals("SAR") && sp2.getSelectedItem().toString().equals("USD"))
                {
                    tot = amt / cfirst;
                    String i = "$";
                    String x = nf.format(tot);
                    result.setText(i+x);
                }


                else if(sp1.getSelectedItem().toString().equals("SAR") && sp2.getSelectedItem().toString().equals("EUR"))
                {
                    tot = amt * cfirst;
                    String i = "€";
                    String x = nf.format(tot);
                    result.setText( i+x  );
                }



                else if(sp1.getSelectedItem().toString().equals("Indian Rupees") && sp2.getSelectedItem().toString().equals("SAR"))
                {

                    tot = amt /cfirst;
                    String i = "SAR";
                    String x = nf.format(tot);
                    result.setText( i+x  );
                }
                else if(sp1.getSelectedItem().toString().equals("USD") && sp2.getSelectedItem().toString().equals("SAR"))
                {
                    tot = amt * cfirst;
                    String i = "SAR";
                    String x = nf.format(tot);
                    result.setText( i+x  );


                }


                else if(sp1.getSelectedItem().toString().equals("EUR") && sp2.getSelectedItem().toString().equals("SAR"))
                {
                    tot = amt / cfirst;
                    String i = "SAR";
                    String x = nf.format(tot);
                    result.setText( i + x  );
                }
                else if (sp1.getSelectedItem().toString().equals("SAR") && sp2.getSelectedItem().toString().equals("SAR"))
                {
                    Toast.makeText(CurrencyActivity.this, "You can not choose same currency  ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(CurrencyActivity.this, "You have to chose from SAR to other currency or opposite ", Toast.LENGTH_SHORT).show();
                }



            }
        });



    }
}