package ru.nekrasoved.testsqlmenu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static long back_pressed;

    String json;
    Answer answer;
    int count;
    String space = "";

    TextView textView;

    String textResultA = "type_a.txt \n\n";
    String textResultB = "type_b.txt \n\n";

    boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Структура сайта");

        SendRequest sendRequest = new SendRequest();
        sendRequest.execute("http://68.183.179.47/get_all_data_menu.php");

        textView = (TextView) findViewById(R.id.textView);

        Button btAdd = (Button)findViewById(R.id.bt_Add);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddData.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        final Button btSql = (Button)findViewById(R.id.btSql);
        btSql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input data
                try {
                    SendFile sendFile = new SendFile();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //end input data
                Toast.makeText(getBaseContext(), "Данные отправлены на сервер!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        final Switch switch_type = (Switch) findViewById(R.id.switch_type);
        switch_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check){
                    textView.setText(textResultB);
                    check = !check;
                }
                else{
                    textView.setText(textResultA);
                    check = !check;
                }
            }
        });


    }


    private class SendRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String[] params)
        {
            String responseFromServer = null;

            try
            {
                String url = params[0];
                int i = 0;
                while(i < params.length){
                    Log.d("Logm", i + " = " + params[i]);
                    i++;
                }

                URL obj = new URL(url);

                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("POST");

                con.setRequestProperty("Accept-Language", "en-US,en,q=0.5");

                String urlParameters = "";

                //запись информации в поток запроса

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));

                String[] check = new String[4];

                writer.write(urlParameters);

                writer.close();

                wr.close();

                //получение информации из потока ответа

                int responseCode = con.getResponseCode();

                Log.d("Logm", "ResponseCode " + responseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;

                StringBuffer response = new StringBuffer();

                while ((inputLine = reader.readLine()) != null)
                {
                    response.append(inputLine);
                    Log.d("Logm", "inputLine  = " + inputLine);
                }

                reader.close();

                responseFromServer = response.toString();

            }
            catch (Exception ex)
            {
                return ex.getMessage();
            }

            return responseFromServer;
        }

        @Override
        protected void onPostExecute(String message)
        {
//            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            json = message;

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            answer = gson.fromJson(json, Answer.class);
            int i = 0;
            if (answer.menu_site != null){
                while (i < answer.menu_site.size()){
                    Log.i("GSON", answer.menu_site.get(i).getString());
                    i ++;
                }
                resultA();
                resultB();
                textView.setText(textResultA);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Повторите, для выхода из Приложения!",
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    public void printResult(Site site) {
        if (site.childrens.isEmpty()){

            for (int l = 0; l < site.childrens.size(); l++){
                site.childrens.get(l).level = site.level+1;
            }
            space = "";
            for (int l = 0; l < site.level; l++){
                space += "  ";
            }

            Log.d("printResultSend", space + site.getName() + " " + site.getAlias());

            textResultA += space + site.getName() + " " + site.getAlias() + "\n";
        }
        else{

            for (int l = 0; l < site.childrens.size(); l++){
                site.childrens.get(l).level = site.level+1;
            }
            space = "";
            for (int l = 0; l < site.level; l++){
                space += "  ";
            }

            Log.d("printResultSend", space + site.getName() + " " + site.getAlias());

            textResultA += space + site.getName() + " " + site.getAlias() + "\n";
            space += "  ";
            for (int i = 0; i < site.childrens.size(); i++){
                printResult(site.childrens.get(i));
            }
        }
    }

    public void getRes(Site parent, Site child, int id) {
        if (parent.getId() == id){
            parent.childrens.add(child);
        }
        else{
            for (int i = 0; i < parent.childrens.size(); i++){
                getRes(parent.childrens.get(i), child, id);
            }
        }
    }

    public void resultA() {
        int i = 0;
        while (i < answer.menu_site.size())
        {
            if (answer.menu_site.get(i).getParent() > 0)
            {
                for (int j = 0; j < answer.menu_site.size(); j++){
                    if (j != i){
                        getRes(answer.menu_site.get(j), answer.menu_site.get(i), answer.menu_site.get(i).getParent());
                    }
                }
                answer.menu_site.remove(i);
            }
            else{
                i++;
            }
        }

        for (int k = 0; k < answer.menu_site.size(); k++) {
            count = 0;
            space = "";

            answer.menu_site.get(k).level = 0;

            for (int l = 0; l < answer.menu_site.get(k).childrens.size(); l++){
                answer.menu_site.get(k).childrens.get(l).level = 1;
            }

            printResult(answer.menu_site.get(k));
        }
    }

   public void resultB() {
        int i  = 0;
        while (i < answer.menu_site.size()){
            textResultB += answer.menu_site.get(i).getName() + "\n";
            for (int j = 0; j < answer.menu_site.get(i).childrens.size(); j++){
                textResultB += "  " + answer.menu_site.get(i).childrens.get(j).getName() + "\n";
            }
            i++;
        }
   }
}
