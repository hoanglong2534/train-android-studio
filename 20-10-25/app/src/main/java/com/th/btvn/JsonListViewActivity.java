package com.th.btvn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonListViewActivity extends AppCompatActivity {

    private ListView userListView;
    private ProgressBar progressBar;
    private TextView statusText;
    private Button loadButton;
    private List<User> userList;
    private ArrayAdapter<String> adapter;

    private static final String JSON_URL = "https://jsonplaceholder.typicode.com/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_json_list_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        userListView = findViewById(R.id.userListView);
        progressBar = findViewById(R.id.progressBar);
        statusText = findViewById(R.id.statusText);
        loadButton = findViewById(R.id.loadButton);

        userList = new ArrayList<>();

        // Set up button click listener
        loadButton.setOnClickListener(v -> {
            loadUsersFromJson();
        });

        // Load data automatically on start
        loadUsersFromJson();
    }

    private void loadUsersFromJson() {
        new JsonTask().execute(JSON_URL);
    }

    private class JsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            statusText.setText("Đang tải dữ liệu từ internet...");
            loadButton.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            loadButton.setEnabled(true);

            if (result.startsWith("Unable to retrieve")) {
                statusText.setText("❌ " + result);
                Toast.makeText(JsonListViewActivity.this, "Lỗi kết nối internet!", Toast.LENGTH_LONG).show();
            } else {
                parseJsonAndUpdateUI(result);
            }
        }
    }

    private String downloadUrl(String urlString) throws IOException {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        String result = "";        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            connection.connect();

            inputStream = connection.getInputStream();
            result = readStream(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    private String readStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private void parseJsonAndUpdateUI(String jsonString) {
        try {
            userList.clear();
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);
                
                User user = new User();
                user.setId(userObject.getInt("id"));
                user.setName(userObject.getString("name"));
                user.setUsername(userObject.getString("username"));
                user.setEmail(userObject.getString("email"));
                user.setPhone(userObject.getString("phone"));
                user.setWebsite(userObject.getString("website"));

                // Parse address
                if (userObject.has("address")) {
                    JSONObject addressObject = userObject.getJSONObject("address");
                    User.Address address = new User.Address();
                    address.setStreet(addressObject.getString("street"));
                    address.setSuite(addressObject.getString("suite"));
                    address.setCity(addressObject.getString("city"));
                    address.setZipcode(addressObject.getString("zipcode"));
                    user.setAddress(address);
                }

                // Parse company
                if (userObject.has("company")) {
                    JSONObject companyObject = userObject.getJSONObject("company");
                    User.Company company = new User.Company();
                    company.setName(companyObject.getString("name"));
                    company.setCatchPhrase(companyObject.getString("catchPhrase"));
                    company.setBs(companyObject.getString("bs"));
                    user.setCompany(company);
                }

                userList.add(user);
            }

            // Update UI
            updateListView();
            statusText.setText("✅ Đã tải thành công " + userList.size() + " người dùng");

        } catch (JSONException e) {
            statusText.setText("❌ Lỗi phân tích dữ liệu JSON");
            Toast.makeText(this, "Lỗi phân tích JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateListView() {
        List<String> userStrings = new ArrayList<>();
        
        for (User user : userList) {
            StringBuilder userInfo = new StringBuilder();
            userInfo.append("👤 ").append(user.getName()).append("\n");
            userInfo.append("📧 ").append(user.getEmail()).append("\n");
            userInfo.append("📱 ").append(user.getPhone()).append("\n");
            
            if (user.getAddress() != null) {
                userInfo.append("🏠 ").append(user.getAddress().getCity()).append("\n");
            }
            
            if (user.getCompany() != null) {
                userInfo.append("🏢 ").append(user.getCompany().getName());
            }
            
            userStrings.add(userInfo.toString());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userStrings);
        userListView.setAdapter(adapter);

        // Set item click listener
        userListView.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = userList.get(position);
            showUserDetails(selectedUser);
        });
    }

    private void showUserDetails(User user) {
        StringBuilder details = new StringBuilder();
        details.append("Tên: ").append(user.getName()).append("\n");
        details.append("Username: ").append(user.getUsername()).append("\n");
        details.append("Email: ").append(user.getEmail()).append("\n");
        details.append("Điện thoại: ").append(user.getPhone()).append("\n");
        details.append("Website: ").append(user.getWebsite()).append("\n");
        
        if (user.getAddress() != null) {
            details.append("Địa chỉ: ").append(user.getAddress().getFullAddress()).append("\n");
        }
        
        if (user.getCompany() != null) {
            details.append("Công ty: ").append(user.getCompany().getName()).append("\n");
            details.append("Slogan: ").append(user.getCompany().getCatchPhrase());
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Chi tiết người dùng #" + user.getId())
                .setMessage(details.toString())
                .setPositiveButton("OK", null)
                .show();
    }
}
