package in.dux.p017ak;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ThrowOnExtraProperties;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import in.dux.p017ak.course_subject.CourseSubjectList;
import in.dux.p017ak.more_website.MoreWebsites;
import in.dux.p017ak.notification.NotificationDataModel;
import in.dux.p017ak.notification.NotificationMyAdapter;
import in.dux.p017ak.programming_lang.ProgrammingLangDataModel;
import in.dux.p017ak.programming_lang.ProgrammingLangMyAdapter;
import in.dux.p017ak.subject.Subject;
import in.dux.p017ak.subject.SubjectDataModel;
import in.dux.p017ak.subject.SubjectMyAdapter;
import in.dux.p017ak.utils.MyActivity;
import in.dux.p017ak.utils.PrefManager;
import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;

import static android.support.constraint.Constraints.TAG;
import static in.dux.p017ak.utils.CheckNetworkConnection.isConnectionAvailable;

public class Dashboard extends MyActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Slider slider;
    private PrefManager prefManager;
    public static List<Slide> slideList = new ArrayList<>();
    private ArrayList<String> sliderLinks = new ArrayList<>();
    private ArrayList<ProgrammingLangDataModel> programmingLangList = new ArrayList<>();
    private ArrayList<SubjectDataModel> eBooksList = new ArrayList<>();
    private ArrayList<NotificationDataModel> notificationList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView programmingLangRecyclerView,notificationRecycler,eBookRecycler;
    private RecyclerView.Adapter programmingLangAdapter,notificationAdapter,eBookAdapter;

    private ImageView aktuImage,aktuResultImage,codeshefImage,tutorialspointImage,scholarshipImage,moreImage;
    private TextView aktu,aktuResult,codeshef,tutrialsPoint,scholarship,more,notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);

        slider = findViewById(R.id.slider);

        prefManager = new PrefManager(Dashboard.this);
        loadAdvertisement();
        loadSliderImageFromFireStore();
        loadProgrammingLanguageList();
        loadNotificationList();
        selectCourse();
        loadEBooksList();
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setTitle("Whole Course");
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.secondaryLightColor));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        RecyclerView courseRecycler = findViewById(R.id.courseRecycler);
        slider.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(sliderLinks.get(i).equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(),"Sorry! no link Available", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(sliderLinks.get(i)));
                startActivity(intent);
                showMyAdd(intent);
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notificationFunc();
                websiteCard();
            }
        });

        programmingLangRecyclerView = findViewById(R.id.programmingLangRecycler);
        programmingLangRecyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this, LinearLayoutManager.HORIZONTAL, false));
        programmingLangAdapter = new ProgrammingLangMyAdapter(programmingLangList, getApplicationContext(), new ProgrammingLangMyAdapter.RecyclerItemListener() {
            @Override
            public void onClick(View view, int adapterPosition) {
                Subject.toolbarData = programmingLangList.get(adapterPosition);
                Intent intent = new Intent(Dashboard.this,Subject.class);
                startActivity(intent);
            }
        });
        programmingLangRecyclerView.setAdapter(programmingLangAdapter);
        programmingLangRecyclerView.setItemAnimator(new DefaultItemAnimator());


        eBookRecycler = findViewById(R.id.ebooks_recycler_view);
        eBookRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        eBookAdapter = new SubjectMyAdapter(eBooksList, this, new SubjectMyAdapter.RecyclerClickListener() {
            @Override
            public void onClick(View view, int adapterPosition) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(eBooksList.get(adapterPosition).getLink()), "application/pdf");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.d("l", "l");
                }
                showMyAdd(intent);

            }
        });
        eBookRecycler.setAdapter(eBookAdapter);
        eBookRecycler.setItemAnimator(new DefaultItemAnimator());
    }

    private void notificationFunc() {
        notificationRecycler = findViewById(R.id.notificationRecyclerView);
        notificationRecycler.setLayoutManager(new LinearLayoutManager(Dashboard.this, LinearLayoutManager.HORIZONTAL, false));
        notificationAdapter = new NotificationMyAdapter(notificationList,Dashboard.this, new NotificationMyAdapter.RecyclerItemListener() {
            @Override
            public void onClick(View view, int adapterPosition) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(notificationList.get(adapterPosition).getLink()));
                startActivity(i);
                showMyAdd(i);
            }
        });
        notificationRecycler.setAdapter(notificationAdapter);
        notificationRecycler.setItemAnimator(new DefaultItemAnimator());
        notificationRecycler.setNestedScrollingEnabled(false);
        ImageView rightArrow = findViewById(R.id.rightArrow);
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationRecycler.smoothScrollBy( NotificationMyAdapter.width-100,0);
            }
        });
        ImageView leftArrow = findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationRecycler.smoothScrollBy( -(NotificationMyAdapter.width-100),0);
            }
        });
    }
    private void websiteCard() {
        aktu = findViewById(R.id.aktuTextView);
        aktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://aktu.ac.in/"));
                startActivity(i);
                showMyAdd(i);
            }
        });
        aktuImage = findViewById(R.id.aktuImage);
        aktuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://aktu.ac.in/"));
                startActivity(i);
                showMyAdd(i);
            }
        });

        aktuResult = findViewById(R.id.aktuResultTextView);
        aktuResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://erp.aktu.ac.in/WebPages/OneView/OneView.aspx"));
                startActivity(i);
                showMyAdd(i);
            }
        });
        aktuResultImage = findViewById(R.id.aktuResultImage);
        aktuResultImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://erp.aktu.ac.in/WebPages/OneView/OneView.aspx"));
                startActivity(i);
                showMyAdd(i);
            }
        });

        scholarship = findViewById(R.id.upScholarshipTextView);
        scholarship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://scholarship.up.nic.in/"));
                startActivity(i);
                showMyAdd(i);
            }
        });
        scholarshipImage = findViewById(R.id.upScholarshipImage);
        scholarshipImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://scholarship.up.nic.in/"));
                startActivity(i);
                showMyAdd(i);
            }
        });

        codeshef = findViewById(R.id.codeshefTextView);
        codeshef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.codechef.com/"));
                startActivity(i);
                showMyAdd(i);
            }
        });
        codeshefImage = findViewById(R.id.codeshefImage);
        codeshefImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.codechef.com/"));
                startActivity(i);
                showMyAdd(i);
            }
        });

        tutrialsPoint = findViewById(R.id.tutorialsPointTextView);
        tutrialsPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.tutorialspoint.com/"));
                startActivity(i);
                showMyAdd(i);
            }
        });
        tutorialspointImage = findViewById(R.id.tutorialsPointImage);
        tutorialspointImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "You are currently offline, Please connect to internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.tutorialspoint.com/"));
                startActivity(i);
                showMyAdd(i);
            }
        });

        more = findViewById(R.id.moreTextView);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, MoreWebsites.class));
            }
        });
        moreImage = findViewById(R.id.moreImage);
        moreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, MoreWebsites.class));
            }
        });
    }
    private void selectCourse() {
        TextView IIndText, IIIrdText, IVthText;
        ImageView IIndImage, IIIrdImage, IVthImage;
        IIIrdImage = findViewById(R.id.IIIrdImage);
        IIIrdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseSubjectList.collection = "cs3";
                startActivity(new Intent(Dashboard.this,CourseSubjectList.class));
            }
        });
        IIIrdText = findViewById(R.id.IIIrdText);
        IIIrdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseSubjectList.collection = "cs3";
                startActivity(new Intent(Dashboard.this,CourseSubjectList.class));
            }
        });
        IIndImage = findViewById(R.id.IIndImage);
        IIndImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseSubjectList.collection = "cs2";
                startActivity(new Intent(Dashboard.this,CourseSubjectList.class));
            }
        });
        IIndText = findViewById(R.id.IIndText);
        IIndText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseSubjectList.collection = "cs2";
                startActivity(new Intent(Dashboard.this,CourseSubjectList.class));
            }
        });
        IVthImage = findViewById(R.id.IVthImage);
        IVthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseSubjectList.collection = "cs4";
                startActivity(new Intent(Dashboard.this,CourseSubjectList.class));
            }
        });
        IVthText = findViewById(R.id.IVthText);
        IVthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseSubjectList.collection = "cs4";
                startActivity(new Intent(Dashboard.this,CourseSubjectList.class));
            }
        });
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.dashboard, menu); //your file name
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//        if (id == R.id.change_course) {
////            startActivity(new Intent(BottomNavigation.this, SelectCourse.class));
////            finish();
//            return true;
//        } else if(id == R.id.feedback) {
////            startActivity(new Intent(BottomNavigation.this, Feedback.class));
////            finish();
//            return true;
//        } else if(id == R.id.share_notes) {
////            startActivity(new Intent(BottomNavigation.this, ShareNotes.class));
////            finish();
//            return true;
//        }

//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.feedback) {
            startActivity(new Intent(Dashboard.this,Feedback.class));
        } else if(id == R.id.rate_us) {
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(prefManager.getRATE_US_LINK()));
            startActivity(rateIntent);
        } else if(id == R.id.nav_share) {

            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                String sAux = prefManager.getSHARE_LINK();
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }


        } else if(id == R.id.navigation_secondYear) {
            CourseSubjectList.collection = "cs2";
            startActivity(new Intent(Dashboard.this,CourseSubjectList.class));
        } else if(id == R.id.navigation_thirdYear) {
            CourseSubjectList.collection = "cs3";
            startActivity(new Intent(Dashboard.this,CourseSubjectList.class));
        } else if(id == R.id.navigation_fourthYear) {
            CourseSubjectList.collection = "cs4";
            startActivity(new Intent(Dashboard.this,CourseSubjectList.class));
        } else if(id == R.id.navigation_more_websites) {
            startActivity(new Intent(Dashboard.this,MoreWebsites.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadProgrammingLanguageList() {
        programmingLangList.clear();
        db.collection("programming_language").whereLessThan("priority","5").orderBy("priority")
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
                                String desc = change.getDocument().get("description").toString();

                                programmingLangList.add(new ProgrammingLangDataModel(title,desc,imageUrl));
                                programmingLangAdapter.notifyDataSetChanged();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });


    }
    private void loadNotificationList() {
        notificationList.clear();
        db.collection("notifications").orderBy("priority", Query.Direction.ASCENDING)
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
                                String link = change.getDocument().get("link").toString();

                                notificationList.add(new NotificationDataModel(title,link));
                                notificationAdapter.notifyDataSetChanged();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });
    }

    private void loadEBooksList() {
        eBooksList.clear();
        db.collection("ebooks").whereLessThan("priority","5").orderBy("priority")
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
                                String link = change.getDocument().get("link").toString();
                                String image_url = change.getDocument().get("image_url").toString();

                                eBooksList.add(new SubjectDataModel(title,image_url,link));
                                eBookAdapter.notifyDataSetChanged();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });
    }

    private void loadSliderImageFromFireStore() {
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("image_slider").orderBy("priority", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen error", e);
                            return;
                        }


                        assert querySnapshot != null;
                        int count = 0;
                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {

                                String url = change.getDocument().get("image_url").toString();
                                String link = change.getDocument().get("link").toString();
                                Integer priority = Integer.valueOf(change.getDocument().get("priority").toString());
                                sliderLinks.add(link);
                                slideList.add(new Slide(priority,url, getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));

                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                        slider.addSlides(slideList);
                    }
                });
    }
}