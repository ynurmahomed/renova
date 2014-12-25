package br.ufu.renova.admob;

import android.app.Activity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by pablohpsilva on 11/6/14.
 */
public class Interstitial {

    private InterstitialAd interstitial;
    private Activity activity;

    //private static final String AD_INTERSTITIAL_ID = "ca-app-pub-6713098943014804/4601996177";
    private static final String AD_INTERSTITIAL_ID = "ca-app-pub-2113484633994413/3386134884";

    public Interstitial(Activity activity){
        this.activity = activity;
    }

    public Interstitial getInterstitial(){
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E0DC64EC0BFF17A4F00640C5294B0128")
                .addTestDevice("FFA680EF5BC1615AEDF85264F09B8E94")
                .build();

        interstitial = new InterstitialAd(this.activity);
        interstitial.setAdUnitId(AD_INTERSTITIAL_ID);
        interstitial.loadAd(adRequest);

        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
            }
        });
        return this;
    }
/*
    public void t(){
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E0DC64EC0BFF17A4F00640C5294B0128")
                .build();

        //interstitial = new InterstitialAd(BooksActivity.this);
        interstitial.setAdUnitId(AD_INTERSTITIAL_ID);
        interstitial.loadAd(adRequest);

        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
            }
        });


        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_UNIT_ID);

        LinearLayout layout = (LinearLayout) findViewById(R.id.login_form);
        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lay.addRule(RelativeLayout.ALIGN_BOTTOM, RelativeLayout.TRUE);
        lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        //adView.setLayoutParams(lay);
        layout.addView(adView,lay);

        // Start loading the ad in the background.
        adView.loadAd(adRequest);

    }
*/
    public void displayInterstitial() {
        // If Interstitial are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }


}
