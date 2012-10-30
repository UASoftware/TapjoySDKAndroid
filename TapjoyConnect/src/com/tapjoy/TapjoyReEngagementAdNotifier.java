// //
//Copyright (C) 2012 by Tapjoy Inc.
//
//This file is part of the Tapjoy SDK.
//
//By using the Tapjoy SDK in your software, you agree to the terms of the Tapjoy SDK License Agreement.
//
//The Tapjoy SDK is bound by the Tapjoy SDK License Agreement can be found here: https://www.tapjoy.com/sdk/license


package com.tapjoy;

/**
 * 
 * Notifier class which sends callbacks whenever receiving Re-engagement data/response.
 *
 */
public interface TapjoyReEngagementAdNotifier
{
	/**
	 * Callback when there is an available re-engagement campaign.
	 */
	public void getReEngagementAdResponse();
	
	
	/**
	 * Callback when there is no available re-engagement data returned from the server. 
	 * @param error						Error code.
	 */
	public void getReEngagementAdResponseFailed(int error);
}