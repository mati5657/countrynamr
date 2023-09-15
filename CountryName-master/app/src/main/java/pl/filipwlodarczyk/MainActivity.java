package pl.filipwlodarczyk;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText textView;
    Button button;
    TextView displayCountry;

    String searchedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.name);
        button = findViewById(R.id.button);
        displayCountry = findViewById(R.id.displayCountry);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchedName = textView.getText().toString();
                String url = "https://api.nationalize.io/?name=" + searchedName;

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                String countryId = "";
                                double maxProb = 0;
                                try {
                                    for (int i = 0; i < response.getJSONArray("country").length(); i++) {
                                        JSONObject country = response.getJSONArray("country").getJSONObject(i);
                                        if (country.getDouble("probability") > maxProb) {
                                            maxProb = country.getDouble("probability");
                                            countryId = country.getString("country_id");
                                        }
                                    }
                                    displayCountry.setText("Your name is used mostly in " + countryId);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", error.toString());

                            }
                        });

                queue.add(jsonObjectRequest);
            }
        });
    }
}