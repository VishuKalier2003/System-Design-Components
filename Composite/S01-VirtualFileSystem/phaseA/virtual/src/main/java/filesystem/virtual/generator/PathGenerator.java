package filesystem.virtual.generator;

import org.springframework.stereotype.Component;

@Component
public class PathGenerator {
    private final String HOME = "/";

    public String addRelativePath(String homePath, String path) {
        return homePath + "/" + path;
    }

    public String addPath(String path) {
        return HOME + path;
    }
}
