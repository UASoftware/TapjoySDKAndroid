// //
//Copyright (C) 2012 by Tapjoy Inc.
//
//This file is part of the Tapjoy SDK.
//
//By using the Tapjoy SDK in your software, you agree to the terms of the Tapjoy SDK License Agreement.
//
//The Tapjoy SDK is bound by the Tapjoy SDK License Agreement can be found here: https://www.tapjoy.com/sdk/license


package com.tapjoy;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class TapjoyReEngagementAdWebView extends Activity
{
	private WebView webView = null;
	private ProgressBar progressBar;

	private String htmlRawData = "";
	
	final String TAPJOY_RE_ENGAGEMENT_AD = "Re-engagement Ad";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		// Get the full screen ad URL from the extras.
		Bundle extras = getIntent().getExtras();

		htmlRawData = extras.getString(TapjoyConstants.EXTRA_RE_ENGAGEMENT_HTML_DATA);
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		RelativeLayout layout = new RelativeLayout(this);
		
		// Load and display the webView.
		webView = new WebView(this);
		webView.setWebViewClient(new TapjoyWebViewClient());
		
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		// Create our progress spinner.
		progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
		progressBar.setVisibility(View.VISIBLE);
		
		// Center progress bar.
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(layoutParams);
        
        // Add relative layout to our content view.
        layout.addView(webView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);		
		layout.addView(progressBar);
		setContentView(layout);
		
		//webView.loadUrl(fullScreenAdURL);
		webView.loadDataWithBaseURL(TapjoyConstants.TJC_SERVICE_URL, htmlRawData, "text/html", "utf-8", null);
		
		TapjoyLog.i(TAPJOY_RE_ENGAGEMENT_AD, "Opening Re-engagement ad = [" + htmlRawData + "]");
	}
	

	/**
	 * Handles screen rotation.
	 */
	public void onConfigurationChanged (Configuration newConfig)
	{		
		super.onConfigurationChanged(newConfig);
		
		if (webView != null)
		{
			// Calling javascript "window.onorientationchange();" does not work here.
			// Delaying and then calling it does work so launch a task to do this for us.
			RefreshTask refreshTask = new RefreshTask();
			refreshTask.execute();
		}	
	}

	
	/**
	 * 
	 * This task will delay for a brief amount of time and then call
	 * javascript function "window.onorientationchange();" because
	 * calling this within the onConfigurationChanged method does
	 * not work correctly.
	 *
	 */
	private class RefreshTask extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params)
		{
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			return true;
		}
		
		protected void onPostExecute(Boolean result)
		{
			if (webView != null)
			{
				// This javascript function will resize the page based on the window dimensions.
				webView.loadUrl("javascript:window.onorientationchange();");
			}
		}
	}
	
	
	/**
	 * This class handle all events of the webview.
	 */
	private class TapjoyWebViewClient extends WebViewClient
	{
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			progressBar.setVisibility(View.VISIBLE);
			progressBar.bringToFront();
		}
		
		public void onPageFinished(WebView view, String url)
		{
			// Dismiss the progress wheel once the page is done loading.
			progressBar.setVisibility(View.GONE);
		}
		
		/**
		 * When any user hits any url from tapjoy custom view then this function is called before opening any user.
		 */
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			TapjoyLog.i(TAPJOY_RE_ENGAGEMENT_AD, "URL = ["+url+"]");
			
			// This is the "OK" button, terminate this activity.
			if (url.startsWith(TapjoyConstants.TJC_REENGAGEMENT_DISMISS_URL))
			{
				TapjoyLog.i(TAPJOY_RE_ENGAGEMENT_AD, "dismiss");
				finishActivity();
			}
			
			return true;
		}
	}
	
	
	/**
	 * Allows access to the webview to close this activity (when SKIP is pressed on Re-engagement ad page).
	 */
	private void finishActivity()
	{
		this.finish();
	}
}
