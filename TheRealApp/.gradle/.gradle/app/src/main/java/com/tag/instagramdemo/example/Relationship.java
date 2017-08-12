package com.tag.instagramdemo.example;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.Toast;
import com.tag.instagramdemo.R;
import java.util.HashMap;

import android.net.ParseException;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



import com.tag.instagramdemo.example.InstagramApp.OAuthAuthenticationListener;
import com.tag.instagramdemo.lazyload.ImageLoader;

import com.tag.instagramdemo.R;

public class Relationship extends Activity {
	private String url = "";
	private InstagramApp.OAuthAuthenticationListener mListener;
	private ListView lvRelationShipAllUser;
	private ArrayList<HashMap<String, String>> usersInfo = new ArrayList<HashMap<String, String>>();
	private Context context;
	private int WHAT_FINALIZE = 0;
	private static int WHAT_ERROR = 1;
	private ProgressDialog pd;
	private InstagramApp mApp;
	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (pd != null && pd.isShowing())
				pd.dismiss();
			if (msg.what == WHAT_FINALIZE) {
				setImageGridAdapter();
			} else {
				Toast.makeText(context, "Check your network.",
						Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	});
	public static final String TAG_DATA = "data";
	public static final String TAG_ID = "id";
	public static final String TAG_PROFILE_PICTURE = "profile_picture";
	public static final String TAG_USERNAME = "username";
	public static final String TAG_BIO = "bio";
	public static final String TAG_WEBSITE = "website";
	public static final String TAG_FULL_NAME = "full_name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webview = new WebView(this);
		CookieManager.getInstance().setAcceptCookie(true);
		setContentView(R.layout.relationship);
		lvRelationShipAllUser = (ListView) findViewById(R.id.lvRelationShip);
		url = getIntent().getStringExtra("userInfo");
		context = Relationship.this;
		getAllMediaImages();
		//connectOrDisconnectUser();
	}

	private void setImageGridAdapter() {
		lvRelationShipAllUser.setAdapter(new RelationShipAdapter(context,
				usersInfo));
	}
	public void setListener(InstagramApp.OAuthAuthenticationListener listener) {
		mListener = listener;
	}

	private void connectOrDisconnectUser() {

			final AlertDialog.Builder builder = new AlertDialog.Builder(
					Relationship.this);
			builder.setMessage("Disconnect from Instagram?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								//do something
								public void onClick(DialogInterface dialog,
													int id) {
									mApp.resetAccessToken();
									// btnConnect.setVisibility(View.VISIBLE);

									// tvSummary.setText("Not connected");
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									dialog.cancel();
								}
							});
			final AlertDialog alert = builder.create();
			alert.show();

	}
	private void getAllMediaImages() {
		pd = ProgressDialog.show(context, "", "Loading...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				int what = WHAT_FINALIZE;
				try {
					// URL url = new URL(mTokenUrl + "&code=" + code);
					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObject = jsonParser.getJSONFromUrlByGet(url);
					JSONArray data = jsonObject.getJSONArray(TAG_DATA);
					for (int data_i = 0; data_i < data.length(); data_i++) {
						HashMap<String, String> hashMap = new HashMap<String, String>();
						JSONObject data_obj = data.getJSONObject(data_i);
						String str_id = data_obj.getString(TAG_ID);

						hashMap.put(TAG_PROFILE_PICTURE,
								data_obj.getString(TAG_PROFILE_PICTURE));

						// String str_username =
						// data_obj.getString(TAG_USERNAME);
						//
						// String str_bio = data_obj.getString(TAG_BIO);
						//
						// String str_website = data_obj.getString(TAG_WEBSITE);
						//USERNAME
						hashMap.put(TAG_USERNAME,
							data_obj.getString(TAG_USERNAME));
						usersInfo.add(hashMap);
					}
					System.out.println("jsonObject::" + jsonObject);

				} catch (Exception exception) {
					exception.printStackTrace();
					what = WHAT_ERROR;
				}
				// pd.dismiss();
				//mListener.onSuccess();
				handler.sendEmptyMessage(what);
			}
		}).start();
	}
}
