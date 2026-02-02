package es.speakly.store_backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import es.speakly.store_backend.annotations.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/speakly/upload")
public class UploadImgController {

    @Autowired
    private Cloudinary cloudinary;

    @Authenticated
    @GetMapping("/signature")
    public Map<String, Object> getSignature() {

        long timestamp = System.currentTimeMillis() / 1000;


        Map<String, Object> params = ObjectUtils.asMap(
                "timestamp", timestamp,
                "folder", "profiles"
        );

        String signature = cloudinary.apiSignRequest(params,
                cloudinary.config.apiSecret);

        return Map.of(
                "timestamp", timestamp,
                "signature", signature,
                "apiKey", cloudinary.config.apiKey,
                "cloudName", cloudinary.config.cloudName
        );
    }
}

