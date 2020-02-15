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
     * �D�ɭ��A�t�d�U���ɭ�����ܡB�P�ϥΪ̤��ʡB�T�M�Τ�ƥ�  
     *  
     */  
    public class MultipleThreadDownloadAndroid
    extends Activity {     
        private static final int PROCESSING = 1;    //���b�U����ɸ�ƶǿ� Message�Ч�  
        private static final int FAILURE = -1;      //�U�����Ѯɪ�Message�Ч�  
          
        private EditText pathText;                  //�U����J��r��  
        private TextView resultView;                //�{�b�i����ܦʤ����r��  
    private Button downloadButton;      //�U�����s�A�i�HĲ�o�U���ƥ�  
    private Button stopbutton;          //������s�A�i�H����U��  
    private ProgressBar progressBar;    //�U���i�׫��ܾ��A��ɹϧΤƪ���ܶi�װT��  
    //hanlder���󪺧@�άO�Ω�V�إ�Hander����Ҧb��������Ҹj�w���T����C�ǰe�T���� �B�z�T��  
    private Handler handler = new UIHander();     
            
    private final class UIHander extends Handler{  
     /**  
    * �t�η|�۰ʩI�s���^�դ�k�A�Ω�B�z�T���ƥ�  
    * Mesaage�@��|�]�A�T�����ЧөM�T�������e�H�ΰT�����B�z��Handler  
    */  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case PROCESSING:        //�U����  
                int size = msg.getData().getInt("size");    //�q�T�������o�w�g�U������ƪ���  
                progressBar.setProgress(size);  //�]�w�i�׫��ܾ����i��  
                float num = (float)progressBar.getProgress() / (float)  
                progressBar.getMax();   //�p��w�g�U�����ʤ���A���B�ݭn�ର�B�I�ƭp��  
                int result = (int)(num * 100);  //����o���B�I�ƭp�⵲�c��Ƭ����  
                resultView.setText(result+ "%");    //��U�����ʤ�����ܦb�ɭ���ܱ���W  
                if(progressBar.getProgress() == progressBar.getMax()){  
                //��U��������  
                    Toast.makeText(getApplicationContext(), R.string.  
                    success, Toast.LENGTH_LONG).show(); //�ϥ�Toast�޳N���ܨϥΪ̤U������  
                }  
                break;  
     
            case -1:    //�U�����Ѯ�  
                Toast.makeText(getApplicationContext(), R.string.error,   
                Toast.LENGTH_LONG).show();  //���ܨϥΪ̤U������  
                break;  
            }  
        }  
    }  
     
    @Override  
    public void onCreate(Bundle savedInstanceState) {   //�M�ε{���Ұʮɷ|�����I�s�B�b�M�ε{����ӥͩR�P�����u�|�I�s�@���A�A�X��_�l�Ƥu�@  
        super.onCreate(savedInstanceState); //�ϥΤ����O��onCreate�ΰ��ù��D�ɭ������h�M��ø��u�@  
        setContentView(R.layout.main);  //�ھ�XML�ɭ��ɮ׳]�w�D�ɭ�  
pathText = (EditText) this.findViewById(R.id.path);    //���o�U��URL����r��J�ت���  
    resultView = (TextView) this.findViewById(R.id.resultView);  
    //���o��ܤU���ʤ����r�������  
    downloadButton = (Button) this.findViewById(R.id.downloadbutton);  
    //���o�U�����s����  
    stopbutton = (Button) this.findViewById(R.id.stopbutton);  
    //���o����U�����s����  
    progressBar = (ProgressBar) this.findViewById(R.id.progressBar);  
    //���o�i�׫��ܾ�����  
    ButtonClickListener listener = new ButtonClickListener();  
    //�ŧi�éw�q���s��ť������  
    downloadButton.setOnClickListener(listener);//�]�w�U�����s����ť������  
    stopbutton.setOnClickListener(listener);//�]�w����U�����s����ť������  
}  
/**  
* ���s��ť����{���O   
*  
*/  
private final class ButtonClickListener implements View.OnClickListener{  
    public void onClick(View v) {   //�Ӥ�k�b�n���F�ӫ��s��ť��������Q�� ���ɷ|�۰ʩI�s�A�Ω��T���I��ƥ�  
        switch (v.getId()) {    //���o�I�磌��ID  
        case R.id.downloadbutton:   //���I��U�����s��  
            String path = pathText.getText().toString();//���o�U�����|  
            if(Environment.getExternalStorageState().equals  
            (Environment.MEDIA_MOUNTED)){   //���oSDCard�O�_�s�b�A��SDCard�s�b��  
                File saveDir = Environment.getExternalStorageDirectory();    //���oSDCard�ڥؿ��ɮ�  
                File saveDir1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);   
               
                File saveDir11 =  getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);  
                download(path, saveDir11);    //�U���ɮ�  
            }else{  //��SDCard���s�b��  
                Toast.makeText(getApplicationContext(), R.string.  
                sdcarderror, Toast.LENGTH_LONG).show();//���ܨϥΪ�SDCard���s�b  
            }  
            downloadButton.setEnabled(false);   //�]�w�U�����s���i��  
            stopbutton.setEnabled(true);    //�]�w����U�����s�i��  
            break;  
        case R.id.stopbutton:   //���I�ﰱ��U�����s��  
            exit(); //����U��  
            downloadButton.setEnabled(true);    //�]�w�U�����s�i��    
            stopbutton.setEnabled(false);   //�]�w������s���i��  
            break;  
        }  
    }  
      
    ///////////////////////////////////////////////////////////////  
    //�ѩ�ϥΪ̪���J�ƥ�(�I��button,Ĳ�N�ù��K)�O�ѥD�b�{�t�d�B�z���A�Y�G�D�b�{�B��u�@���A  
    //���ɨϥΪ̲��ͪ���J�ƥ�Y�G�S��b5���o��B�z�A�t�δN�|��"�M�εL�T��"���~  
    //�ҥH�b�D�b�{�̤������@�����Ӯɪ��u�@�A�_�h�|�]�D�b�{����ӵL�k�B�z�ϥΪ̪���J�ƥ�  
    //�ɭP"�M�εL�T��"���~���X�{�A�Ӯɪ��u�@���Ӧb�l������̰���  
    ///////////////////////////////////////////////////////////////  
      
    private DownloadTask task;  //�ŧi�U�������  
    /**  
     * ���}�U��  
     */  
    public void exit(){  
        if(task!=null) task.exit(); //�Y�G���U������ɡA���}�U��  
    }  
    /**  
     * �U���귽�A�ͩR�U������̨ö}�P������}�l�{�b  
     * @param path  �U�������|  
     * @param saveDir   �x�s�ɮ�  
     */  
    private void download(String path, File saveDir){//����k����b�D�b�{  
        task = new DownloadTask(path, saveDir); //�רҤƤU���u�@  
        new Thread(task).start();   //�}�l�U��  
    }  
      
      
    /*  
     * UI����e������ø(��s)�O�ѥD�b�{�t�d�B�z���A�Y�G�b�l���������sUI�����  
    �ȡA��s�᪺�Ȥ��|��ø��ù��W  
     * �@�w�n�b�D�b�{�̧�sUI������ȡA�o�ˤ~��b�ù��W��ܥX�ӡA����b�l�������  
    ��sUI�������  
     */  
    private final class DownloadTask implements Runnable{  
        private String path;    //�U�����|  
        private File saveDir;   //�U�����x�s�쪺�ɮ�  
        private FileDownloader loader;  //�ɮפU����(�U����������e��)  
        /**  
         * �غc��k�A��{�ܼư_�l��  
         * @param path  �U�����|  
         * @param saveDir   �U���n�x�s�쪺�ɮ�  
*/  
        public DownloadTask(String path, File saveDir) {      
            this.path = path;  
            this.saveDir = saveDir;  
        }  
          
        /**  
         * ���}�U��  
         */  
        public void exit(){  
            if(loader!=null) loader.exit();//�Y�G�U�����s�b���ܫh���}�U��  
        }  
        DownloadProgressListener downloadProgressListener = new   
        DownloadProgressListener() {    //�}�l�U���A�ó]�w�U������ť��  
               
            /**  
             * �U�����ɮת��׷|���_���Q�ǤJ�Ӧ^�դ�k  
             */  
            public void onDownloadSize(int size) {    
                Message msg = new Message(); //�s�W�ߤ@��Message����  
                msg.what = PROCESSING;       //�]�wID��1�F  
                msg.getData().putInt("size", size); //���ɮפU����size�]�w�iMessage����  
                handler.sendMessage(msg);//�z�Lhandler�ǰe�T����T����C  
            }  
        };  
        /**  
         * �U��������������k�A�|�Q�t�Φ۰ʩI�s  
         */  
        public void run() {  
            try {  
                loader = new FileDownloader(getApplicationContext(),   
                path, saveDir, 3);  //�_�l�ƤU��  
                progressBar.setMax(loader.getFileSize());   //�]�w�i�׫��ܾ����̤j���  
                loader.download(downloadProgressListener);  
                  
            } catch (Exception e) {  
                e.printStackTrace();  
                handler.sendMessage(handler.obtainMessage(FAILURE));  
                //�U�����ѮɦV�T����C�ǰe�T��  
                /*Message message = handler.obtainMessage();  
                message.what = FAILURE;*/  
            }  
        }             
    }  
}  
 
 
}  
