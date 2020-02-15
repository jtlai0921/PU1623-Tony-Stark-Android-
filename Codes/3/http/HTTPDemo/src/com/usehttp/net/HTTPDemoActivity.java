package com.usehttp.net;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.usehttp.net.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HTTPDemoActivity extends Activity {

	private Button getBtn, postBtn, imageBtn;
	private EditText urlText, imageUrlText;
	private TextView resutlView;
	private ImageView imageView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		urlText = (EditText) findViewById(R.id.urlText);
		imageUrlText = (EditText) findViewById(R.id.imageurlText);

		resutlView = (TextView) findViewById(R.id.resultView);
		getBtn = (Button) findViewById(R.id.getBtn);
		postBtn = (Button) findViewById(R.id.postBtn);
		imageBtn = (Button) findViewById(R.id.imgBtn);
		imageView = (ImageView) findViewById(R.id.imgeView01);

		getBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println(urlText.getText().toString());
				resutlView
						.setText(request("GET", urlText.getText().toString()));
			}
		});
		
		postBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				System.out.println(urlText.getText().toString());
				resutlView
						.setText(request("POST", urlText.getText().toString()));
			}
		});
		imageBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				getImage(imageUrlText.getText().toString());
			}
		});
	}
	private String request(String method, String url) {
		HttpResponse httpResponse = null;
		StringBuffer result = new StringBuffer();
		try {
			if (method.equals("GET")) {
				// 1.�z�Lurl�إ�HttpGet����
				HttpGet httpGet = new HttpGet(url);
				// 2.�z�LDefaultClient��excute��k����Ǧ^�@��HttpResponse����
				HttpClient httpClient = new DefaultHttpClient();
				httpResponse = httpClient.execute(httpGet);
				// 3.���o���p�T��
				// ���oHttpEntiy
				HttpEntity httpEntity = httpResponse.getEntity();
				// �o��@�Ǹ��
				// �z�LEntityUtils�ë��w�ѽX�Ҧ�����Ǧ^�����
				result.append(EntityUtils.toString(httpEntity, "utf-8"));
				//�o��StatusLine���f����
				StatusLine statusLine = httpResponse.getStatusLine();

				//�o���w
				;
				result.append("��w:" + statusLine.getProtocolVersion() + "\r\n");
				int statusCode = statusLine.getStatusCode();

				result.append("���A�X:" + statusCode + "\r\n");

			} else if (method.equals("POST")) {

				// 1.�z�Lurl�إ�HttpGet����
				HttpPost httpPost = new HttpPost(url);
				// 2.�z�LDefaultClient��excute��k����Ǧ^�@��HttpResponse����
				HttpClient httpClient = new DefaultHttpClient();
				httpResponse = httpClient.execute(httpPost);
				// 3.���o���p�T��
				// ���oHttpEntiy
				HttpEntity httpEntity = httpResponse.getEntity();
				// �o��@�Ǹ��
				// �z�LEntityUtils�ë��w�ѽX�Ҧ�����Ǧ^�����
				result.append(EntityUtils.toString(httpEntity, "utf-8"));
				StatusLine statusLine = httpResponse.getStatusLine();
				statusLine.getProtocolVersion();
				int statusCode = statusLine.getStatusCode();

				result.append("���A�X:" + statusCode + "\r\n");

			}
		} catch (Exception e) {
			Toast.makeText(HTTPDemoActivity.this, "�����s�u�ҥ~", Toast.LENGTH_LONG)
					.show();
		}
		return result.toString();
	}

	public void getImage(String url) {
		try {
			// 1.�z�Lurl�إ�HttpGet����
			HttpGet httpGet = new HttpGet(url);
			// 2.�z�LDefaultClient��excute��k����Ǧ^�@��HttpResponse����
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpGet);
			// 3.���o���p�T��
			// ���oHttpEntiy
			HttpEntity httpEntity = httpResponse.getEntity();
			// 4.�z�LHttpEntiy.getContent�o��@�ӿ�J�y
			InputStream inputStream = httpEntity.getContent();
			System.out.println(inputStream.available());
			
			//�z�L�ǤJ���y�A�z�LBitmap�u�t�إߤ@��Bitmap
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			//�]�wimageView		
			imageView.setImageBitmap(bitmap);
		} catch (Exception e) {
			Toast.makeText(HTTPDemoActivity.this, "�����s�u�ҥ~", Toast.LENGTH_LONG)
			.show();
		}
	}

}