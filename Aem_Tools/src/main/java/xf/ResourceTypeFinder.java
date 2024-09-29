package xf;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;

/**
 * 현재 XF 컴포넌트의 Sling:ResourceType의 값을 조회해온다.
 *
 */
public class ResourceTypeFinder {
    private static HashSet<String> extractedResourceTypes = new HashSet<>();

    public static void main(String[] args) {
        // 지정된 폴더 경로 (예: XML 파일들이 있는 최상위 폴더 경로)
        String folderPath = "C:\\Users\\ycd63\\Downloads\\de_pa_xf\\jcr_root\\content\\experience-fragments\\lgemts\\de\\de\\d2b2c\\partner_store\\master"; // 실제 폴더 경로로 수정하세요
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            // 폴더 내 XML 파일의 sling:resourceType 값을 추출
            extractResourceTypes(folder);

            // 최종 중복이 제거된 값을 출력
            extractedResourceTypes.forEach(System.out::println);
        } else {
            System.out.println("유효한 폴더 경로가 아닙니다.");
        }
    }

    // 폴더를 탐색하여 XML 파일에서 sling:resourceType 값을 추출하는 메소드
    public static void extractResourceTypes(File folder) {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 하위 폴더 탐색
                    extractResourceTypes(file);
                } else if (file.getName().endsWith(".xml")) {
                    // XML 파일에서 sling:resourceType 값을 추출
                    extractResourceTypeFromXML(file);
                }
            }
        }
    }

    // XML 파일에서 sling:resourceType 값을 추출하는 메소드
    private static void extractResourceTypeFromXML(File xmlFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(xmlFile);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(fileInputStream);
            document.getDocumentElement().normalize();

            // XML 파일 내 모든 요소에서 sling:resourceType 속성을 찾습니다
            extractResourceType(document.getDocumentElement());

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    // XML 요소에서 sling:resourceType 값을 추출하는 재귀 메소드
    private static void extractResourceType(Element element) {
        // sling:resourceType 속성이 있는 경우 값을 추가
        if (element.hasAttribute("sling:resourceType")) {
            String resourceType = element.getAttribute("sling:resourceType");
            // 중복되지 않는 경우에만 추가
            extractedResourceTypes.add(resourceType);
        }
        // 하위 요소를 재귀적으로 탐색
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                extractResourceType((Element) node);
            }
        }
    }
}
