package com.ojk;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Web extends Activity {

	private ProgressBar progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		String url = "";
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c70100")));
        
		int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        
		if (actionBarTitleId > 0) {
            TextView title = (TextView) findViewById(actionBarTitleId);
            if (title != null) {
                title.setTextColor(Color.WHITE);
            }
        }
		
		SharedPreferences settings = getSharedPreferences("bahasa",
				MODE_PRIVATE);
		String bahasanya = settings.getString("bahasanya", "ID");
		
		if (getIntent().getExtras().containsKey("POSITION")) {
			String dummy = getIntent().getStringExtra("POSITION");
			String dummySplit[] = dummy.split(",");
			int value = Integer.parseInt(dummySplit[0]);
			setTitle(dummySplit[1]);
			if (bahasanya.equals("EN")) {
				if (value == 0) {
					url = "http://portalojk.dev.altrovis.com/en/tentang-ojk/Pages/Visi-Misi.aspx?mobile=1";
				} else if (value == 1) {
					url = "http://portalojk.dev.altrovis.com/en/tentang-ojk/Pages/Tugas-dan-Fungsi.aspx?mobile=1";
				} else if (value == 2) {
					url = "http://portalojk.dev.altrovis.com/en/tentang-ojk/Pages/Dewan-Komisioner.aspx?mobile=1";
				} else if (value == 3) {
					url = "http://portalojk.dev.altrovis.com/en/tentang-ojk/Pages/Nilai-Nilai.aspx?mobile=1";
				} else if (value == 4) {
					url = "http://portalojk.dev.altrovis.com/en/tentang-ojk/Pages/Struktur-Organisasi.aspx?mobile=1";
				} else if (value == 5) {
					url = "http://portalojk.dev.altrovis.com/en/tentang-ojk/Pages/Kode-Etik-Pegawai.aspx?mobile=1";
				}
			} else {
				if (value == 0) {
					url = "http://portalojk.dev.altrovis.com/id/tentang-ojk/Pages/Visi-Misi.aspx?mobile=1";
				} else if (value == 1) {
					url = "http://portalojk.dev.altrovis.com/id/tentang-ojk/Pages/Tugas-dan-Fungsi.aspx?mobile=1";
				} else if (value == 2) {
					url = "http://portalojk.dev.altrovis.com/id/tentang-ojk/Pages/Dewan-Komisioner.aspx?mobile=1";
				} else if (value == 3) {
					url = "http://portalojk.dev.altrovis.com/id/tentang-ojk/Pages/Nilai-Nilai.aspx?mobile=1";
				} else if (value == 4) {
					url = "http://portalojk.dev.altrovis.com/id/tentang-ojk/Pages/Struktur-Organisasi.aspx?mobile=1";
				} else if (value == 5) {
					url = "http://portalojk.dev.altrovis.com/id/tentang-ojk/Pages/Kode-Etik-Pegawai.aspx?mobile=1";
				}
			}
		} else if (getIntent().getExtras().containsKey("FromOJKTerbaruURL")) {
			String extra = getIntent().getStringExtra("FromOJKTerbaruURL");
			String[] splitExtra = extra.split(",");
			String title = "Data dan Statistik";
			Log.d("tipe", splitExtra[0]);
			if (splitExtra[0].contains("berita")) {
				title = "Berita dan Kegiatan";
			}
			this.setTitle(title);
			url = splitExtra[1];
		}
		
		WebView webView = (WebView) findViewById(R.id.webview);
		webView.setWebChromeClient(new MyWebViewClient());

		progress = (ProgressBar) findViewById(R.id.progressBar);
		progress.setMax(100);
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportMultipleWindows(true); // This forces ChromeClient enabled.    


		webView.setWebViewClient(new WebViewClient() {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        view.loadUrl(url);
		        return false;
		    }
		});
		
		webView.loadUrl(url);
		Web.this.progress.setProgress(0);
	}
	
	private class MyWebViewClient extends WebChromeClient {	
		@Override
		public void onProgressChanged(WebView view, int newProgress) {			
			Web.this.setValue(newProgress);
			if (newProgress == 100) {
				progress.setVisibility(ProgressBar.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}
		
		@Override
	    public void onReceivedTitle(WebView view, String title) {
//	         getWindow().setTitle(title); //Set Activity tile to page title.
	    }
	}
	
	public void setValue(int progress) {
		this.progress.setProgress(progress);		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		// return true;
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		// return super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			//overridePendingTransition(R.anim.ltor, R.anim.rtol);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
