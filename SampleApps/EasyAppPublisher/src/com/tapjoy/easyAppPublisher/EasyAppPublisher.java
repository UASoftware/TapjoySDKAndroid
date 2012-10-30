package com.tapjoy.easyAppPublisher;

import java.util.Hashtable;

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

import com.tapjoy.TapjoyAwardPointsNotifier;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyConstants;
import com.tapjoy.TapjoyDisplayAdNotifier;
import com.tapjoy.TapjoyDisplayAdSize;
import com.tapjoy.TapjoyEarnedPointsNotifier;
import com.tapjoy.TapjoyFeaturedAppNotifier;
import com.tapjoy.TapjoyFeaturedAppObject;
import com.tapjoy.TapjoyLog;
import com.tapjoy.TapjoyNotifier;
import com.tapjoy.TapjoyReEngagementAdNotifier;
import com.tapjoy.TapjoyReEngagementAdStatus;
import com.tapjoy.TapjoySpendPointsNotifier;
import com.tapjoy.TapjoyVideoNotifier;
import com.tapjoy.TapjoyVideoStatus;


public class EasyAppPublisher extends Activity implements View.OnClickListener, TapjoyNotifier, TapjoyFeaturedAppNotifier, TapjoySpendPointsNotifier, TapjoyDisplayAdNotifier, TapjoyAwardPointsNotifier, TapjoyEarnedPointsNotifier, TapjoyVideoNotifier 
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
	
	
	// Need handler for callbacks to the UI thread
	final Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Enables logging to the console.
		TapjoyLog.enableLogging(true);
		
		// OPTIONAL: For custom startup flags.
		Hashtable<String, String> flags = new Hashtable<String, String>();
		//flags.put(TapjoyConnectFlag.DISABLE_VIDEO_OFFERS, "true");
		
		// Connect with the Tapjoy server.  Call this when the application first starts.
		// REPLACE THE APP ID WITH YOUR TAPJOY APP ID.
		// REPLACE THE SECRET KEY WITH YOUR SECRET KEY.
		TapjoyConnect.requestTapjoyConnect(getApplicationContext(), "bba49f11-b87f-4c0f-9632-21aa810dd6f1", "yiQIURFEeKm0zbOggubu", flags);
		
		// For PAID APPS ONLY.  Replace your Paid App Pay-Per-Action ID as the parameter.
		//TapjoyConnect.getTapjoyConnectInstance(this).enablePaidAppWithActionID("ENTER_YOUR_PAID_APP_ACTION_ID_HERE");
		
		// No longer required.  Videos are initialized automatically.
		// Use initVideoAd to receive TapjoyVideoNotifier callbacks or to start manual caching.
		// TapjoyConnect.getTapjoyConnectInstance().initVideoAd(this);
		
		// Set our earned points notifier to this class.
		TapjoyConnect.getTapjoyConnectInstance().setEarnedPointsNotifier(this);
		
		relativeLayout = (RelativeLayout)findViewById(R.id.RelativeLayout01);
		adLinearLayout = (LinearLayout)findViewById(R.id.AdLinearLayout);
		
		// This button launches the offers page when clicked.
		Button offers = (Button) findViewById(R.id.OffersButton);
		offers.setOnClickListener(this);
		
		// This button retrieves the virtual currency info from the server.
		Button getPoints = (Button) findViewById(R.id.GetPointsButton);
		getPoints.setOnClickListener(this);
		
		// This spends virtual currency for this device.
		Button spendPoints = (Button) findViewById(R.id.SpendPointsButton);
		spendPoints.setOnClickListener(this);
		
		// This spends virtual currency for this device.
		Button awardPoints = (Button) findViewById(R.id.AwardPointsButton);
		awardPoints.setOnClickListener(this);
		
		// This button displays the full screen ad when clicked.
		Button getFeaturedApp = (Button) findViewById(R.id.GetFeaturedApp);
		getFeaturedApp.setOnClickListener(this);
		
		// This button displays the re-engagement ad when clicked.
		Button getReEngagementAd = (Button) findViewById(R.id.GetReEngagementAd);
		getReEngagementAd.setOnClickListener(this);
		
		// This button displays the banner ad when clicked.
		Button displayAd = (Button) findViewById(R.id.DisplayAd);
		displayAd.setOnClickListener(this);
		
		// Event tracking.
		Button iapEvent = (Button) findViewById(R.id.IAPEventButton);
		iapEvent.setOnClickListener(this);
		
		pointsTextView = (TextView)findViewById(R.id.PointsTextView);
		
		tapjoySDKVersionView = (TextView)findViewById(R.id.TapjoySDKVersionView);
		tapjoySDKVersionView.setText("SDK version: " + TapjoyConstants.TJC_LIBRARY_VERSION_NUMBER);
	}
	
	public void onClick(View v)
	{
		if (v instanceof Button) 
		{
			int id = ((Button) v).getId();
			
			switch (id)
			{
				case R.id.GetPointsButton:
					// Retrieve the virtual currency amount from the server.
					TapjoyConnect.getTapjoyConnectInstance().getTapPoints(this);
					break;
					
				case R.id.SpendPointsButton:
					// Spend virtual currency.
					TapjoyConnect.getTapjoyConnectInstance().spendTapPoints(25, this);
					break;
					
				case R.id.AwardPointsButton:
					// Spend virtual currency.
					TapjoyConnect.getTapjoyConnectInstance().awardTapPoints(10, this);
					break;
					
				case R.id.OffersButton:
					// This will show Offers web view from where you can download the latest offers.
					TapjoyConnect.getTapjoyConnectInstance().showOffers();
					break;
					
				case R.id.GetFeaturedApp:
					// Show the full screen ad banner.
					TapjoyConnect.getTapjoyConnectInstance().getFeaturedApp(this);
					break;
					
				case R.id.GetReEngagementAd:
                    // Show the featured app banner.
                    TapjoyConnect.getTapjoyConnectInstance().getReEngagementAd(new TapjoyReEngagementAdNotifier()
                    {
                        
                        @Override
                        public void getReEngagementAdResponseFailed(int error)
                        {
                            switch (error)
                            {
                                case TapjoyReEngagementAdStatus.STATUS_NETWORK_ERROR:
                                    displayText = "Re-engagement ad network error.";
                                    break;
                                    
                                case TapjoyReEngagementAdStatus.STATUS_NO_ADS_AVAILABLE:
                                    displayText = "No Re-engagement ads available";
                                    break;
                                    
                                case TapjoyReEngagementAdStatus.STATUS_SERVER_ERROR:
                                    displayText = "Re-engagement ad server error";
                                    break;
                            }
                            
                            update_text = true;
                            
                            // We must use a handler since we cannot update UI elements from a different thread.
                            mHandler.post(mUpdateResults);
                        }
                        
                        @Override
                        public void getReEngagementAdResponse()
                        {
                            TapjoyConnect.getTapjoyConnectInstance().showReEngagementFullScreenAd();
                        }
                    });
                    break;
					
				case R.id.DisplayAd:
					// Show the display/banner ad.
					TapjoyConnect.getTapjoyConnectInstance().enableBannerAdAutoRefresh(true);
					TapjoyConnect.getTapjoyConnectInstance().getDisplayAd(this);
					break;
					
				case R.id.IAPEventButton:
					TapjoyConnect.getTapjoyConnectInstance().sendIAPEvent("swag", 0.99f, 1, "usd");
					break;
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		// Send shutdown event tracking event.
		TapjoyConnect.getTapjoyConnectInstance().sendShutDownEvent();
		
		super.onDestroy();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		TapjoyConnect.getTapjoyConnectInstance().enableBannerAdAutoRefresh(false);
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

	
	// This method must be implemented if using the TapjoyConnect.getTapPoints() method.
	// It is the callback method which contains the currency and points data.
	public void getUpdatePoints(String currencyName, int pointTotal)
	{
		Log.i("EASY_APP", "currencyName: " + currencyName);
		Log.i("EASY_APP", "pointTotal: " + pointTotal);
		
		currency_name = currencyName;
		point_total = pointTotal;
		
		update_text = true;
		
		if (earnedPoints)
		{
			displayText = displayText + "\n" + currencyName + ": " + pointTotal;
			earnedPoints = false;
		}
		else
		{
			displayText = currencyName + ": " + pointTotal;
		}
		
		// We must use a handler since we cannot update UI elements from a different thread.
		mHandler.post(mUpdateResults);
	}
	
	
	// This method must be implemented if using the TapjoyConnect.getTapPoints() method.
	// It is the callback method which contains the currency and points data.
	public void getUpdatePointsFailed(String error)
	{
		Log.i("EASY_APP", "getTapPoints error: " + error);
		
		update_text = true;
		displayText = "Unable to retrieve tap points from server.";
		
		// We must use a handler since we cannot update UI elements from a different thread.
		mHandler.post(mUpdateResults);
	}
	
	
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

	
	// Notifier for when spending virtual currency succeeds.
	public void getSpendPointsResponse(String currencyName, int pointTotal)
	{
		Log.i("EASY_APP", "currencyName: " + currencyName);
		Log.i("EASY_APP", "pointTotal: " + pointTotal);
		
		update_text = true;
		displayText = currencyName + ": " + pointTotal;
		
		// We must use a handler since we cannot update UI elements from a different thread.
		mHandler.post(mUpdateResults);
	}

	
	// Notifier for when spending virtual currency fails.
	public void getSpendPointsResponseFailed(String error)
	{
		Log.i("EASY_APP", "spendTapPoints error: " + error);
		
		update_text = true;
		displayText = "Spend Tap Points: " + error;
		
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

	@Override
	public void getAwardPointsResponse(String currencyName, int pointTotal)
	{
		update_text = true;
		displayText = currencyName + ": " + pointTotal;
		
		// We must use a handler since we cannot update UI elements from a different thread.
		mHandler.post(mUpdateResults);
	}

	@Override
	public void getAwardPointsResponseFailed(String error)
	{
		update_text = true;
		displayText = "Award Points: " + error;
		
		// We must use a handler since we cannot update UI elements from a different thread.
		mHandler.post(mUpdateResults);
	}

	@Override
	public void earnedTapPoints(int amount)
	{
		earnedPoints = true;
		update_text = true;
		displayText = "You've just earned " + amount + " Tap Points!";
		
		// We must use a handler since we cannot update UI elements from a different thread.
		mHandler.post(mUpdateResults);
	}

	@Override
	public void videoReady()
	{
		Log.i("EASY_APP", "VIDEO READY");
		
		update_text = true;
		displayText = "VIDEO READY!";
		
		// We must use a handler since we cannot update UI elements from a different thread.
		mHandler.post(mUpdateResults);
	}

	@Override
	public void videoError(int statusCode)
	{
		Log.i("EASY_APP", "VIDEO ERROR: " + statusCode);
		
		switch (statusCode)
		{
			case TapjoyVideoStatus.STATUS_MEDIA_STORAGE_UNAVAILABLE:
				displayText = "VIDEO ERROR: No SD card or external media storage mounted on device";
				break;
			case TapjoyVideoStatus.STATUS_NETWORK_ERROR_ON_INIT_VIDEOS:
				displayText = "VIDEO ERROR: Network error on init videos";
				break;
			case TapjoyVideoStatus.STATUS_UNABLE_TO_PLAY_VIDEO:
				displayText = "VIDEO ERROR: Error playing video";
				break;
		}
		
		update_text = true;
		mHandler.post(mUpdateResults);
	}

	@Override
	public void videoComplete()
	{
		Log.i("EASY_APP", "VIDEO COMPLETE");
	}
}
