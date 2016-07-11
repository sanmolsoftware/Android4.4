package com.ulta.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.ulta.core.util.caching.UltaDataCache;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;

public class ImageDownloader extends AsyncTask<Void, Integer, Bitmap> {
	private String url;
	private Bitmap bmp;
	private String from;
	private int position;
	
	private ImageDownloadListener imageDownloadListener;
	
	public interface ImageDownloadListener{
	
		public void onImageDownload();
	}
	
	
	/*--- constructor ---*/
	public ImageDownloader(String url, Context c, Bitmap bmp, String from, int position) {
		/*--- we need to pass some objects we are going to work with ---*/
		this.url = url;
		this.bmp = bmp;
		this.from = from;
		this.position = position;
		imageDownloadListener = (ImageDownloadListener) c;
	}

	@Override
	protected Bitmap doInBackground(Void... arg0) {

		bmp = getBitmapFromURL(url);
		return bmp;

	}

	@Override
	protected void onPostExecute(Bitmap result) {

		super.onPostExecute(result);
		if (from.equalsIgnoreCase("prodDetails")) {
			UltaDataCache.getDataCacheInstance().setPdpHashMapOfImages(position,result);
		} else {
			UltaDataCache.getDataCacheInstance().setPlpHashMapOfImages(position, result);
		}

	}

	@SuppressWarnings("deprecation")
	public static Bitmap getBitmapFromURL(String link) {
		
		try {
			HttpGet httpRequest = new HttpGet(URI.create(link) );
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			Bitmap myBitmap = BitmapFactory.decodeStream(bufHttpEntity.getContent());
			httpRequest.abort();

			return myBitmap;

		} catch (IOException e) {
			e.printStackTrace();
			Log.e("getBmpFromUrl error: ", e.getMessage().toString());
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			Log.e("getBmpFromUrl error: ", e.getMessage().toString());
			return null;
		}
		
		
	}

}
