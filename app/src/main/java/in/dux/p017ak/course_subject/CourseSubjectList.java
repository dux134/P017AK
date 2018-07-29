package in.dux.p017ak.course_subject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import in.dux.p017ak.R;

import static android.support.constraint.Constraints.TAG;

public class CourseSubjectList extends AppCompatActivity {
    private ListView listView;
    private ArrayList<CourseSubjectListDataModel> stringArrayList = new ArrayList<>();
    private ArrayAdapter<CourseSubjectListDataModel> adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    public static String collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_subject_list);

        progressDialog = new ProgressDialog(CourseSubjectList.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.courseSubjecttoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = (ListView) findViewById(R.id.courseSubjectListView);

        setData();
        adapter = new CourseSubjectListViewAdapter(this, R.layout.recycler_item_course_subject_list, stringArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CourseSubject.object = stringArrayList.get(i);
                startActivity(new Intent(CourseSubjectList.this,CourseSubject.class));
//                Toast.makeText(getApplicationContext(),"hi "+stringArrayList.get(i),Toast.LENGTH_LONG).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        },500);
    }

    private void setData() {
        stringArrayList.clear();
        db.collection(collection).whereLessThan("priority","5").orderBy("priority")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen error", e);
                            return;
                        }
                        assert querySnapshot != null;
                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {

                                String title = change.getDocument().get("title").toString();
                                 String subject_Title = change.getDocument().get("subject_title").toString();
                                String icon = change.getDocument().get("icon").toString();
                                String image = change.getDocument().get("image_url").toString();
                                String syllabusUrl = change.getDocument().get("syllabus_url").toString();
                                stringArrayList.add(new CourseSubjectListDataModel(title,subject_Title,icon,image,syllabusUrl));
                                adapter.notifyDataSetChanged();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
