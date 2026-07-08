package kr.co.sscm.common.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.sscm.common.exception.ApiException;

/**
 * @FileName FileUploadUtils.java
 * @comment file upload util
 * @author AJH
 */
@Component
public class FileUploadUtils {

	public static String absolute_path;

	public static String upload_path;

	public static String push_dir;

	public static String fitm_file_path;

	@Value("${push.upload.absolute_path}")
	public void setAbsolutePath(String path) {
		absolute_path = path;
	}

	@Value("${push.upload.path}")
	public void setUploadDir(String dir) {
		upload_path = dir;
	}

	@Value("${push.upload.dir}")
	public void setPushImgDirNm(String dir) {
		push_dir = dir;
	}

	@Value("${fitm_file_path}")
	public void setFitmFilePath(String path) {
		fitm_file_path = path;
	}

	public Map<String, Object> pushFileUpload(MultipartFile file) throws ApiException {

		String path = "";
		String pathWeb = "";
		String fName = "";
		String thumbPathWeb = "";

		Map<String, Object> imgMap = new HashMap<String, Object>();

		if (!file.isEmpty()) {

			path = absolute_path + File.separator;
			pathWeb = File.separator + upload_path + File.separator + push_dir + File.separator;

			try {
				File destinationDir = new File(path);
				FileUtils.forceMkdir(destinationDir);

				SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMddHHmmssSS");
				String namePrefix = sdt.format(Calendar.getInstance().getTime());

				String orgFileName = file.getOriginalFilename();
				int index = orgFileName.lastIndexOf(".");
				if (index != -1) {
					String fileExt = orgFileName.substring(index + 1);
					UUID uuid = UUID.randomUUID();
					fName = uuid.toString() + "_" + namePrefix + "." + fileExt;
					if (!fileExt.toLowerCase().equals("gif") && !fileExt.toLowerCase().equals("jpg")
							&& !fileExt.toLowerCase().equals("jpeg") && !fileExt.toLowerCase().equals("png")) {
						throw new ApiException("Only GIF, JPG, JPEG, PNG image files can be uploaded.");
					}
				} else {
					throw new ApiException("Invalid file extension.");
				}

				File toFile = new File(path + fName);
				file.transferTo(toFile);

				imgMap.put("imageUploadUrl", pathWeb + fName);
				imgMap.put("filePathNm", path);
				imgMap.put("originalNm", orgFileName);
				imgMap.put("uploadFileNm", fName);
				imgMap.put("fileSize", Long.valueOf(file.getSize()).intValue());
				imgMap.put("thumbUploadUrl", thumbPathWeb + "thumbnail." + fName);

			} catch (IOException e) {
				throw new ApiException(e.toString());
			}
		}
		return imgMap;
	}

	public static Map<String, Object> fileUpload(HttpServletRequest request) throws ApiException {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!ServletFileUpload.isMultipartContent(request)) {
			return resultMap;
		}

		if (!(request instanceof MultipartHttpServletRequest)) {
			throw new ApiException("Multipart request is required.");
		}

		if (fitm_file_path == null || fitm_file_path.trim().length() == 0) {
			throw new ApiException("fitm_file_path is not configured.");
		}

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		Set<String> fileKeySet = fileMap.keySet();
		Iterator<String> iterator = fileKeySet.iterator();

		while (iterator.hasNext()) {
			String fileKey = iterator.next();
			MultipartFile file = fileMap.get(fileKey);

			if (file == null || file.isEmpty()) {
				continue;
			}

			String originalFileName = file.getOriginalFilename();
			if (originalFileName == null || originalFileName.trim().length() == 0) {
				continue;
			}

			int extensionIndex = originalFileName.lastIndexOf(".");
			if (extensionIndex == -1 || extensionIndex == originalFileName.length() - 1) {
				throw new ApiException("Invalid file extension.");
			}

			String fileExt = originalFileName.substring(extensionIndex + 1).toLowerCase();
			if (!isAllowedFitmFileExtension(fileExt)) {
				throw new ApiException("Only image files can be uploaded.");
			}

			String fileUuid = UUID.randomUUID().toString();
			String fileGrpUuid = UUID.randomUUID().toString();
			String fileNmUuid = UUID.randomUUID().toString();

			Calendar calendar = Calendar.getInstance();
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
			String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
			String filePos = year + "/" + month + "/" + day + "/";

			File uploadDir = new File(fitm_file_path + File.separator + year + File.separator + month + File.separator + day);

			try {
				FileUtils.forceMkdir(uploadDir);
				file.transferTo(new File(uploadDir, fileNmUuid));
			} catch (IOException e) {
				throw new ApiException("File upload failed.", e);
			}

			Map<String, Object> fileInfo = new HashMap<String, Object>();
			fileInfo.put("FILE_UUID", fileUuid);
			fileInfo.put("FILE_GRP_UUID", fileGrpUuid);
			fileInfo.put("FILE_NM_UUID", fileNmUuid);
			fileInfo.put("FILE_SIZE", Long.valueOf(file.getSize()).intValue());
			fileInfo.put("FILE_ORIGI_NM", originalFileName);
			fileInfo.put("FILE_NM", originalFileName.substring(0, extensionIndex));
			fileInfo.put("FILE_EXT", fileExt);
			fileInfo.put("FILE_POS", filePos);
			fileInfo.put("MIME_TYPE", file.getContentType());

			resultMap.put(fileKey, fileInfo);
		}

		return resultMap;
	}

	private static boolean isAllowedFitmFileExtension(String fileExt) {
		return "jpg".equals(fileExt) || "jpeg".equals(fileExt) || "gif".equals(fileExt)
				|| "png".equals(fileExt) || "bmp".equals(fileExt) || "webp".equals(fileExt)
				|| "tiff".equals(fileExt) || "heic".equals(fileExt) || "heif".equals(fileExt);
	}
}
