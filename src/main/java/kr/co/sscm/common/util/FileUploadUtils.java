package kr.co.sscm.common.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import kr.co.sscm.common.exception.ApiException;

/**
 * @FileName FileUploadUtils.java
 * @comment file upload util
 * @author AJH
 */
@Component
public class FileUploadUtils {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadUtils.class);
	private static final int THUMBNAIL_MAX_SIZE = 480;

	public static String absolute_path;

	public static String upload_path;

	public static String push_dir;

	public static String fitm_file_path;

	@Value("${push.upload.absolute_path:}")
	public void setAbsolutePath(String path) {
		absolute_path = path;
	}

	@Value("${push.upload.path:}")
	public void setUploadDir(String dir) {
		upload_path = dir;
	}

	@Value("${push.upload.dir:}")
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
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

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
		MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
		int fileIndex = 0;

		for (Map.Entry<String, List<MultipartFile>> entry : fileMap.entrySet()) {
			String fileKey = entry.getKey();
			List<MultipartFile> files = entry.getValue();

			if (files == null || files.isEmpty()) {
				continue;
			}

			for (MultipartFile file : files) {
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
					File uploadedFile = new File(uploadDir, fileNmUuid);
					file.transferTo(uploadedFile);
					createThumbnailQuietly(uploadedFile, fileExt);
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
				fileInfo.put("FILE_PARM_NM", fileKey);

				resultMap.put(fileKey + "_" + fileIndex, fileInfo);
				fileIndex++;
			}
		}

		return resultMap;
	}

	public static File getThumbnailFile(File originalFile) {
		return new File(originalFile.getParentFile(), originalFile.getName() + "_thumbnail");
	}

	private static void createThumbnailQuietly(File originalFile, String fileExt) {
		if (!isThumbnailSupportedImage(fileExt)) {
			return;
		}

		try {
			BufferedImage originalImage = ImageIO.read(originalFile);
			if (originalImage == null) {
				return;
			}
			originalImage = applyExifOrientation(originalFile, originalImage, fileExt);

			int originalWidth = originalImage.getWidth();
			int originalHeight = originalImage.getHeight();
			int maxSide = Math.max(originalWidth, originalHeight);
			double scale = maxSide > THUMBNAIL_MAX_SIZE ? (double) THUMBNAIL_MAX_SIZE / maxSide : 1.0;
			int thumbnailWidth = Math.max(1, (int) Math.round(originalWidth * scale));
			int thumbnailHeight = Math.max(1, (int) Math.round(originalHeight * scale));

			int imageType = isJpeg(fileExt) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
			BufferedImage thumbnailImage = new BufferedImage(thumbnailWidth, thumbnailHeight, imageType);
			Graphics2D graphics = thumbnailImage.createGraphics();
			try {
				graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				graphics.drawImage(originalImage, 0, 0, thumbnailWidth, thumbnailHeight, null);
			} finally {
				graphics.dispose();
			}

			String formatName = isJpeg(fileExt) ? "jpg" : "png";
			ImageIO.write(thumbnailImage, formatName, getThumbnailFile(originalFile));
		} catch (Exception e) {
			logger.warn("Thumbnail creation failed. originalFile={}", originalFile.getAbsolutePath(), e);
		}
	}

	private static BufferedImage applyExifOrientation(File originalFile, BufferedImage image, String fileExt) {
		if (!isJpeg(fileExt)) {
			return image;
		}

		int orientation = readExifOrientation(originalFile);
		if (orientation <= 1) {
			return image;
		}

		int width = image.getWidth();
		int height = image.getHeight();
		int orientedWidth = width;
		int orientedHeight = height;
		AffineTransform transform = new AffineTransform();

		switch (orientation) {
		case 2:
			transform.scale(-1.0, 1.0);
			transform.translate(-width, 0);
			break;
		case 3:
			transform.translate(width, height);
			transform.rotate(Math.PI);
			break;
		case 4:
			transform.scale(1.0, -1.0);
			transform.translate(0, -height);
			break;
		case 5:
			orientedWidth = height;
			orientedHeight = width;
			transform.rotate(Math.PI / 2);
			transform.scale(1.0, -1.0);
			break;
		case 6:
			orientedWidth = height;
			orientedHeight = width;
			transform.translate(height, 0);
			transform.rotate(Math.PI / 2);
			break;
		case 7:
			orientedWidth = height;
			orientedHeight = width;
			transform.translate(height, 0);
			transform.rotate(Math.PI / 2);
			transform.scale(-1.0, 1.0);
			transform.translate(-width, 0);
			break;
		case 8:
			orientedWidth = height;
			orientedHeight = width;
			transform.translate(0, width);
			transform.rotate(-Math.PI / 2);
			break;
		default:
			return image;
		}

		BufferedImage orientedImage = new BufferedImage(orientedWidth, orientedHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = orientedImage.createGraphics();
		try {
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics.drawImage(image, transform, null);
		} finally {
			graphics.dispose();
		}
		return orientedImage;
	}

	private static int readExifOrientation(File originalFile) {
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(originalFile);
			ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
				return directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
			}
		} catch (Exception e) {
			logger.warn("Failed to read image EXIF orientation. originalFile={}", originalFile.getAbsolutePath(), e);
		}
		return 1;
	}
	private static boolean isThumbnailSupportedImage(String fileExt) {
		return "jpg".equals(fileExt) || "jpeg".equals(fileExt) || "png".equals(fileExt);
	}

	private static boolean isJpeg(String fileExt) {
		return "jpg".equals(fileExt) || "jpeg".equals(fileExt);
	}
	private static boolean isAllowedFitmFileExtension(String fileExt) {
		return "jpg".equals(fileExt) || "jpeg".equals(fileExt) || "gif".equals(fileExt)
				|| "png".equals(fileExt) || "bmp".equals(fileExt) || "webp".equals(fileExt)
				|| "tiff".equals(fileExt) || "heic".equals(fileExt) || "heif".equals(fileExt);
	}
}
