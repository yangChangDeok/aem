package content;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XmlMigration {

    // 대상폴더지정
    private static final String INPUT_DIRECTORY = "C:\\Users\\ycd63\\Downloads\\de_chnage_xf_partner\\jcr_root\\content\\experience-fragments\\lgemts\\de\\de\\d2b2c\\partner_store\\master"; // XF 패키지가 있는 디렉토리

    //대상문자
    private static String searchTerm;

    //변경문자
    private static String replaceTerm;

    public static void main(String[] args) {
        searchTerm = "/content/lge/de/partnerstore";

        replaceTerm = "/content/lgemts/de/partnerstore";

        try {
            // XML 파일을 검색하고 내용을 수정하는 메소드 호출
            processDirectory(INPUT_DIRECTORY);
            System.out.println("Migration completed successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred during migration: " + e.getMessage());
        }
    }

    // 지정된 디렉토리와 그 하위 디렉토리의 모든 파일을 재귀적으로 검색하는 메소드
    private static void processDirectory(String directoryPath) throws IOException {
        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".xml"))
                .forEach(XmlMigration::processXMLFile);
    }

    // XML 파일의 내용을 읽고, 특정 단어를 변경하는 메소드
    private static void processXMLFile(Path filePath) {
        try {
            File file = filePath.toFile();
            String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);

            // 검색어를 찾아 대체어로 변경
            if (content.contains(searchTerm)) {
                String updatedContent = content.replace(searchTerm, replaceTerm);
                // 변경된 내용을 파일에 다시 씀
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    outputStream.write(updatedContent.getBytes(StandardCharsets.UTF_8));
                    System.out.println("Updated: " + file.getPath());
                }
            }
        } catch (IOException e) {
            System.err.println("Error processing file: " + filePath + " - " + e.getMessage());
        }
    }
}
