package ru.nekrasoved.testsqlmenu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static android.view.View.GONE;


public class AddData extends AppCompatActivity {

    EditText etID;
    EditText etName;
    EditText etAlias;
    EditText etParent;

    Button btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Добавление страницы");
        actionBar.setDisplayHomeAsUpEnabled(true);

        etID = (EditText) findViewById(R.id.et_ID);
        etName = (EditText) findViewById(R.id.et_Name);
        etAlias = (EditText) findViewById(R.id.et_Alias);
        etParent = (EditText) findViewById(R.id.et_Parent);

        btSave = (Button) findViewById(R.id.bt_Save);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendRequest sendRequest = new SendRequest();
                Site dataSite = null;

                if ((etID.getText().toString().length() > 0) &&
                        (etName.getText().toString().length() > 0) &&
                        (etAlias.getText().toString().length() > 0))
                {

                        if ((etParent.getText().toString().length()) > 0)

                        {
                            if ((Integer.valueOf(etParent.getText().toString()) > 0)&&
                                    (Integer.valueOf(etID.getText().toString()) > 0))
                            {
                                dataSite = new Site(Integer.valueOf(etID.getText().toString()),
                                        etName.getText().toString(), etAlias.getText().toString(),
                                        Integer.valueOf(etParent.getText().toString()));
                            }
                            else{
                                Toast.makeText(AddData.this, "ID и Parent должны быть больше 0", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            if (Integer.valueOf(etID.getText().toString()) > 0) {
                                dataSite = new Site(Integer.valueOf(etID.getText().toString()),
                                        etName.getText().toString(), etAlias.getText().toString(), 0);
                            }
                            else{
                                Toast.makeText(AddData.this, "ID должен быть больше 0", Toast.LENGTH_LONG).show();
                            }
                        }

                    sendRequest.execute("http://68.183.179.47/create_data.php", dataSite.getString());

                    Intent intent = new Intent(AddData.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();

                }
                else
                {
                    Toast.makeText(AddData.this, "Для начала заполните поля", Toast.LENGTH_LONG).show();
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

                String urlParameters = params[1];

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
//            Toast.makeText(AddData.this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddData.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(AddData.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        return true;
    }

}