package unit.com.ntnn;

import com.ntnn.Main;
import com.ntnn.common.Utils;
import com.ntnn.model.User;
import com.ntnn.repository.ReadCsvFile;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MainTest {
    @InjectMocks
    private Main main;

    @BeforeEach
    public void init() {
        File file = new File("src/test/resources/data_test/output.json");
        if(file.exists()) {
            if (file.delete()) {
                System.out.println("Success deleted file: "+ file.getName());
            } else {
                System.out.println("Deleting fail");
            }
        }
    }

    @Test
    public void processCommandLine_Test_Success() throws Exception {
        String[] args = {"src/test/resources/data_test/emailTemplate.json", "src/test/resources/data_test/customer.csv", "src/test/resources/data_test/", "src/test/resources/data_test/errors.csv"};
        Assertions.assertDoesNotThrow(() -> {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                main.processCommandLine(args);
            });
            completableFuture.join();
            if (completableFuture.isDone()) {
                JSONArray jsonArray = parseArray("src/test/resources/data_test/output.json");
                String actual = jsonArray.toJSONString();
                Assertions.assertEquals(2, jsonArray.size());
                Assertions.assertNotNull(actual);
            }
        });
    }

    @Test
    public void processCommandLine_Test_Errors() throws Exception {
        String[] args = {"src/test/resources/data_test/emailTemplate.json", "src/test/resources/data_test/customerErrors.csv", "src/test/resources/data_test/", "src/test/resources/data_test/errors2.csv"};
        Assertions.assertDoesNotThrow(() -> {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                main.processCommandLine(args);
            });
            completableFuture.join();
            if (completableFuture.isDone()) {
                JSONArray jsonArray = parseArray("src/test/resources/data_test/output.json");
                String actual = jsonArray.toJSONString();
                List<User> errors = ReadCsvFile.getInstance().readFromFile("src/test/resources/data_test/errors2.csv");
                Assertions.assertEquals(2, jsonArray.size());
                Assertions.assertNotNull(actual);
                Assertions.assertEquals(2, errors.size());
            }
        });
    }

    @Test
    public void processCommandLine_Test_Errors_EmailTemplate_NotFound() throws Exception {
        String[] args = {"", "src/test/resources/data_test/customerErrors.csv", "src/test/resources/data_test/", "src/test/resources/data_test/errors2.csv"};
        Assertions.assertDoesNotThrow(() -> {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                main.processCommandLine(args);
            });
            completableFuture.join();
            if (completableFuture.isDone()) {
                File file = new File("src/test/resources/data_test/output.json");
                Assertions.assertFalse(file.exists());
            }
        });
    }

    @Test
    public void processCommandLine_Test_Errors_PathOutput_NotFound() throws Exception {
        String[] args = {"src/test/resources/data_test/emailTemplate.json", "src/test/resources/data_test/customerErrors.csv", "", "src/test/resources/data_test/errors2.csv"};
        Assertions.assertDoesNotThrow(() -> {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                main.processCommandLine(args);
            });
            completableFuture.join();
            if (completableFuture.isDone()) {
                File file = new File("src/test/resources/data_test/output.json");
                Assertions.assertFalse(file.exists());
            }
        });
    }

    @Test
    public void processCommandLine_Test_Errors_PathErrors_NotFound() throws Exception {
        String[] args = {"src/test/resources/data_test/emailTemplate.json", "src/test/resources/data_test/customerErrors.csv", "", "src/test/resources/data_test/errors2.csv"};
        Assertions.assertDoesNotThrow(() -> {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                main.processCommandLine(args);
            });
            completableFuture.join();
            if (completableFuture.isDone()) {
                File file = new File("src/test/resources/data_test/output.json");
                Assertions.assertFalse(file.exists());
            }
        });
    }

    @Test
    public void processCommandLine_Test_Errors_Data_NotFound() throws Exception {
        String[] args = {"src/test/resources/data_test/emailTemplate.json", "", "src/test/resources/data_test/", ""};
        Assertions.assertDoesNotThrow(() -> {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                main.processCommandLine(args);
            });
            completableFuture.join();
            if (completableFuture.isDone()) {
                File file = new File("src/test/resources/data_test/output.json");
                Assertions.assertFalse(file.exists());
            }
        });
    }

    @Test
    public void processCommandLine_Test_Errors_DataErrors_NotFound() throws Exception {
        String[] args = {"src/test/resources/data_test/emailTemplate.json", "src/test/resources/data_test/", "src/test/resources/data_test/", ""};
        Assertions.assertDoesNotThrow(() -> {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                main.processCommandLine(args);
            });
            completableFuture.join();
            if (completableFuture.isDone()) {
                File file = new File("src/test/resources/data_test/output.json");
                Assertions.assertFalse(file.exists());
            }
        });
    }

    @Test
    public void processCommandLine_FileTemplate_NotMatch() throws Exception {
        String[] args = {"src/test/resources/data_test/emailTemplateFail.json", "src/test/resources/data_test/customer.csv", "src/test/resources/data_test/", "src/test/resources/data_test/errors.csv"};
        Assertions.assertDoesNotThrow(() -> {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                main.processCommandLine(args);
            });
            completableFuture.join();
            if (completableFuture.isDone()) {
                JSONArray array = Utils.parseJsonArray("src/test/resources/data_test/output.json");
                Assertions.assertEquals(0, array.size());
            }
        });
    }

    @Test
    public void processCommandLine_FileTemplate_NoHaveBody() throws Exception {
        String[] args = {"src/test/resources/data_test/emailTemplateNobody.json", "src/test/resources/data_test/customer.csv", "src/test/resources/data_test/", "src/test/resources/data_test/errors.csv"};
        Assertions.assertDoesNotThrow(() -> {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                main.processCommandLine(args);
            });
            completableFuture.join();
            if (completableFuture.isDone()) {
                File file = new File("src/test/resources/data_test/output.json");
                Assertions.assertFalse(file.exists());
            }
        });
    }

    public JSONArray parseArray(String path) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(path)) {
            return (JSONArray) parser.parse(reader);
        }
    }

}
