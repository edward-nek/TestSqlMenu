package ru.nekrasoved.testsqlmenu;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendFile {

    String space = "";
    int f = 0;

    String str = "";

    SendRequest sendRequest;

    public SendFile() throws InterruptedException {
        Answer inputSite;
        String json = "{menu_site:[{\"id\":1,\"name\":\"\\u041f\\u043e\\u043b\\u044c\\u0437\\u043e\\u0432\\u0430\\u0442\\u0435\\u043b\\u0438\",\"alias\":\"users\",\"childrens\":[{\"id\":2,\"name\":\"\\u0421\\u043e\\u0437\\u0434\\u0430\\u043d\\u0438\\u0435\",\"alias\":\"create\"},{\"id\":3,\"name\":\"\\u0421\\u043f\\u0438\\u0441\\u043e\\u043a\",\"alias\":\"list\",\"childrens\":[{\"id\":4,\"name\":\"\\u0410\\u043a\\u0442\\u0438\\u0432\\u043d\\u044b\\u0435\",\"alias\":\"active\"},{\"id\":5,\"name\":\"\\u0423\\u0434\\u0430\\u043b\\u0435\\u043d\\u043d\\u044b\\u0435\",\"alias\":\"deleted\"}]},{\"id\":8,\"name\":\"\\u041f\\u043e\\u0438\\u0441\\u043a\",\"alias\":\"search\"}]},{\"id\":6,\"name\":\"\\u0417\\u0430\\u044f\\u0432\\u043a\\u0438\",\"alias\":\"requests\",\"childrens\":[{\"id\":9,\"name\":\"\\u0417\\u0430\\u044f\\u0432\\u043a\\u0438 \\u043d\\u0430 \\u043f\\u043e\\u043a\\u043b\\u044e\\u0447\\u0435\\u043d\\u0438\\u0435\",\"alias\":\"connecting\"},{\"id\":10,\"name\":\"\\u0417\\u0430\\u044f\\u0432\\u043a\\u0438 \\u043d\\u0430 \\u0440\\u0435\\u043c\\u043e\\u043d\\u0442\",\"alias\":\"repairs\"},{\"id\":11,\"name\":\"\\u0417\\u0430\\u044f\\u0432\\u043a\\u0438 \\u043d\\u0430 \\u043e\\u0431\\u0445\\u043e\\u0434\",\"alias\":\"round\"}]},{\"id\":7,\"name\":\"\\u041e\\u0442\\u0447\\u0451\\u0442\\u044b\",\"alias\":\"reports\",\"childrens\":[{\"id\":12,\"name\":\"\\u041e\\u0442\\u0434\\u0435\\u043b \\u043c\\u0430\\u0440\\u043a\\u0435\\u0442\\u0438\\u043d\\u0433\\u0430\",\"alias\":\"marketing\",\"childrens\":[{\"id\":15,\"name\":\"\\u041e\\u0442\\u0447\\u0451\\u0442 \\u043f\\u043e \\u0441\\u043f\\u0438\\u0441\\u0430\\u043d\\u0438\\u044f\\u043c\",\"alias\":\"write-offs\"},{\"id\":16,\"name\":\"\\u041e\\u0442\\u0447\\u0451\\u0442 \\u043f\\u043e \\u0440\\u0430\\u0441\\u0445\\u043e\\u0434\\u0430\\u043c\",\"alias\":\"costs\"},{\"id\":17,\"name\":\"\\u0413\\u043e\\u0434\\u043e\\u0432\\u043e\\u0439 \\u043e\\u0442\\u0447\\u0451\\u0442\",\"alias\":\"year\"}]},{\"id\":14,\"name\":\"\\u0423\\u043f\\u0440\\u0430\\u0432\\u043b\\u0435\\u043d\\u0438\\u0435\",\"alias\":\"control\",\"0\":[{\"id\":18,\"name\":\"\\u041e\\u0442\\u0447\\u0451\\u0442 \\u043f\\u043e \\u044d\\u0444\\u0444\\u0435\\u043a\\u0442\\u0438\\u0432\\u043d\\u043e\\u0441\\u0442\\u0438 \\u0440\\u0430\\u0431\\u043e\\u0442\\u044b\",\"alias\":\"efficiency\"},{\"id\":19,\"name\":\"\\u041e\\u0442\\u0447\\u0451\\u0442 \\u043f\\u043e \\u043f\\u043e\\u0434\\u043a\\u043b\\u044e\\u0447\\u0435\\u043d\\u0438\\u044f\\u043c\",\"alias\":\"connecting\"}]}]}]}";

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        inputSite = gson.fromJson(json, Answer.class);

        sendRequest = new SendRequest();

        for (int k = 0; k < inputSite.menu_site.size(); k++) {
            space = "";
            inputSite.menu_site.get(k).level = 0;

            for (int l = 0; l < inputSite.menu_site.get(k).childrens.size(); l++){
                inputSite.menu_site.get(k).childrens.get(l).level = 1;
            }

            pushSite(inputSite.menu_site.get(k));
            printResult(inputSite.menu_site.get(k));
        }

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

            str += space + site.getName() + " " + site.getAlias() + "\n";
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

            str += space + site.getName() + " " + site.getAlias() + "\n";
            space += "  ";
            for (int i = 0; i < site.childrens.size(); i++){
                printResult(site.childrens.get(i));
            }
        }
    }

    public void pushSite(Site site) throws InterruptedException {
        if (site.childrens.isEmpty()){
            str = "id=" + site.getId() + "&&" + "name=" + site.getName() + "&&" + "alias=" + site.getAlias() + "&&" + "parent=" + f;
            Log.d("pushSite", str);

            sendRequest = new SendRequest();
            sendRequest.execute("http://68.183.179.47/create_data.php", str);


        }
        else{
            str = "id=" + site.getId() + "&&" + "name=" + site.getName() + "&&" + "alias=" + site.getAlias() + "&&" + "parent=" + f;
            Log.d("pushSite", str);

            sendRequest = new SendRequest();
            sendRequest.execute("http://68.183.179.47/create_data.php", str);


            for (int i = 0; i < site.childrens.size(); i++){
                f = site.getId();
                pushSite(site.childrens.get(i));
            }
        }
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
}
