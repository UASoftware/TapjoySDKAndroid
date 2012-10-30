package com.tapjoy.easyAppMultipleCurrency;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyConstants;
import com.tapjoy.TapjoyDisplayAdNotifier;
import com.tapjoy.TapjoyDisplayAdSize;
import com.tapjoy.TapjoyFeaturedAppObject;
import com.tapjoy.TapjoyFeaturedAppNotifier;
import com.tapjoy.TapjoyLog;


public class EasyAppMultipleCurrency extends Activity implements View.OnClickListener, TapjoyFeaturedAppNotifier, TapjoyDisplayAdNotifier
{
	TextView pointsTextView;
	TextView tapjoySDKVersionView;

	int point_total;
	String currency_name;
	
	String displayText = "";
	boolean update_text = false;
	boolean earnedPoints = false;
	
	// Banner Ads.
	boolean update_display_ad = false;
	View adView;
	RelativeLayout relativeLayout;
	LinearLayout adLinearLayout;
	
	public static final String CURRENCY_GOLD 		= "62f27e18-b17f-4a05-989e-6e54b9ab5b57";
	public static final String CURRENCY_SILVER 		= "21951da9-3e85-4053-8061-00866471b04e";
	
	// Need handler for callbacks to the UI thread
	final Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Enables logging to the console.
		TapjoyLog.enableLogging(true);
		
		// Connect with the Tapjoy server.  Call this when the application first starts.
		// REPLACE THE APP ID WITH YOUR TAPJOY APP ID.
		// REPLACE THE SECRET KEY WITH YOUR SECRET KEY.
		TapjoyConnect.requestTapjoyConnect(getApplicationContext(), "62f27e18-b17f-4a05-989e-6e54b9ab5b57", "0tG4bwQr6ewcJ5AaBYhr");
		
		TapjoyConnect.getTapjoyConnectInstance().setUserID("some_user_id");
		
		// For PAID APPS ONLY.  Replace your Paid App Pay-Per-Action ID as the parameter.
		//TapjoyConnect.getTapjoyConnectInstance().enablePaidAppWithActionID("ENTER_YOUR_PAID_APP_ACTION_ID_HERE");
		
		relativeLayout = (RelativeLayout)findViewById(R.id.RelativeLayout01);
		adLinearLayout = (LinearLayout)findViewById(R.id.AdLinearLayout);
		
		// OFFERS GOLD
		Button offers = (Button) findViewById(R.id.OffersButton);
		offers.setOnClickListener(this);
		
		// OFFERS SELECTOR
		Button offersSelector = (Button) findViewById(R.id.OffersSelectorButton);
		offersSelector.setOnClickListener(this);
		
		// OFFERS SILVER
		Button offersSilver = (Button) findViewById(R.id.OffersSilverButton);
		offersSilver.setOnClickListener(this);
		
		// FEATURED APP
		Button getFeaturedApp = (Button) findViewById(R.id.GetFeaturedApp);
		getFeaturedApp.setOnClickListener(this);
		
		// FEAURED APP SILVER
		Button getFeaturedAppSilver = (Button) findViewById(R.id.GetFeaturedAppSilver);
		getFeaturedAppSilver.setOnClickListener(this);
		
		// Banner Ad
		Button displayAd = (Button) findViewById(R.id.DisplayAd);
		displayAd.setOnClickListener(this);

		// Banner Ad SILVER
		Button displayAdSilver = (Button) findViewById(R.id.DisplayAdSilver);
		displayAdSilver.setOnClickListener(this);
		
		// MULTIPLIER 1x
		Button multiplier1X = (Button) findViewById(R.id.Mulitiplier1XButton);
		multiplier1X.setOnClickListener(this);
		
		// MULTIPLIER 2x
		Button multiplier2X = (Button) findViewById(R.id.Mulitiplier2XButton);
		multiplier2X.setOnClickListener(this);
		
		pointsTextView = (TextView)findViewById(R.id.PointsTextView);
		
		tapjoySDKVersionView = (TextView)findViewById(R.id.TapjoySDKVersionView);
		tapjoySDKVersionView.setText("This is only to demo APIs for multiple currencies.\nCompleting offers with multiple currencies will only work with NON-MANAGED by Tapjoy currency.\nSDK version: " + TapjoyConstants.TJC_LIBRARY_VERSION_NUMBER);
	}
	
	public void onClick(View v)
	{
		if (v instanceof Button) 
		{
			int id = ((Button) v).getId();
			
			switch (id)
			{		
				// OFFERS SELECTOR
				case R.id.OffersSelectorButton:
					TapjoyConnect.getTapjoyConnectInstance().showOffersWithCurrencyID(CURRENCY_GOLD, true);
					break;
				// OFFERS GOLD
				case R.id.OffersButton:
					TapjoyConnect.getTapjoyConnectInstance().showOffersWithCurrencyID(CURRENCY_GOLD, false);
					break;
				// OFFERS SILVER
				case R.id.OffersSilverButton:
					TapjoyConnect.getTapjoyConnectInstance().showOffersWithCurrencyID(CURRENCY_SILVER, false);
					break;
					
				// FEATURED APP GOLD
				case R.id.GetFeaturedApp:
					TapjoyConnect.getTapjoyConnectInstance().getFeaturedAppWithCurrencyID(CURRENCY_GOLD, this);
					break;
				// FEATURED APP SILVER
				case R.id.GetFeaturedAppSilver:
					TapjoyConnect.getTapjoyConnectInstance().getFeaturedAppWithCurrencyID(CURRENCY_SILVER, this);
					break;
					
				// Banner Ad GOLD
				case R.id.DisplayAd:
					TapjoyConnect.getTapjoyConnectInstance().setBannerAdSize(TapjoyDisplayAdSize.TJC_AD_BANNERSIZE_640X100);
					TapjoyConnect.getTapjoyConnectInstance().getDisplayAdWithCurrencyID(CURRENCY_GOLD, this);
					break;
				// Banner Ad SILVER
				case R.id.DisplayAdSilver:
					TapjoyConnect.getTapjoyConnectInstance().setBannerAdSize(TapjoyDisplayAdSize.TJC_AD_BANNERSIZE_640X100);
					TapjoyConnect.getTapjoyConnectInstance().getDisplayAdWithCurrencyID(CURRENCY_SILVER, this);
					break;

				// MULTIPLIER 1X
				case R.id.Mulitiplier1XButton:
					TapjoyConnect.getTapjoyConnectInstance().setCurrencyMultiplier(1.0f);
					pointsTextView.setText("Multiplier: x" + TapjoyConnect.getTapjoyConnectInstance().getCurrencyMultiplier());
					break;
				// MULTIPLIER 2X
				case R.id.Mulitiplier2XButton:
					TapjoyConnect.getTapjoyConnectInstance().setCurrencyMultiplier(2.0f);
					pointsTextView.setText("Multiplier: x" + TapjoyConnect.getTapjoyConnectInstance().getCurrencyMultiplier());
					break;
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	
	// Create runnable for posting
	final Runnable mUpdateResults = new Runnable() 
	{
		public void run() 
		{
			updateResultsInUi();
		}
	};
	
	
	private void updateResultsInUi() 
	{
		if (update_display_ad)
		{
			// Remove all subviews of our ad layout.
			adLinearLayout.removeAllViews();
			
			// Add the ad to our layout.
			adLinearLayout.addView(adView);
			
			update_display_ad = false;
		}
		
		// Back in the UI thread -- update our UI elements based on the data in mResults
		if (pointsTextView != null)
		{	
			// Update the display text.
			if (update_text)
			{
				pointsTextView.setText(displayText);
				update_text = false;
			}
		}
	}

	
	//================================================================================
	// CALLBACK Methods
	//================================================================================
	
	
	// Notifier for receiving the full screen ad data on a successful connect.
	public void getFeaturedAppResponse(TapjoyFeaturedAppObject featuredApObject)
	{
		Log.i("EASY_APP", "Displaying Full Screen Ad..");
		TapjoyConnect.getTapjoyConnectInstance().showFeaturedAppFullScreenAd();
	}

	
	// Notifier for when there is an error or no full screen ad to display.
	public void getFeaturedAppResponseFailed(String error)
	{
		Log.i("EASY_APP", "No Full Screen Ad to display: " + error);
		
		update_text = true;
		displayText = "No Full Screen Ad to display.";
		
		// We must use a handler since we cannot update UI elements from a different thread.
		mHandler.post(mUpdateResults);
	}

	
	public void getDisplayAdResponse(View view)
	{
		adView = view;

		int ad_width = adView.getLayoutParams().width;
		int ad_height = adView.getLayoutParams().height;
		
		Log.i("EASY_APP", "adView dimensions: " + ad_width + "x" + ad_height);
		
		// Using screen width, but substitute for the any width.
		int desired_width = adLinearLayout.getMeasuredWidth();
		
		if (desired_width > ad_width)
			desired_width = ad_width;
		
		// Resize banner to desired width and keep aspect ratio.
		LayoutParams layout = new LayoutParams(desired_width, (desired_width*ad_height)/ad_width);
		adView.setLayoutParams(layout);
		
		Log.i("EASY_APP", "adLinearLayout dimensions: " + adLinearLayout.getMeasuredWidth() + "x" + adLinearLayout.getMeasuredHeight());
		
		update_display_ad = true;
		
		// We must use a handler since we cannot update UI elements from a different thread.
		mHandler.post(mUpdateResults);
	}

	
	public void getDisplayAdResponseFailed(String error)
	{
		Log.i("EASY_APP", "getDisplayAd error: " + error);
		
		update_text = true;
		displayText = "Banner Ads: " + error;
		
		// We must use a handler since we cannot update UI elements from a different thread.
		mHandler.post(mUpdateResults);
	}
}
