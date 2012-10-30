package com.tapjoy;

/**
 * Flags used in {@link TapjoyConnect#requestTapjoyConnect} to define special startup options.<br><br>
 * These flags can also be defined in the meta-data tags in AndroidManifest.xml.  They
 * must be put within the <application> tag and the format is:<br>
 * <meta-data android:name="tapjoy.<flag_name>" android:value="<value>" />
 */
public class TapjoyConnectFlag
{
	/**
	 * Sends the SHA2 hash of UDID/deviceID (IMEI/MEID/serial) in sha2_udid parameter instead of the udid parameter.<br>
	 * Use "true" as the flag value.<br>
	 * <b>CAN ONLY BE USED IN THE ADVERTISER/CONNECT SDK.</b>
	 */
	public static final String SHA_2_UDID				= "sha_2_udid";
	
	/**
	 * Sets the app to use a different market/app store other than Google Play.  If not set, Google Play will be used.<br>
	 * Use the store name as the flag value.<br>
	 */
	public static final String STORE_NAME				= "store_name";
	
	/**
	 * Prevents video offers from being shown in any ad units.  Will override any dashboard settings.<br>
	 * Use "true" as the flag value.
	 */
	public static final String DISABLE_VIDEO_OFFERS		= "disable_video_offers";
	
	
	//--------------------------------------------------------------------------------
	// Use these values when using the STORE_NAME flag.
	//--------------------------------------------------------------------------------
	public static final String STORE_GFAN				= "gfan";
	
	
	//--------------------------------------------------------------------------------
	// Array of possible values used to check Manifest meta-data tags.
	// Add new flags here.
	//--------------------------------------------------------------------------------
	public static final String [] FLAG_ARRAY = 
		{
			SHA_2_UDID,
			STORE_NAME,
			DISABLE_VIDEO_OFFERS
		};
	
	//--------------------------------------------------------------------------------
	// Array of possible STORE_NAME values.
	//--------------------------------------------------------------------------------
	public static final String [] STORE_ARRAY = 
		{
			STORE_GFAN
		};
}
