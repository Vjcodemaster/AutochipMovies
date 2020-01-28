package app_utility;

import android.content.Context;
import android.os.AsyncTask;

import com.ac.autochipmovies.MainActivity;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.OdooUser;
import oogbox.api.odoo.client.AuthError;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.AuthenticateListener;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

import static app_utility.StaticReferenceClass.DB_NAME;
import static app_utility.StaticReferenceClass.PASSWORD;
import static app_utility.StaticReferenceClass.USER_ID;
import static app_utility.StaticReferenceClass.sURL;

public class MoviesAsyncTask extends AsyncTask<String, Void, String> {

    private int type;
    private Context context;
    private String res = "";

    OdooClient client;
    AuthenticateListener loginCallback;

    public MoviesAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        //setProgressBar();
    }

    @Override
    protected String doInBackground(String... params) {
        type = Integer.parseInt(params[0]);
        switch (type) {
            case 1:
                readAllMovies();
                break;
            case 2:
                //readDeliveryNumberViaLibrary();
                break;
        }
        return res;
    }

    private void readAllMovies(){
        client = new OdooClient.Builder(context)
                .setHost(sURL)
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        client.authenticate(USER_ID, PASSWORD, DB_NAME, loginCallback);
                    }
                }).build();

        loginCallback = new AuthenticateListener() {
            @Override
            public void onLoginSuccess(OdooUser user) {
               /* ODomain domain = new ODomain();
                domain.add("state", "=", "confirmed");
                domain.add("delivery_name", "=", "Deliveryv Orders");*/

                OdooFields fields = new OdooFields();
                fields.addAll("name");

                int offset = 0;
                int limit = 80;
                String sorting = "";

                client.searchRead("autochip.movies", null, null, offset, limit, sorting, new IOdooResponse() {
                    @Override
                    public void onResult(OdooResult result) {
                        //HashMap<String, MovieData> hmMovies = new HashMap<>();
                        ArrayList<MovieData> alMovieData = new ArrayList<>();
                        /*ArrayList<String> alMovieName = new ArrayList<>();
                        ArrayList<String> alMovieLink = new ArrayList<>();
                        ArrayList<String> alDescription = new ArrayList<>();
                        ArrayList<String> alLanguage = new ArrayList<>();*/
                        OdooRecord[] records = result.getRecords();
                        for (OdooRecord record : records) {
                            alMovieData.add(new MovieData(record.getString("movie_name"), record.getString("movie_language"),
                                    record.getString("video_link"), record.getString("description"),
                                    record.getString("movie_image")));
                            /*MovieData movieData = new MovieData(record.getString("movie_name"), record.getString("language"),
                                    record.getString("video_link"), record.getString("description"),
                                    record.getString("photo"));
                            hmMovies.put(record.getString("movie_name"), movieData);*/
                            /*alMovieName.add(record.getString("movie_name"));
                            alMovieLink.add(record.getString("video_link"));
                            String encodedBitmap = record.getString("movie_image");*/
                            //alDeliveryOrderNumber.add(record.get("name").toString());
                            //Log.e(">>", record.getString("name"));
                        }
                        MainActivity.onAsyncTaskInterfaceListener.onResult("MOVIE", alMovieData);
                        /*OdooRecord[] records = result.getRecords();
                        for (OdooRecord record : records) {
                            alDeliveryOrderNumber.add(record.get("name").toString());
                            //Log.e(">>", record.getString("name"));
                        }
                        MainActivity.onServiceInterface.onServiceCall("SUCCESS", 0,"", "", null, alDeliveryOrderNumber);

                        if (circularProgressBar != null && circularProgressBar.isShowing()) {
                            circularProgressBar.dismiss();
                        }*/
                    }
                });

            }

            @Override
            public void onLoginFail(AuthError error) {
                String s = "failed";
                //s = s + error.hashCode();
                /*if (circularProgressBar != null && circularProgressBar.isShowing()) {
                    circularProgressBar.dismiss();
                }*/
            }
        };
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
