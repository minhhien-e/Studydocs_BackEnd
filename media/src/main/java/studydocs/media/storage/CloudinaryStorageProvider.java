package studydocs.media.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import studydocs.media.model.enums.HttpMethod;
import studydocs.media.storage.StorageProvider;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.Duration;
import java.util.Map;

@Component
public class CloudinaryStorageProvider implements StorageProvider {
    private final Cloudinary cloudinary;

    public CloudinaryStorageProvider(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public void upload(String key, InputStream data, long contentLength, Map<String, String> metadata) {
        try {
            cloudinary.uploader().upload(data, ObjectUtils.asMap("public_id", key));
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload to Cloudinary", e);
        }
    }

    @Override
    public InputStream download(String key) {
        throw new UnsupportedOperationException("Download via stream not supported for Cloudinary");
    }

    @Override
    public void delete(String key) {
        try {
            cloudinary.uploader().destroy(key, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete from Cloudinary", e);
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            cloudinary.api().resource(key, ObjectUtils.emptyMap());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generatePresignedUrl(String key, HttpMethod method, Duration expiresIn) {
        if (method == HttpMethod.GET) {
            return cloudinary.url().generate(key);
        }
        long timestamp = System.currentTimeMillis() / 1000;
        Map<String, Object> paramsToSign = ObjectUtils.asMap("timestamp", timestamp, "public_id", key);
        String signature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret);
        
        return String.format("https://api.cloudinary.com/v1_1/%s/image/upload?api_key=%s&timestamp=%d&signature=%s&public_id=%s",
                cloudinary.config.cloudName, cloudinary.config.apiKey, timestamp, signature, key);
    }
}
