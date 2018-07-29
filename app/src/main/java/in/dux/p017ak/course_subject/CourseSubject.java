package in.dux.p017ak.course_subject;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import in.dux.p017ak.R;
import in.dux.p017ak.subject.SubjectDataModel;
import in.dux.p017ak.subject.SubjectMyAdapter;
import in.dux.p017ak.utils.MyActivity;

import static android.support.constraint.Constraints.TAG;
import static in.dux.p017ak.utils.CheckNetworkConnection.isConnectionAvailable;

public class CourseSubject extends MyActivity {
    private TextView previousYearAvailable,bookAvailable,pdfAvailable,videoAvailabe;
    private ImageView toolbarImage;
    private ProgressDialog progressDialog;
    public static CourseSubjectListDataModel object;
    private RecyclerView previousYearRecyclerView,pdfRecyclerView,videoRecyclerView,bookRecyclerView;
    private ArrayList<PreviousYearDataModel> previousYearList = new ArrayList<>();
    private ArrayList<SubjectDataModel> pdfList = new ArrayList<>();
    private ArrayList<SubjectDataModel> videoList = new ArrayList<>();
    private ArrayList<SubjectDataModel> bookList = new ArrayList<>();
    private RecyclerView.Adapter previousYearAdapter,pdfAdapter,videosAdapter,bookAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_subject);

        progressDialog = new ProgressDialog(CourseSubject.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        loadAdvertisement();
        toolbarImage = findViewById(R.id.courseImage);
        Glide.with(getApplicationContext())
                .load(object.getImage())
                .into(toolbarImage);
        toolbarImage.setScaleType(ImageView.ScaleType.FIT_XY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.courseToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.course_collapsing);
        collapsingToolbar.setTitle(object.getTitle());
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        loadPreviousYearData();
        loadPdfsList();
        loadVideosList();
        loadBookList();

        pdfAvailable = findViewById(R.id.course_pdf_available);
        bookAvailable = findViewById(R.id.course_books_available);
        previousYearAvailable = findViewById(R.id.couse_previous_year_available);
        videoAvailabe = findViewById(R.id.course_video_available);

        previousYearRecyclerView = findViewById(R.id.previous_year_recyclerView);
        previousYearRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        previousYearAdapter = new PreviousYearMyAdapter(previousYearList, new PreviousYearMyAdapter.RecyclerItemListener() {
            @Override
            public void onClick(View view, int adapterPosition) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(previousYearList.get(adapterPosition).getLink()), "application/pdf");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.d("l", "l");
                }
                showMyAdd(intent);
            }
        });
        previousYearRecyclerView.setAdapter(previousYearAdapter);
        previousYearRecyclerView.setItemAnimator(new DefaultItemAnimator());

        pdfRecyclerView = findViewById(R.id.course_subject_pdf_recycler);
        pdfRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        pdfAdapter = new SubjectMyAdapter(pdfList, this, new SubjectMyAdapter.RecyclerClickListener() {
            @Override
            public void onClick(View view, int adapterPosition) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(pdfList.get(adapterPosition).getLink()), "application/pdf");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.d("l", "l");
                }
                showMyAdd(intent);
            }
        });
        pdfRecyclerView.setAdapter(pdfAdapter);
        pdfRecyclerView.setItemAnimator(new DefaultItemAnimator());

        videoRecyclerView = findViewById(R.id.course_subject_videos_recycler);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        videosAdapter = new SubjectMyAdapter(videoList, this, new SubjectMyAdapter.RecyclerClickListener() {
            @Override
            public void onClick(View view, int adapterPosition) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(videoList.get(adapterPosition).getLink()));
                startActivity(i);
                showMyAdd(i);
            }
        });
        videoRecyclerView.setAdapter(videosAdapter);
        videoRecyclerView.setItemAnimator(new DefaultItemAnimator());

        bookRecyclerView = findViewById(R.id.course_book_recycler_view);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        bookAdapter = new SubjectMyAdapter(bookList, this, new SubjectMyAdapter.RecyclerClickListener() {
            @Override
            public void onClick(View view, int adapterPosition) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(bookList.get(adapterPosition).getLink()), "application/pdf");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.d("l", "l");
                }
                showMyAdd(intent);
            }
        });
        bookRecyclerView.setAdapter(bookAdapter);
        bookRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Button syllabus = findViewById(R.id.course_syllabus);
        syllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(CourseSubject.object.getSyllabusURL().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(),"Sorry! Syllabus not available",Toast.LENGTH_LONG).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(CourseSubject.object.getSyllabusURL()));
                startActivity(i);
                showMyAdd(i);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        },500);
    }

    private void loadPreviousYearData() {
        previousYearList.clear();
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection(CourseSubjectList.collection).document(CourseSubject.object.getTitle()).collection("papers").whereLessThan("priority","5").orderBy("priority")

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

                                String link = change.getDocument().get("link").toString();
                                String title = change.getDocument().get("title").toString();
                                String icon = change.getDocument().get("icon").toString();

//                                list.add(new CCSUnotesDataModel(title,imageUrl));
                                previousYearList.add(new PreviousYearDataModel(title,icon,link));
                                previousYearAdapter.notifyDataSetChanged();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                        showAvailable();
                        progressDialog.setMessage("Loading Videos...");
                    }
                });
    }

    private void loadBookList() {
        bookList.clear();
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection(CourseSubjectList.collection).document(CourseSubject.object.getTitle()).collection("book").whereLessThan("priority","5").orderBy("priority")

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

                                String link = change.getDocument().get("link").toString();
                                String title = change.getDocument().get("title").toString();
                                String image_url = change.getDocument().get("image_url").toString();

//                                list.add(new CCSUnotesDataModel(title,imageUrl));
                                bookList.add(new SubjectDataModel(title,image_url,link));
                                bookAdapter.notifyDataSetChanged();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                        showAvailable();
                        progressDialog.setMessage("Loading Videos...");
                    }
                });
    }

    private void loadPdfsList() {
        pdfList.clear();
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection(CourseSubjectList.collection).document(CourseSubject.object.getTitle()).collection("pdf").whereLessThan("priority","5").orderBy("priority")

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

                                String link = change.getDocument().get("link").toString();
                                String title = change.getDocument().get("title").toString();
                                String image_url = change.getDocument().get("image_url").toString();

//                                list.add(new CCSUnotesDataModel(title,imageUrl));
                                pdfList.add(new SubjectDataModel(title,image_url,link));
                                pdfAdapter.notifyDataSetChanged();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                        showAvailable();
                        progressDialog.setMessage("Loading Videos...");
                    }
                });
    }

    private void loadVideosList() {
        videoList.clear();
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection(CourseSubjectList.collection).document(CourseSubject.object.getTitle()).collection("video").whereLessThan("priority","5").orderBy("priority")

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

                                String link = change.getDocument().get("link").toString();
                                String title = change.getDocument().get("title").toString();
                                String image_url = change.getDocument().get("image_url").toString();

//                                list.add(new CCSUnotesDataModel(title,imageUrl));
                                videoList.add(new SubjectDataModel(title,image_url,link));
                                videosAdapter.notifyDataSetChanged();
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

    private void showAvailable() {
        if (pdfList.isEmpty())
            pdfAvailable.setText("Sorry! No Pdfs are available");
        else
            pdfAvailable.setText("");

        if(videoList.isEmpty())
            videoAvailabe.setText("Sorry! No videos are available");
        else
            videoAvailabe.setText("");

        if (previousYearList.isEmpty())
            previousYearAvailable.setText("Sorry! No Papers are available");
        else
            previousYearAvailable.setText("");

        if(bookList.isEmpty())
            bookAvailable.setText("Sorry! No Books are available");
        else
            bookAvailable.setText("");
    }
}

