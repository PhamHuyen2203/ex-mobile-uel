package com.example.k23411tapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adapters.CourseAdapter;
import com.example.models.Course;
import com.example.models.Major;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class MyUelActivity extends AppCompatActivity {

    private static final int VOICE_REQUEST_CODE = 101;
    private static final String BASE_URL = "https://myuel.uel.edu.vn/Default.aspx?ModuleId=f92f39b2-dea3-4185-8cbb-56c1c49c5226";
    
    EditText edtQuery;
    ImageButton btnVoice;
    Spinner spnMajor;
    Button btnSearch;
    ListView lvResults;
    ProgressBar pbLoading;
    
    CourseAdapter adapter;
    ArrayList<Course> displayList = new ArrayList<>();
    ArrayList<Major> majorList = new ArrayList<>();
    ArrayAdapter<Major> majorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_uel);
        
        addViews();
        addEvents();
        loadMajors();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        edtQuery = findViewById(R.id.edtQuery);
        btnVoice = findViewById(R.id.btnVoice);
        spnMajor = findViewById(R.id.spnMajor);
        btnSearch = findViewById(R.id.btnSearchMyUel);
        lvResults = findViewById(R.id.lvMyUelResults);
        pbLoading = findViewById(R.id.pbLoading);

        majorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, majorList);
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMajor.setAdapter(majorAdapter);

        adapter = new CourseAdapter(this, android.R.layout.simple_list_item_2, displayList);
        lvResults.setAdapter(adapter);
    }

    private void addEvents() {
        ImageView imgBack = findViewById(R.id.imgBackMyUel);
        imgBack.setOnClickListener(v -> finish());

        btnVoice.setOnClickListener(v -> startVoiceRecognition());
        
        btnSearch.setOnClickListener(v -> {
            Major selected = (Major) spnMajor.getSelectedItem();
            if (selected == null) {
                Toast.makeText(this, "Vui lòng chọn chương trình đào tạo", Toast.LENGTH_SHORT).show();
                return;
            }
            String query = edtQuery.getText().toString().trim();
            scrapeCourses(selected.getId(), query);
        });
    }

    private void loadMajors() {
        pbLoading.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(BASE_URL).timeout(15000).get();
                // Tìm tất cả option trong select có liên quan đến Ology (Ngành)
                Elements options = doc.select("select[name*=OlogyID] option");
                
                ArrayList<Major> list = new ArrayList<>();
                for (Element opt : options) {
                    String id = opt.val();
                    String name = opt.text().trim();
                    if (!id.isEmpty() && !id.equals("0")) {
                        list.add(new Major(id, name));
                    }
                }

                runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    majorList.clear();
                    majorList.addAll(list);
                    majorAdapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    // Nếu lỗi thì thêm cứng vài ngành để test
                    majorList.add(new Major("406", "Hệ thống thông tin quản lý"));
                    majorList.add(new Major("411E", "Thương mại điện tử"));
                    majorList.add(new Major("416", "Kinh doanh số và trí tuệ nhân tạo"));
                    majorAdapter.notifyDataSetChanged();
                });
            }
        }).start();
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Đang nghe...");
        try {
            startActivityForResult(intent, VOICE_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Voice Recognition not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                edtQuery.setText(result.get(0));
                btnSearch.performClick();
            }
        }
    }

    private void scrapeCourses(String majorId, String query) {
        pbLoading.setVisibility(View.VISIBLE);
        displayList.clear();
        adapter.notifyDataSetChanged();

        String url = BASE_URL + "&OlogyID=" + majorId + "&DepartmentID=05&GraduateLevelID=DH&StudyTypeID=CQ";

        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(url).timeout(15000).get();
                Elements rows = doc.select("tr.gridItem, tr.gridAlternatingItem");
                
                ArrayList<ScoredCourse> scoredList = new ArrayList<>();
                for (Element row : rows) {
                    Elements cols = row.select("td");
                    if (cols.size() >= 5) {
                        String code = cols.get(1).text().trim();
                        String name = cols.get(2).text().trim();
                        String credits = cols.get(3).text().trim();
                        String semester = cols.get(5).text().trim();
                        
                        Course course = new Course(code, name, credits, semester);
                        double score = calculateMatchScore(course, query);
                        
                        if (query.isEmpty() || score > 0) {
                            scoredList.add(new ScoredCourse(course, score));
                        }
                    }
                }

                // Thuật toán: Sắp xếp theo điểm số trùng khớp giảm dần (Logic toán học)
                Collections.sort(scoredList, (a, b) -> Double.compare(b.score, a.score));

                runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    for (ScoredCourse sc : scoredList) {
                        displayList.add(sc.course);
                    }
                    adapter.notifyDataSetChanged();
                    if (displayList.isEmpty()) {
                        Toast.makeText(MyUelActivity.this, "Không tìm thấy môn học nào phù hợp", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    Toast.makeText(MyUelActivity.this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    /**
     * Logic toán học: Thuật toán tính điểm tương đồng (Similarity Score)
     * Kết hợp giữa: Khớp chính xác, Khớp bắt đầu, Khớp chứa và Khoảng cách Levenshtein (Fuzzy)
     */
    private double calculateMatchScore(Course course, String query) {
        if (query.isEmpty()) return 1.0;

        String name = removeAccent(course.getName().toLowerCase());
        String code = removeAccent(course.getCode().toLowerCase());
        String q = removeAccent(query.toLowerCase());

        double score = 0;

        // 1. Khớp chính xác (Ưu tiên cao nhất)
        if (name.equals(q) || code.equals(q)) score += 100;
        
        // 2. Chứa toàn bộ cụm từ
        if (name.contains(q)) score += 50;
        if (code.contains(q)) score += 40;

        // 3. Khớp từng từ (Token matching)
        String[] qWords = q.split("\\s+");
        int matchCount = 0;
        for (String word : qWords) {
            if (name.contains(word)) matchCount++;
        }
        score += (matchCount * 10.0 / qWords.length);

        // 4. Logic mờ (Fuzzy logic) dùng Levenshtein Distance
        // Nếu độ dài chuỗi gần nhau và khoảng cách chỉnh sửa nhỏ thì vẫn tính là khớp
        if (q.length() > 3) {
            int dist = getLevenshteinDistance(name, q);
            if (dist < 3) score += 20;
        }

        return score;
    }

    private int getLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }

    // Class phụ để lưu điểm số cùng kết quả
    private static class ScoredCourse {
        Course course;
        double score;
        ScoredCourse(Course course, double score) {
            this.course = course;
            this.score = score;
        }
    }
}
