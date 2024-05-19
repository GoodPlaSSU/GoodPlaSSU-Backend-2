package com.ssu.goodplassu.image.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssu.goodplassu.image.config.NcpS3Properties;
import com.ssu.goodplassu.image.entity.Image;
import com.ssu.goodplassu.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
	private final NcpS3Properties ncpS3Properties;
	private final AmazonS3Client amazonS3Client;
	private final ImageRepository imageRepository;

	public List<Image> uploadImages(
			final List<MultipartFile> multipartFiles,
			final String filePath
	) {
		List<Image> images = new ArrayList<>();

		for (MultipartFile multipartFile : multipartFiles) {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(multipartFile.getSize());
			objectMetadata.setContentType(multipartFile.getContentType());

			String originalFileName = multipartFile.getOriginalFilename();
			String newFileName = filePath + "_" + LocalDateTime.now() + "_" + UUID.randomUUID() + "_" + originalFileName;
			String folderName = createFolderNameWithTodayDate();
			String keyName = filePath + "/" + folderName + "/" + newFileName;
			String uploadUrl = "";

			try (InputStream inputStream = multipartFile.getInputStream()) {

				amazonS3Client.putObject(
						new PutObjectRequest(
								ncpS3Properties.getS3().getBucketName(), keyName, inputStream, objectMetadata)
								.withCannedAcl(CannedAccessControlList.PublicRead));

				uploadUrl = ncpS3Properties.getS3().getEndPoint() + "/" + ncpS3Properties.getS3().getBucketName() + "/"
						+ keyName;

			} catch (SdkClientException e) {
				e.printStackTrace();
				throw new SdkClientException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}

			Image image = Image.builder()
					.imageId(keyName)
					.name(newFileName)
					.url(uploadUrl)
					.size(multipartFile.getSize())
					.build();

			images.add(image);
		}

		return images;
	}

	private String createFolderNameWithTodayDate() {
		LocalDateTime now = LocalDateTime.now();
		String year = String.valueOf(now.getYear());
		String month = String.format("%02d", now.getMonthValue());

		return year + "/" + month;
	}

	public void deleteImageInS3Bucket(final String keyName) {
		try {
			amazonS3Client.deleteObject(ncpS3Properties.getS3().getBucketName(), keyName);
		} catch (AmazonS3Exception e) {
			e.printStackTrace();
			throw new AmazonS3Exception(e.getMessage());
		} catch (SdkClientException e) {
			e.printStackTrace();
			throw new SdkClientException(e.getMessage());
		}
	}

	public String getImageIdAndDeleteImage(final String imageUrl) {
		Image image = imageRepository.findByUrl(imageUrl).orElse(null);
		if (image == null) {
			return null;
		}

		imageRepository.delete(image);

		return image.getImageId();
	}
}
