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
			request.setCharacterEncoding("UTF-8"); // �]�w�B�z�ШD�Ѽƪ��ѽX�榡
			response.setContentType("text/html;charset=UTF-8"); // �]�wContent-Type�r�q��
			PrintWriter out = response.getWriter();
		
			// �U�����{���X�}�l�ϥ�Commons-UploadFile����B�z�W�Ǫ��ɮ׸��
			FileItemFactory factory = new DiskFileItemFactory(); // �إ�FileItemFactory����
			ServletFileUpload upload = new ServletFileUpload(factory);
			// ���R�ШD�A�ño��W���ɮת�FileItem����
			List<FileItem> items = upload.parseRequest(request);
			// �qweb.xml�ɮפ����ѼƤ��o��W���ɮת����|
			String uploadPath = "d:\\upload\\";
			File file = new File(uploadPath);
			if (!file.exists())
			{
				file.mkdir();
			}
			String filename = ""; // �W���ɮ��x�s����A�����ɦW
			InputStream is = null; // �ثe�W���ɮת�InputStream����
			// �`���B�z�W���ɮ�
			for (FileItem item : items)
			{
				// �B�z���q������
				if (item.isFormField())
				{
					if (item.getFieldName().equals("filename"))
					{
						// �Y�G�s�ɮפ����šA�N���x�s�bfilename��
						if (!item.getString().equals(""))
							filename = item.getString("UTF-8");
					}
				}
				// �B�z�W���ɮ�
				else if (item.getName() != null && !item.getName().equals(""))
				{
					// �q�Τ�ݶǰe�L�Ӫ��W���ɮ׸��|���I���ɦW
					filename = item.getName().substring(
							item.getName().lastIndexOf("\\") + 1);
					is = item.getInputStream(); // �o��W���ɮת�InputStream����
				}
			}
			// �N���|�M�W���ɦW�s�զX�����㪺�A�Ⱥݸ��|
			filename = uploadPath + filename;
			// �Y�G���A���w�g�s�b�M�W���ɮצW�٬ۦP���ɮסA�h��X���ܰT��
			if (new File(filename).exists())
			{
				new File(filename).delete();
			}
			// �}�l�W���ɮ�
			if (!filename.equals(""))
			{
				// ��FileOutputStream�}�ҪA�Ⱥݪ��W���ɮ�
				FileOutputStream fos = new FileOutputStream(filename);
				byte[] buffer = new byte[8192]; // �C��Ū8K�줸��
				int count = 0;
				// �}�lŪ���W���ɮת��줸�աA�ñN���X��A�Ⱥݪ��W���ɮ׿�X�y��
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count); // �V�A�Ⱥ��ɮ׼g�J�줸�լy
					
				}
				fos.close(); // ����FileOutputStream����
				is.close(); // InputStream����
				out.println("�ɮפW�Ǧ��\!");
				
			}
		}
		catch (Exception e)
		{

		}
	}
}
