package in.dux.p017ak.subject;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

import in.dux.p017ak.R;
import in.dux.p017ak.programming_lang.ProgrammingLangDataModel;
import in.dux.p017ak.utils.MyActivity;

import static android.support.constraint.Constraints.TAG;
import static in.dux.p017ak.utils.CheckNetworkConnection.isConnectionAvailable;

public class Subject extends MyActivity {
    private ImageView toolbarImage;
    private TextView introText,pdfAvailabe,videosAvailable;
    private RecyclerView pdfRecycler,videoRecycler;
    private RecyclerView.Adapter pdfAdapter,videoAdapter;
    private ProgressDialog progressDialog;
    public static ProgrammingLangDataModel toolbarData;
    private ArrayList<SubjectDataModel> pdfList = new ArrayList<>();
    private ArrayList<SubjectDataModel> videoList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        progressDialog = new ProgressDialog(Subject.this);
        progressDialog.setMessage("Loading Notes...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        loadAdvertisement();
        loadListFromFireStore();

        toolbarImage = findViewById(R.id.subject_toolbar_image);
        Glide.with(getApplicationContext())
                .load(toolbarData.getImage_url())
                .into(toolbarImage);
        toolbarImage.setScaleType(ImageView.ScaleType.FIT_XY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.subject_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.subject_collapsing);
        collapsingToolbar.setTitle(toolbarData.getTitle());
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);

        pdfAvailabe = findViewById(R.id.programming_books_availabe);
        videosAvailable = findViewById(R.id.programming_videos_available);
        introText = findViewById(R.id.subject_intro);
        introText.setText(toolbarData.getDescription());
        pdfRecycler = findViewById(R.id.subject_pdf_recycler);
        pdfRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pdfAdapter = new SubjectMyAdapter(pdfList, Subject.this, new SubjectMyAdapter.RecyclerClickListener() {
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
        pdfRecycler.setAdapter(pdfAdapter);
        pdfRecycler.setItemAnimator(new DefaultItemAnimator());

        videoRecycler = findViewById(R.id.subject_video_recycler);
        videoRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        videoAdapter = new SubjectMyAdapter(videoList, Subject.this, new SubjectMyAdapter.RecyclerClickListener() {
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
        videoRecycler.setAdapter(videoAdapter);
        videoRecycler.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadListFromFireStore() {

        pdfList.clear();
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("programming_language").document(toolbarData.getTitle()).collection("pdf").whereLessThan("priority","5").orderBy("priority")

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



        videoList.clear();
        db.setFirestoreSettings(settings);

        db.collection("programming_language").document(toolbarData.getTitle()).collection("video").whereLessThan("priority","5").orderBy("priority")

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
                                videoAdapter.notifyDataSetChanged();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                        showAvailable();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        },500);

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
            pdfAvailabe.setText("Sorry! No Books are available");
        else
            pdfAvailabe.setText("");

        if(videoList.isEmpty())
            videosAvailable.setText("Sorry! No videos are available");
        else
            videosAvailable.setText("");

    }
}
