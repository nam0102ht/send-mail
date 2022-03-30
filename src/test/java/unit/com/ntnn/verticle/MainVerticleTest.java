package unit.com.ntnn.verticle;

import com.ntnn.verticle.MailVerticle;
import com.ntnn.verticle.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.multipart.MultipartForm;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
@ExtendWith(VertxExtension.class)
public class MainVerticleTest {

    @BeforeEach
    void setup(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new MailVerticle(), res -> {
            testContext.completeNow();
        });
        vertx.deployVerticle(new MainVerticle(), res -> {
            testContext.completeNow();
        });
    }

    @Test
    @DisplayName("Test file uploaded")
    public void uploadFile(Vertx vertx, VertxTestContext context) throws InterruptedException {
        Thread.sleep(3000);
        String filename = "src/test/resources/data_test/output_complete.txt";
        FileSystem fs = vertx.fileSystem();

        WebClient client = WebClient.create(vertx);


        MultipartForm multipartForm = MultipartForm.create().textFileUpload("output_complete",
                "output_complete.txt",
                        filename,
                        "application/octet-stream"
                );

        HttpRequest<Buffer> req = client.post(8000, "localhost", "/api/v2/send-mail");
        req.sendMultipartForm(multipartForm, ar -> {
            if (ar.succeeded()) {
                HttpResponse<Buffer> response = ar.result();
                System.out.println("Got HTTP response with status " + response.statusCode());
                Assertions.assertEquals("{\"status\":\"Success\",\"message\":\"[\\\"output_complete.txt\\\"]\"}", response.bodyAsString());
                context.completeNow();
            } else {
                ar.cause().printStackTrace();
                context.failNow(ar.cause());
            }
            });
    }
}
