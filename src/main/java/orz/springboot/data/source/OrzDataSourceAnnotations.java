package orz.springboot.data.source;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class OrzDataSourceAnnotations {
    private final LinkedHashMap<String, OrzDataSourceAnnotation> annotations = new LinkedHashMap<>();

    public void register(OrzDataSourceAnnotation annotation) {
        var exists = annotations.get(annotation.getName());
        if (exists != null) {
            throw new IllegalArgumentException("@OrzDataSource duplicated: name=" + annotation.getName() + ", class1=" + exists.getMetadata().getClassName() + ", class2=" + annotation.getMetadata().getClassName());
        }
        annotations.put(annotation.getName(), annotation);
    }

    public void forEach(BiConsumer<String, OrzDataSourceAnnotation> action) {
        annotations.forEach(action);
    }
}
