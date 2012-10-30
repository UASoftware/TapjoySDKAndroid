package com.tapjoy;

/**
 * Status codes used in the callbacks for TapjoyReEngagementAdNotifier::getReEngagementAdResponseFailed.
 */
public class TapjoyReEngagementAdStatus
{
	/**
	 * No re-engagement ads are available.
	 */
	public static final int STATUS_NO_ADS_AVAILABLE						= 1;
	
	
	/**
	 * Network error occurred while trying to retrieve a re-engagement ad.
	 */
	public static final int STATUS_NETWORK_ERROR						= 2;
	
	
	/**
	 * Error parsing server response.
	 */
	public static final int STATUS_SERVER_ERROR							= 3;
}