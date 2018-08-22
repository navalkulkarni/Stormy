package naval.stormy;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import naval.stormy.databinding.ActivityMainBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String Tag = MainActivity.class.getSimpleName();
    private CurrentWeather currentWeather = new CurrentWeather();
    private ImageView iconImageView;

    double latitude = 37.8267;
    double longitude = 122.4233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getForecast(latitude,longitude);
    }

    private void getForecast(double latitude,double longitude) {
        final ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        TextView darkSky = findViewById(R.id.darkSkyAttribution);
        darkSky.setMovementMethod(LinkMovementMethod.getInstance());
        iconImageView = findViewById(R.id.iconImageView);

        String apiKey = "2597d3d374ae7786561e3f204d5fdf50";

        String forecastURL = "https://api.darksky.net/forecast/"+
                apiKey + '/'  + latitude + ',' + '-' + longitude ;

        boolean networkState = isNetworkAvailable();
        if(networkState){
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(forecastURL).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        //synchronous method
                        //response = call.execute();
                        String jsonData= response.body().string();
                        Log.v(Tag,jsonData);
                        if (response.isSuccessful()) {

                            currentWeather = getCurrentDetails(jsonData);

                            final CurrentWeather display = new CurrentWeather(
                                currentWeather.getLocationLabel(),
                                currentWeather.getIcon(),
                                currentWeather.getSummary(),
                                currentWeather.getTimezone(),
                                currentWeather.getTime(),
                                currentWeather.getTemp(),
                                currentWeather.getHumidity(),
                                currentWeather.getPrecipChance()
                          );
                            binding.setWeather(display);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable drawable = getResources().getDrawable(display.getIconId());
                                    iconImageView.setImageDrawable(drawable);
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        // e.printStackTrace();
                        Log.e(Tag, "IO EXCEPTION CAUGHT",e);
                    }catch (JSONException e){
                        Log.e(Tag,"JSON Exception caught",e);
                    }
                }
            });
        }
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {

        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(Tag,"From JSON" + timezone);

        JSONObject currently = forecast.getJSONObject("currently");
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTemp(currently.getDouble("temperature"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setLocationLabel("Alcatrez Island,CA");
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTimezone(timezone);

        Log.d(Tag,currentWeather.getFormattedTime());
        return currentWeather;
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo !=null && networkInfo.isConnected())
            isAvailable=true;
        else
            Toast.makeText(this, R.string.network_unavailable_msg,Toast.LENGTH_LONG).show();
    return isAvailable;
    }


    private void alertUserAboutError() {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.show(getSupportFragmentManager(),"error_dialog");
    }

    public void refreshOnClick(View view){
        Toast.makeText(this,"Refreshing Weather Data",Toast.LENGTH_LONG).show();
        getForecast(latitude,longitude);

    }
}
