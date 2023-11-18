package orz.springboot.data.lock;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class OrzLockName {
    private final List<String> parts;

    private OrzLockName(List<String> parts) {
        this.parts = parts;
    }

    public OrzLockName with(Object... args) {
        var addParts = toParts(args);
        var newParts = new ArrayList<String>(this.parts.size() + addParts.size());
        newParts.addAll(this.parts);
        newParts.addAll(addParts);
        return new OrzLockName(newParts);
    }

    public String getName(String applicationName) {
        return applicationName + ":orz-lock:" + String.join("-", parts);
    }

    public static OrzLockName of(Object... args) {
        return new OrzLockName(toParts(args));
    }

    private static List<String> toParts(Object... args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("args is empty");
        }
        var parts = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) {
            var arg = args[i];
            if (arg == null) {
                throw new IllegalArgumentException("args[" + i + "] is null");
            }
            var str = arg.toString();
            if (StringUtils.isBlank(str)) {
                throw new IllegalArgumentException("args[" + i + "] is blank");
            }
            parts.add(str);
        }
        return parts;
    }
}
