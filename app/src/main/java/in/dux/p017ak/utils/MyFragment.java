package in.dux.p017ak.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public class MyFragment extends Fragment {
    private static final int START_LEVEL = 1;
    private int mLevel;
    public static Intent intent;
    public InterstitialAd mInterstitialAd;
    private PrefManager prefManager;

    public void loadAdvertisement() {
        prefManager = new PrefManager(getActivity());
        mInterstitialAd = newInterstitialAd();

        loadInterstitial();
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(getContext());
        interstitialAd.setAdUnitId(getAdId());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                //mNextLevelButton.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //mNextLevelButton.setEnabled(true);
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                goToNextLevel();
                startActivity(intent);
            }
        });
        return interstitialAd;
    }

    private void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private String getAdId() {
//        if(Dashboard.addCount == 1) {
//            return prefManager.getAdmobIdDux();
//        } else {
//            return prefManager.getAdmobId();
//        }
        return "";
    }

    public void showMyAdd(Intent i) {
        intent = i;

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (mInterstitialAd.isLoaded()) {
////                    Toast.makeText(getActivity(), "loaded " + Dashboard.addCount, Toast.LENGTH_SHORT).show();
//                    if (Dashboard.addCount == 0)
//                        Dashboard.addCount = 1;
//                    else
//                        Dashboard.addCount = 0;
//                    mInterstitialAd.show();
//                }


            }
        },1000);
    }
}
