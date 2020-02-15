package irdc.ex08_11;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/* import���pclass */

public class EX08_11 extends Activity
{
  /*
   * �ܼƫŧi filename�G�W�ǫ�b���A���W���ɦW�� uploadFile�G�n�W�Ǫ��ɮ׸��| actionUrl�G���A���W�������{�����|
   */

  private String uploadFile = "/sdcard/test.mp3";
  private String srcPath = "/sdcard/test.mp3";
  private String actionUrl = "http://192.168.1.21:9090/upload_file_service/upload.jsp";
  private TextView mText1;
  private TextView mText2;
  private Button mButton;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    mText1 = (TextView) findViewById(R.id.myText2);
    mText1.setText("�ɮ׸��|�G\n" + uploadFile);
    mText2 = (TextView) findViewById(R.id.myText3);
    mText2.setText("�W�Ǻ��}�G\n" + actionUrl);
    /* �]�wmButton��onClick�ƥ�B�z */
    mButton = (Button) findViewById(R.id.myButton);
    mButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        uploadFile();
      }
    });
  }

  /* �W���ɮצ�Server����k */
  private void uploadFile()
  {

    String uploadUrl = "http://192.168.1.21:9090/upload_file_service/UploadServlet";
    String end = "\r\n";
    String twoHyphens = "--";
    String boundary = "******";
    try
    {
      URL url = new URL(uploadUrl);
      HttpURLConnection httpURLConnection = (HttpURLConnection) url
          .openConnection();
      httpURLConnection.setDoInput(true);
      httpURLConnection.setDoOutput(true);
      httpURLConnection.setUseCaches(false);
      httpURLConnection.setRequestMethod("POST");
      httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
      httpURLConnection.setRequestProperty("Charset", "UTF-8");
      httpURLConnection.setRequestProperty("Content-Type",
          "multipart/form-data;boundary=" + boundary);

      DataOutputStream dos = new DataOutputStream(httpURLConnection
          .getOutputStream());
      dos.writeBytes(twoHyphens + boundary + end);
      dos
          .writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
              + srcPath.substring(srcPath.lastIndexOf("/") + 1)
              + "\"" + end);
      dos.writeBytes(end);

      FileInputStream fis = new FileInputStream(srcPath);
      byte[] buffer = new byte[8192]; // 8k
      int count = 0;
      while ((count = fis.read(buffer)) != -1)
      {
        dos.write(buffer, 0, count);

      }
      fis.close();

      dos.writeBytes(end);
      dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
      dos.flush();

      InputStream is = httpURLConnection.getInputStream();
      InputStreamReader isr = new InputStreamReader(is, "utf-8");
      BufferedReader br = new BufferedReader(isr);
      String result = br.readLine();

      Toast.makeText(this, result, Toast.LENGTH_LONG).show();
      dos.close();
      is.close();

    } catch (Exception e)
    {
      e.printStackTrace();
      setTitle(e.getMessage());
    }

  }

}
