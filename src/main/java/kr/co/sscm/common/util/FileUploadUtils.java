package kr.co.sscm.common.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.sscm.common.exception.ApiException;


/**
 * @FileName FileUploadUtils.java
 * @comment file upload util
 * @author AJH
 */
@Component
public class FileUploadUtils {

	private static Logger logger = LoggerFactory.getLogger(FileUploadUtils.class);

	public static String absolute_path;

	public static String upload_path;

	public static String push_dir;

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

	/**
	 * 푸시용 파일 업로드
	 * @param file				: 파일 업로드할 파일
	 * @param imageFolderName	: 업로드 기본경우 하위 저장 폴더명
	 * @return
	 * @throws ApiException
	 */
	public Map<String, Object> pushFileUpload(MultipartFile file) throws ApiException{

		String path = "";
		String pathWeb = "";
		String fName = "";
		String thumbPath = "";
		String thumbPathWeb = "";

		Map<String, Object> imgMap = new HashMap<String, Object>();

        if(!file.isEmpty()){

            path = absolute_path + File.separator ;															// 파일 저장 경로
            pathWeb = File.separator + upload_path + File.separator + push_dir + File.separator ;			// 웹상 접근경로

            try {
            	//디렉토리생성
                File destinationDir = new File(path);
				FileUtils.forceMkdir(destinationDir);

	            SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMddHHmmssSS");
	            String namePrefix = sdt.format(Calendar.getInstance().getTime());

	            String orgFileName = file.getOriginalFilename();
	            int index = orgFileName.lastIndexOf(".");
	            if (index != -1) {
	                String fileExt  = orgFileName.substring(index + 1);
	                UUID uuid = UUID.randomUUID();
	                fName = uuid.toString() + "_" + namePrefix +"."+fileExt;
	                if(!fileExt.toLowerCase().equals("gif") && !fileExt.toLowerCase().equals("jpg") && !fileExt.toLowerCase().equals("jpeg") && !fileExt.toLowerCase().equals("png")){
	                    throw new ApiException("확장자가 GIF, JPG, PNG 이미지 파일만 첨부 가능합니다.");
	                }
	            }else{
	                throw new ApiException("올바르지 않은 확장자 입니다.");
	            }

	            File toFile = new File(path + fName);
	            file.transferTo(toFile);

	            imgMap.put("imageUploadUrl", pathWeb + fName);	// 접근경로
	            imgMap.put("filePathNm", path);					// 파일 저장 경로
	            imgMap.put("originalNm", orgFileName);			// 원폰 파일명
	            imgMap.put("uploadFileNm", fName);				// 업로드된 파일명
	            imgMap.put("fileSize", Long.valueOf(destinationDir.length()).intValue());
	            imgMap.put("thumbUploadUrl", thumbPathWeb + "thumbnail." + fName);	// 썸네일 접근경로

            } catch (IOException e) {
				// TODO Auto-generated catch block
            	throw new ApiException(e.toString());
			}
        }
        return imgMap;
	}
}
