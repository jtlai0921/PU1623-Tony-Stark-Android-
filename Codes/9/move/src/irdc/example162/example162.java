package irdc.example162;

import irdc.example162.R;
import android.app.Activity;
import android.os.Bundle;
/**/
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class example162 extends Activity
{
  /*�ŧiImageView�ܼ�*/
  private ImageView mImageView01;
  /*�ŧi���p�ܼƧ@���x�s�Ϥ����e,��m�ϥ�*/
  private int intWidth, intHeight, intDefaultX, intDefaultY;
  private float mX, mY; 
  /*�ŧi�x�s�ù����ѪR���ܼ� */
  private int intScreenX, intScreenY;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main);
    
    /* ���o�ù����� */
    DisplayMetrics dm = new DisplayMetrics(); 
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    
    /* ���o�ù��ѪR���� */
    intScreenX = dm.widthPixels;
    intScreenY = dm.heightPixels;
    
    /* �]�w�Ϥ������e */
    intWidth = 100;
    intHeight = 100;
    /*�z�LfindViewById�غc���إ�ImageView����*/ 
    mImageView01 =(ImageView) findViewById(R.id.myImageView1);
    /*�N�Ϥ��qDrawable�����ȵ�ImageView�ӧe�{*/
    mImageView01.setImageResource(R.drawable.baby);
    
    /* �_�l�ƫ��s��m�m�� */
    RestoreButton();
    
    /* ���I��ImageView�A�_��_�l��m */
    mImageView01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        RestoreButton();
      }
    });
  }
  
  /*�л\Ĳ���ƥ�*/
  @Override
  public boolean onTouchEvent(MotionEvent event) 
  {
    /*���o���Ĳ���ù�����m*/
    float x = event.getX();
    float y = event.getY();
    
    try
    {
      /*Ĳ���ƥ󪺳B�z*/
      switch (event.getAction()) 
      {
        /*�I���ù�*/
        case MotionEvent.ACTION_DOWN:
          picMove(x, y);
            break;
        /*�h����m*/
        case MotionEvent.ACTION_MOVE:
          picMove(x, y);
            break;
        /*���}�ù�*/
        case MotionEvent.ACTION_UP:
          picMove(x, y);  
            break;
      }
    }catch(Exception e)
      {
        e.printStackTrace();
      }
    return true;
  }
  /*�h���Ϥ�����k*/
  private void picMove(float x, float y)
  {
    /*�w�]�L�չϤ��P���Ъ��۹��m*/
    mX=x-(intWidth/2);
    mY=y-(intHeight/2);
    
    /*���Ϥ��W�L�ù������p�B�z*/
    /*����ù��V�k�W�L�ù�*/
    if((mX+intWidth)>intScreenX)
    {
      mX = intScreenX-intWidth;
    }
    /*����ù��V���W�L�ù�*/
    else if(mX<0)
    {
      mX = 0;
    }
    /*����ù��V�U�W�L�ù�*/
    else if ((mY+intHeight)>intScreenY)
    {
      mY=intScreenY-intHeight;
    }
    /*����ù��V�W�W�L�ù�*/
    else if (mY<0)
    {
      mY = 0;
    }
    /*�z�Llog ���˵��Ϥ���m*/
    Log.i("jay", Float.toString(mX)+","+Float.toString(mY));
    /* �HsetLayoutParams��k�A���s�w��Layout�W����m */
    mImageView01.setLayoutParams
    (
      new AbsoluteLayout.LayoutParams
      (intWidth,intHeight,(int) mX,(int)mY)
    );
  }
  
  /* �_��ImageView��m���ƥ�B�z */
  public void RestoreButton()
  {
    intDefaultX = ((intScreenX-intWidth)/2);
    intDefaultY = ((intScreenY-intHeight)/2);
    /*Toast�_���m�y��*/
    mMakeTextToast
    (
      "("+
      Integer.toString(intDefaultX)+
      ","+
      Integer.toString(intDefaultY)+")",true
    );
    
    /* �HsetLayoutParams��k�A���s�w��Layout�W����m */
    mImageView01.setLayoutParams
    (
      new AbsoluteLayout.LayoutParams
      (intWidth,intHeight,intDefaultX,intDefaultY)
    );
  }
  /*�ۭq�@�o�X�T������k*/
  public void mMakeTextToast(String str, boolean isLong)
  {
    if(isLong==true)
    {
      Toast.makeText(example162.this, str, Toast.LENGTH_LONG).show();
    }
    else
    {
      Toast.makeText(example162.this, str, Toast.LENGTH_SHORT).show();
    }
  }
}
