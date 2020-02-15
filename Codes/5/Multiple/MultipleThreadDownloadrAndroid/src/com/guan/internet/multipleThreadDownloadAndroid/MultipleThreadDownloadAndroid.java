    package com.guan.internet.multipleThreadDownloadAndroid;  
    import java.io.File;  
    import android.app.Activity;  
    import android.os.Bundle;  
    import android.os.Environment;  
    import android.os.Handler;  
    import android.os.Message;  
    import android.view.View;  
    import android.widget.Button;  
    import android.widget.EditText;  
    import android.widget.ProgressBar;  
    import android.widget.TextView;  
    import android.widget.Toast;  
    import com.guan.internet.service.download.DownloadProgressListener;  
    import com.guan.internet.service.download.FileDownloader;  
    /**  
     * 主界面，負責下載界面的顯示、與使用者互動、響套用戶事件等  
     *  
     */  
    public class MultipleThreadDownloadAndroid
    extends Activity {     
        private static final int PROCESSING = 1;    //正在下載實時資料傳輸 Message標志  
        private static final int FAILURE = -1;      //下載失敗時的Message標志  
          
        private EditText pathText;                  //下載輸入文字框  
        private TextView resultView;                //現在進度顯示百分比文字框  
    private Button downloadButton;      //下載按鈕，可以觸發下載事件  
    private Button stopbutton;          //停止按鈕，可以停止下載  
    private ProgressBar progressBar;    //下載進度指示器，實時圖形化的顯示進度訊息  
    //hanlder物件的作用是用於向建立Hander物件所在的執行緒所綁定的訊息佇列傳送訊息並 處理訊息  
    private Handler handler = new UIHander();     
            
    private final class UIHander extends Handler{  
     /**  
    * 系統會自動呼叫的回調方法，用於處理訊息事件  
    * Mesaage一般會包括訊息的標志和訊息的內容以及訊息的處理器Handler  
    */  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case PROCESSING:        //下載時  
                int size = msg.getData().getInt("size");    //從訊息中取得已經下載的資料長度  
                progressBar.setProgress(size);  //設定進度指示器的進度  
                float num = (float)progressBar.getProgress() / (float)  
                progressBar.getMax();   //計算已經下載的百分比，此處需要轉為浮點數計算  
                int result = (int)(num * 100);  //把取得的浮點數計算結構轉化為整數  
                resultView.setText(result+ "%");    //把下載的百分比顯示在界面顯示控制項上  
                if(progressBar.getProgress() == progressBar.getMax()){  
                //當下載完成時  
                    Toast.makeText(getApplicationContext(), R.string.  
                    success, Toast.LENGTH_LONG).show(); //使用Toast技術提示使用者下載完成  
                }  
                break;  
     
            case -1:    //下載失敗時  
                Toast.makeText(getApplicationContext(), R.string.error,   
                Toast.LENGTH_LONG).show();  //提示使用者下載失敗  
                break;  
            }  
        }  
    }  
     
    @Override  
    public void onCreate(Bundle savedInstanceState) {   //套用程式啟動時會首先呼叫且在套用程式整個生命周期中只會呼叫一次，適合於起始化工作  
        super.onCreate(savedInstanceState); //使用父類別的onCreate用做螢幕主界面的底層和基本繪制工作  
        setContentView(R.layout.main);  //根據XML界面檔案設定主界面  
pathText = (EditText) this.findViewById(R.id.path);    //取得下載URL的文字輸入框物件  
    resultView = (TextView) this.findViewById(R.id.resultView);  
    //取得顯示下載百分比文字控制項物件  
    downloadButton = (Button) this.findViewById(R.id.downloadbutton);  
    //取得下載按鈕物件  
    stopbutton = (Button) this.findViewById(R.id.stopbutton);  
    //取得停止下載按鈕物件  
    progressBar = (ProgressBar) this.findViewById(R.id.progressBar);  
    //取得進度指示器物件  
    ButtonClickListener listener = new ButtonClickListener();  
    //宣告並定義按鈕監聽器物件  
    downloadButton.setOnClickListener(listener);//設定下載按鈕的監聽器物件  
    stopbutton.setOnClickListener(listener);//設定停止下載按鈕的監聽器物件  
}  
/**  
* 按鈕監聽器實現類別   
*  
*/  
private final class ButtonClickListener implements View.OnClickListener{  
    public void onClick(View v) {   //該方法在登錄了該按鈕監聽器的物件被單 擊時會自動呼叫，用於響應點選事件  
        switch (v.getId()) {    //取得點選物件的ID  
        case R.id.downloadbutton:   //當點選下載按鈕時  
            String path = pathText.getText().toString();//取得下載路徑  
            if(Environment.getExternalStorageState().equals  
            (Environment.MEDIA_MOUNTED)){   //取得SDCard是否存在，當SDCard存在時  
                File saveDir = Environment.getExternalStorageDirectory();    //取得SDCard根目錄檔案  
                File saveDir1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);   
               
                File saveDir11 =  getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);  
                download(path, saveDir11);    //下載檔案  
            }else{  //當SDCard不存在時  
                Toast.makeText(getApplicationContext(), R.string.  
                sdcarderror, Toast.LENGTH_LONG).show();//提示使用者SDCard不存在  
            }  
            downloadButton.setEnabled(false);   //設定下載按鈕不可用  
            stopbutton.setEnabled(true);    //設定停止下載按鈕可用  
            break;  
        case R.id.stopbutton:   //當點選停止下載按鈕時  
            exit(); //停止下載  
            downloadButton.setEnabled(true);    //設定下載按鈕可用    
            stopbutton.setEnabled(false);   //設定停止按鈕不可用  
            break;  
        }  
    }  
      
    ///////////////////////////////////////////////////////////////  
    //由於使用者的輸入事件(點選button,觸摸螢幕…)是由主軸程負責處理的，若果主軸程處於工作狀態  
    //此時使用者產生的輸入事件若果沒能在5秒內得到處理，系統就會報"套用無響應"錯誤  
    //所以在主軸程裡不能執行一件比較耗時的工作，否則會因主軸程阻塞而無法處理使用者的輸入事件  
    //導致"套用無響應"錯誤的出現，耗時的工作應該在子執行緒裡執行  
    ///////////////////////////////////////////////////////////////  
      
    private DownloadTask task;  //宣告下載執行者  
    /**  
     * 離開下載  
     */  
    public void exit(){  
        if(task!=null) task.exit(); //若果有下載物件時，離開下載  
    }  
    /**  
     * 下載資源，生命下載執行者並開闢執行緒開始現在  
     * @param path  下載的路徑  
     * @param saveDir   儲存檔案  
     */  
    private void download(String path, File saveDir){//此方法執行在主軸程  
        task = new DownloadTask(path, saveDir); //案例化下載工作  
        new Thread(task).start();   //開始下載  
    }  
      
      
    /*  
     * UI控制項畫面的重繪(更新)是由主軸程負責處理的，若果在子執行緒中更新UI控制項的  
    值，更新後的值不會重繪到螢幕上  
     * 一定要在主軸程裡更新UI控制項的值，這樣才能在螢幕上顯示出來，不能在子執行緒中  
    更新UI控制項的值  
     */  
    private final class DownloadTask implements Runnable{  
        private String path;    //下載路徑  
        private File saveDir;   //下載到儲存到的檔案  
        private FileDownloader loader;  //檔案下載器(下載執行緒的容器)  
        /**  
         * 建構方法，實現變數起始化  
         * @param path  下載路徑  
         * @param saveDir   下載要儲存到的檔案  
*/  
        public DownloadTask(String path, File saveDir) {      
            this.path = path;  
            this.saveDir = saveDir;  
        }  
          
        /**  
         * 離開下載  
         */  
        public void exit(){  
            if(loader!=null) loader.exit();//若果下載器存在的話則離開下載  
        }  
        DownloadProgressListener downloadProgressListener = new   
        DownloadProgressListener() {    //開始下載，並設定下載的監聽器  
               
            /**  
             * 下載的檔案長度會不斷的被傳入該回調方法  
             */  
            public void onDownloadSize(int size) {    
                Message msg = new Message(); //新增立一個Message物件  
                msg.what = PROCESSING;       //設定ID為1；  
                msg.getData().putInt("size", size); //把檔案下載的size設定進Message物件  
                handler.sendMessage(msg);//透過handler傳送訊息到訊息佇列  
            }  
        };  
        /**  
         * 下載執行緒的執行方法，會被系統自動呼叫  
         */  
        public void run() {  
            try {  
                loader = new FileDownloader(getApplicationContext(),   
                path, saveDir, 3);  //起始化下載  
                progressBar.setMax(loader.getFileSize());   //設定進度指示器的最大刻度  
                loader.download(downloadProgressListener);  
                  
            } catch (Exception e) {  
                e.printStackTrace();  
                handler.sendMessage(handler.obtainMessage(FAILURE));  
                //下載失敗時向訊息佇列傳送訊息  
                /*Message message = handler.obtainMessage();  
                message.what = FAILURE;*/  
            }  
        }             
    }  
}  
 
 
}  
