package com.renovapp.app.admob;

import android.app.Activity;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by pablohpsilva on 11/7/14.
 */
public class Banner {
    private Activity activity;
    private AdView adView;

    private static final String AD_UNIT_ID = "ca-app-pub-6713098943014804/6078729371";

    public Banner(Activity activity){
        this.activity = activity;
    }

    public Banner getBanner(){
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E0DC64EC0BFF17A4F00640C5294B0128")
                .build();

        adView = new AdView(this.activity);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_UNIT_ID);

        /*LinearLayout layout = (LinearLayout) findViewById(R.id.login_form);
        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lay.addRule(RelativeLayout.ALIGN_BOTTOM, RelativeLayout.TRUE);
        lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        //adView.setLayoutParams(lay);
        layout.addView(adView,lay);*/

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
        return this;
    }

}
