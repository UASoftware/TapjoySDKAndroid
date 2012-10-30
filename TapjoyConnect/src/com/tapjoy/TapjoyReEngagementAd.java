// //
//Copyright (C) 2012 by Tapjoy Inc.
//
//This file is part of the Tapjoy SDK.
//
//By using the Tapjoy SDK in your software, you agree to the terms of the Tapjoy SDK License Agreement.
//
//The Tapjoy SDK is bound by the Tapjoy SDK License Agreement can be found here: https://www.tapjoy.com/sdk/license


package com.tapjoy;

import android.content.Context;
import android.content.Intent;

public class TapjoyReEngagementAd
{
	private static TapjoyReEngagementAdNotifier reEngagementAdNotifier;
	private static TapjoyURLConnection tapjoyURLConnection = null;
	private Context context;
	
	public static String reEngagementAdURLParams;
	private String currencyID;
	private static String htmlData;
	
	final String TAPJOY_RE_ENGAGEMENT = "Re-engagement";
	
	
	/**
	 * Constructor.
	 */
	public TapjoyReEngagementAd(Context ctx)
	{	
		context = ctx;
		tapjoyURLConnection = new TapjoyURLConnection();
	}
	
	
	/**
	 * Connects to the server and to get Re-engagement ad data.
	 * @param notifier				Class which receives callback with Re-engagement data.
	 */
	public void getReEngagementAd(TapjoyReEngagementAdNotifier notifier)
	{
		TapjoyLog.i(TAPJOY_RE_ENGAGEMENT, "Getting Re-engagement Ad");
		
		getReEngagementAdWithCurrencyID(null, notifier);
	}
	
	
	/**
	 * Connects to the server and to get Re-engagement ad data.
	 * @param currencyID			ID of the currency to display.
	 * @param notifier				Class which receives callback with Re-engagement data.
	 */
	public void getReEngagementAdWithCurrencyID(String theCurrencyID, TapjoyReEngagementAdNotifier notifier)
	{
		// Store the currency ID.  We need to use use this if the offer wall is launched from the webView.
		currencyID = theCurrencyID;
		
		TapjoyLog.i(TAPJOY_RE_ENGAGEMENT, "Getting Re-engagement ad userID: " + TapjoyConnectCore.getUserID() + ", currencyID: " + currencyID);
		
		reEngagementAdNotifier = notifier;

		// Add the user ID to our request.
		reEngagementAdURLParams = TapjoyConnectCore.getURLParams();
		reEngagementAdURLParams += "&" + TapjoyConstants.TJC_USER_ID + "=" + TapjoyConnectCore.getUserID();
		
		// Add the currency ID if it was specified.
		if (currencyID != null)
			reEngagementAdURLParams += "&" + TapjoyConstants.TJC_CURRENCY_ID + "=" + currencyID;
		
		new Thread(new Runnable() 
		{
			public void run()
			{
				TapjoyHttpURLResponse httpResponse = tapjoyURLConnection.getResponseFromURL(TapjoyConstants.TJC_SERVICE_URL + TapjoyConstants.TJC_RE_ENGAGEMENT_AD_URL_PATH, reEngagementAdURLParams);
				
				if (httpResponse != null) 
				{
					switch (httpResponse.statusCode)
					{
						// 200
						case 200:
								// Save raw HTML data.
								htmlData = httpResponse.response;
								reEngagementAdNotifier.getReEngagementAdResponse();
							break;
						
						// No content
						case 204:
							reEngagementAdNotifier.getReEngagementAdResponseFailed(TapjoyReEngagementAdStatus.STATUS_NO_ADS_AVAILABLE);
							break;
					}
				}
				else
				{
					reEngagementAdNotifier.getReEngagementAdResponseFailed(TapjoyReEngagementAdStatus.STATUS_NETWORK_ERROR);
				}
			}
		}).start();
	}
	
	
	/**
	 * Displays the Re-engagement ad.
	 * Should be called after getReEngagementAd() and after receiving the TapjoyReEngagementAdNotifier.getReEngagementAdResponse callback. 
	 */
	public void showReEngagementFullScreenAd()
	{
		TapjoyLog.i(TAPJOY_RE_ENGAGEMENT, "Displaying Re-engagement ad...");
		
		if (htmlData != null && htmlData.length() != 0)
		{
			Intent intent = new Intent(context, com.tapjoy.TapjoyReEngagementAdWebView.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(TapjoyConstants.EXTRA_RE_ENGAGEMENT_HTML_DATA, htmlData);
			context.startActivity(intent);
		}
	}
}
