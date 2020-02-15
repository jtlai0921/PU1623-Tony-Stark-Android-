package irdc.httpSHI;


/*���ݰѦ�apache.http���p���O?�ӫإ�HTTP�p��*/
import org.apache.http.HttpResponse; 
import org.apache.http.NameValuePair; 
import org.apache.http.client.ClientProtocolException; 
import org.apache.http.client.entity.UrlEncodedFormEntity; 
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost; 
import org.apache.http.impl.client.DefaultHttpClient; 
import org.apache.http.message.BasicNameValuePair; 
import org.apache.http.protocol.HTTP; 
import org.apache.http.util.EntityUtils; 
/*���ݰѦ�java.io �Pjava.util���p���O?��Ū�g�ɮ�*/
import irdc.httpSHI.R;

import java.io.IOException; 
import java.util.ArrayList; 
import java.util.List; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 
import android.widget.TextView; 

public class httpSHI extends Activity 
{ 
  /*�Ţ����Button����,�P�X��TextView����*/
  private Button mButton1,mButton2; 
  private TextView mTextView1; 
   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
     
    /*�z�LfindViewById�غc�x�إ�TextView�PButton����*/ 
    mButton1 =(Button) findViewById(R.id.myButton1); 
    mButton2 =(Button) findViewById(R.id.myButton2);
    mTextView1 = (TextView) findViewById(R.id.myTextView1); 
     
    /*�]�wOnClickListener�Ӳ�ťOnClick�ƥ�*/
    mButton1.setOnClickListener(new Button.OnClickListener() 
    { 
      /*�мgonClick�ƥ�*/
      @Override 
      public void onClick(View v) 
      { 
        /*�Ţ�����?��*/
        String uriAPI = "http://www.dubblogs.cc:8751/Android/Test/API/Post/index.php";
        /*�إ�HTTP Post�p��*/
        HttpPost httpRequest = new HttpPost(uriAPI); 
        /*
         * Post�B�z�ǿ��ܼƥ�����NameValuePair[]�}�b�x?
        */
        List <NameValuePair> params = new ArrayList <NameValuePair>(); 
        params.add(new BasicNameValuePair("str", "I am Post String")); 
        try 
        { 
          /*�o�oHTTP request*/
          httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
          /*���oHTTP response*/
          HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
          /*�Y���A�X��200*/
          if(httpResponse.getStatusLine().getStatusCode() == 200)  
          { 
            /*���o�r��*/
            String strResult = EntityUtils.toString(httpResponse.getEntity()); 
            mTextView1.setText(strResult); 
          } 
          else 
          { 
            mTextView1.setText("Error Response: "+httpResponse.getStatusLine().toString()); 
          } 
        } 
        catch (ClientProtocolException e) 
        {  
          mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace(); 
        } 
        catch (IOException e) 
        {  
          mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace(); 
        } 
        catch (Exception e) 
        {  
          mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace();  
        }  
         
      } 
    }); 
    mButton2.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        String uriAPI = "http://www.126.com/str=I+am+Get+String"; 
        /*�إ�HTTP Get�p��*/
        HttpGet httpRequest = new HttpGet(uriAPI); 
        try 
        { 
          /*�ǰe���o��HTTP request*/
          HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
          /*�Y���A�X��200*/
          if(httpResponse.getStatusLine().getStatusCode() == 200)  
          { 
            /*���o����?��*/
            String strResult = EntityUtils.toString(httpResponse.getEntity());
            strResult = eregi_replace("(\r\n|\r|\n|\n\r)","",strResult);
            mTextView1.setText(strResult); 
          } 
          else 
          { 
            mTextView1.setText("Error Response: "+httpResponse.getStatusLine().toString()); 
          } 
        } 
        catch (ClientProtocolException e) 
        {  
          mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace(); 
        } 
        catch (IOException e) 
        {  
          mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace(); 
        } 
        catch (Exception e) 
        {  
          mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace();  
        }  
      } 
    }); 
  }
    public String eregi_replace(String strFrom, String strTo, String strTarget)
    {
      String strPattern = "(?i)"+strFrom;
      Pattern p = Pattern.compile(strPattern);
      Matcher m = p.matcher(strTarget);
      if(m.find())
      {
        return strTarget.replaceAll(strFrom, strTo);
      }
      else
      {
        return strTarget;
      }
    }
} 
