package in.dux.p017ak;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import in.dux.p017ak.course_subject.CourseSubjectList;
import in.dux.p017ak.notification.NotificationDataModel;
import in.dux.p017ak.utils.PrefManager;

import static android.support.constraint.Constraints.TAG;

public class SplashScreen extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static int addCount = 0;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        prefManager = new PrefManager(SplashScreen.this);
        loadAddId();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this,Dashboard.class));
                finish();
            }
        },1000);
    }

    private void loadAddId() {
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("z_admob")
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

                                String id = change.getDocument().get("id").toString();
                                String type = change.getDocument().get("type").toString();

                                if(type.equalsIgnoreCase("dux"))
                                    prefManager.setAdmobIdDux(id);

                                else
                                    prefManager.setAdmobId(id);


                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });

        db.collection("z_rate_and_share")
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

                                String rate = change.getDocument().get("rate_link").toString();
                                String share = change.getDocument().get("share_text").toString();
                                prefManager.setRATE_US_LINK(rate);
                                prefManager.setSHARE_LINK(share);
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });
    }
}
