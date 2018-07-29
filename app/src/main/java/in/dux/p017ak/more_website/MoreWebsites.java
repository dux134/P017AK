package in.dux.p017ak.more_website;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import in.dux.p017ak.R;
import in.dux.p017ak.utils.MyActivity;

import static android.support.constraint.Constraints.TAG;
import static in.dux.p017ak.utils.CheckNetworkConnection.isConnectionAvailable;

public class MoreWebsites extends MyActivity {
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private RecyclerView recyclerView4;
    private RecyclerView recyclerView5;
    private RecyclerView.Adapter adapter1;
    private RecyclerView.Adapter adapter2;
    private RecyclerView.Adapter adapter3;
    private RecyclerView.Adapter adapter4;
    private RecyclerView.Adapter adapter5;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<MoreWebsiteDataModel> list1 = new ArrayList<>();
    private ArrayList<MoreWebsiteDataModel> list2 = new ArrayList<>();
    private ArrayList<MoreWebsiteDataModel> list3 = new ArrayList<>();
    private ArrayList<MoreWebsiteDataModel> list4 = new ArrayList<>();
    private ArrayList<MoreWebsiteDataModel> list5 = new ArrayList<>();
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_websites);
        if(!isConnectionAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_LONG).show();
        }

        progressDialog = new ProgressDialog(MoreWebsites.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        loadAdvertisement();

        textView1 = findViewById(R.id.textView01);
        textView2 = findViewById(R.id.textView02);
        textView3 = findViewById(R.id.textView03);
        textView4 = findViewById(R.id.textView04);
        textView5 = findViewById(R.id.tutorialsPointTextView);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadTitle();
                loadWebsiteFromFireStore();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.moreWebsitesToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView1 = findViewById(R.id.recyclerView01);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter1 = new MoreWebsiteMyAdapter(list1, new MoreWebsiteMyAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(list1.get(position).getWebsiteLinkUrl()));
                startActivity(i);
                showMyAdd(i);
            }
        });
        recyclerView1.setAdapter(adapter1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());

        recyclerView2 = findViewById(R.id.recyclerView02);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter2 = new MoreWebsiteMyAdapter(list2, new MoreWebsiteMyAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(list2.get(position).getWebsiteLinkUrl()));
                startActivity(i);
                showMyAdd(i);
            }
        });
        recyclerView2.setAdapter(adapter2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter3 = new MoreWebsiteMyAdapter(list3, new MoreWebsiteMyAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(list3.get(position).getWebsiteLinkUrl()));
                startActivity(i);
                showMyAdd(i);
            }
        });
        recyclerView3.setAdapter(adapter3);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());

        recyclerView4 = findViewById(R.id.recyclerView04);
        recyclerView4.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter4 = new MoreWebsiteMyAdapter(list4, new MoreWebsiteMyAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(list4.get(position).getWebsiteLinkUrl()));
                startActivity(i);
                showMyAdd(i);
            }
        });
        recyclerView4.setAdapter(adapter4);
        recyclerView4.setItemAnimator(new DefaultItemAnimator());

        recyclerView5 = findViewById(R.id.recyclerView05);
        recyclerView5.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter5 = new MoreWebsiteMyAdapter(list5, new MoreWebsiteMyAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(list5.get(position).getWebsiteLinkUrl()));
                startActivity(i);
                showMyAdd(i);
            }
        });
        recyclerView5.setAdapter(adapter5);
        recyclerView5.setItemAnimator(new DefaultItemAnimator());



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

    private void loadWebsiteFromFireStore() {
        list1.clear();
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("websites").document("one").collection("websites").orderBy("priority", Query.Direction.ASCENDING)
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

                                String imageUrl = change.getDocument().get("image_url").toString();
                                String title = change.getDocument().get("title").toString();
                                String link = change.getDocument().get("link").toString();

                                list1.add(new MoreWebsiteDataModel(title,link,imageUrl));
                                refereshContent1();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });

        list2.clear();
        db.setFirestoreSettings(settings);

        db.collection("websites").document("two").collection("websites").orderBy("priority", Query.Direction.ASCENDING)
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

                                String imageUrl = change.getDocument().get("image_url").toString();
                                String title = change.getDocument().get("title").toString();
                                String link = change.getDocument().get("link").toString();

                                list2.add(new MoreWebsiteDataModel(title,link,imageUrl));
                                refereshContent2();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });

        list3.clear();
        db.setFirestoreSettings(settings);

        db.collection("websites").document("three").collection("websites").orderBy("priority", Query.Direction.ASCENDING)
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

                                String imageUrl = change.getDocument().get("image_url").toString();
                                String title = change.getDocument().get("title").toString();
                                String link = change.getDocument().get("link").toString();

                                list3.add(new MoreWebsiteDataModel(title,link,imageUrl));
                                refereshContent3();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });

        list4.clear();
        db.setFirestoreSettings(settings);

        db.collection("websites").document("four").collection("websites").orderBy("priority", Query.Direction.ASCENDING)
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

                                String imageUrl = change.getDocument().get("image_url").toString();
                                String title = change.getDocument().get("title").toString();
                                String link = change.getDocument().get("link").toString();

                                list4.add(new MoreWebsiteDataModel(title,link,imageUrl));
                                refereshContent4();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });

        list5.clear();
        db.setFirestoreSettings(settings);

        db.collection("websites").document("five").collection("websites").orderBy("priority", Query.Direction.ASCENDING)
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

                                String imageUrl = change.getDocument().get("image_url").toString();
                                String title = change.getDocument().get("title").toString();
                                String link = change.getDocument().get("link").toString();

                                list5.add(new MoreWebsiteDataModel(title,link,imageUrl));
                                refereshContent5();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        },500);
                    }
                });

    }

    private void loadTitle() {
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("websites").orderBy("priority", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen error", e);
                            return;
                        }
                        int count = 1;
                        assert querySnapshot != null;
                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {
                                String title = change.getDocument().get("title").toString();

                                switch(count) {
                                    case 1:
                                        textView1.setText(title);
                                        count++;
                                        break;

                                    case 2:
                                        textView2.setText(title);
                                        count++;
                                        break;

                                    case 3:
                                        textView3.setText(title);
                                        count++;
                                        break;

                                    case 4:
                                        textView4.setText(title);
                                        count++;
                                        break;

                                    case 5:
                                        textView5.setText(title);
                                        count++;
                                        break;
                                }
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });

    }

    private void refereshContent1() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                mySwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
        adapter1.notifyDataSetChanged();
    }

    private void refereshContent2() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                mySwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
        adapter2.notifyDataSetChanged();
    }

    private void refereshContent3() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                mySwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
        adapter3.notifyDataSetChanged();
    }

    private void refereshContent4() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                mySwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
        adapter4.notifyDataSetChanged();
    }

    private void refereshContent5() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                mySwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
        adapter5.notifyDataSetChanged();
    }

}

