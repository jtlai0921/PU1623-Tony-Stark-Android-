package net.blogjava.mobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet
{
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			request.setCharacterEncoding("UTF-8"); // 設定處理請求參數的解碼格式
			response.setContentType("text/html;charset=UTF-8"); // 設定Content-Type字段值
			PrintWriter out = response.getWriter();
		
			// 下面的程式碼開始使用Commons-UploadFile元件處理上傳的檔案資料
			FileItemFactory factory = new DiskFileItemFactory(); // 建立FileItemFactory物件
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 分析請求，並得到上傳檔案的FileItem物件
			List<FileItem> items = upload.parseRequest(request);
			// 從web.xml檔案中的參數中得到上傳檔案的路徑
			String uploadPath = "d:\\upload\\";
			File file = new File(uploadPath);
			if (!file.exists())
			{
				file.mkdir();
			}
			String filename = ""; // 上傳檔案儲存到伺服器的檔名
			InputStream is = null; // 目前上傳檔案的InputStream物件
			// 循環處理上傳檔案
			for (FileItem item : items)
			{
				// 處理普通的表單域
				if (item.isFormField())
				{
					if (item.getFieldName().equals("filename"))
					{
						// 若果新檔案不為空，將其儲存在filename中
						if (!item.getString().equals(""))
							filename = item.getString("UTF-8");
					}
				}
				// 處理上傳檔案
				else if (item.getName() != null && !item.getName().equals(""))
				{
					// 從用戶端傳送過來的上傳檔案路徑中截取檔名
					filename = item.getName().substring(
							item.getName().lastIndexOf("\\") + 1);
					is = item.getInputStream(); // 得到上傳檔案的InputStream物件
				}
			}
			// 將路徑和上傳檔名群組合成完整的服務端路徑
			filename = uploadPath + filename;
			// 若果伺服器已經存在和上傳檔案名稱相同的檔案，則輸出提示訊息
			if (new File(filename).exists())
			{
				new File(filename).delete();
			}
			// 開始上傳檔案
			if (!filename.equals(""))
			{
				// 用FileOutputStream開啟服務端的上傳檔案
				FileOutputStream fos = new FileOutputStream(filename);
				byte[] buffer = new byte[8192]; // 每次讀8K位元組
				int count = 0;
				// 開始讀取上傳檔案的位元組，並將其輸出到服務端的上傳檔案輸出流中
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count); // 向服務端檔案寫入位元組流
					
				}
				fos.close(); // 關閉FileOutputStream物件
				is.close(); // InputStream物件
				out.println("檔案上傳成功!");
				
			}
		}
		catch (Exception e)
		{

		}
	}
}
