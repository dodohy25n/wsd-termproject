package hello.wsd.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${app.firebase.key-path}")
    private String firebaseKeyPath;

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        InputStream serviceAccount;

        if (firebaseKeyPath.startsWith("classpath:")) {
            serviceAccount = new ClassPathResource(firebaseKeyPath.replace("classpath:", "")).getInputStream();
        } else {
            serviceAccount = new FileInputStream(firebaseKeyPath);
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        return FirebaseAuth.getInstance(initializeFirebase());
    }
}
