package filesystem.virtual.core;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import filesystem.virtual.Enum.AdapterTypes;
import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.model.Adapter;
import filesystem.virtual.service.MountExtractor;
import filesystem.virtual.utils.FileData;

@Component
public class DataStoreAdapter extends Adapter {
    private final Set<String> offWords;

    @Autowired private MountExtractor mountExtractor;

    public DataStoreAdapter(@Value("${storage.db-store-path}") String path) {
        super(path, AdapterTypes.DATASTORE, "DATASTORE-ADAPTER");
        this.offWords = new HashSet<>();
        offWords.add("system");
        offWords.add("windows");
        offWords.add("exe");
    }

    @Override
    public OperationStatus createFolder(String path) {
        try {
            Set<String> words = mountExtractor.extractStrings(path);
            for(String s : offWords)
                if(words.contains(s))
                    return OperationStatus.NOMENCLATURE_ERROR;
            Path BASE = Paths.get(getHOME());
            Path additional = BASE.resolve(path).normalize();
            Files.createDirectories(additional);
            return OperationStatus.DONE;
        }
        catch(IOException e) {
            return OperationStatus.FAIL;
        }
    }

    @Override
    public OperationStatus createFile(FileData fd) {
        try {
            Path BASE = Paths.get(getHOME());
            Path additional = BASE.resolve(fd.getRelativeFilePath()).normalize();
            // create directories if not exist
            Files.createDirectories(additional);
            // create path to the respective file
            Path targetFile = additional.resolve(fd.getFileName()).normalize();
            Files.createFile(targetFile);
            return OperationStatus.DONE;
        }
        catch(IOException e) {
            return OperationStatus.FAIL;
        }
    }

    @Override
    public OperationStatus readFile(FileData fd) {
        try {
            Path base = Paths.get(getHOME());
            Path filePath = base.resolve(fd.getRelativeFilePath()).resolve(fd.getFileName()).normalize();
            String content = Files.readString(filePath);
            System.out.println("COntent : "+content);
            getBUFFER().append(content).append("\n");
            return OperationStatus.DONE;
        } catch (NoSuchFileException e) {
            return OperationStatus.PATH_ERROR;
        } catch (IOException e) {
            return OperationStatus.FAIL;
        }
    }

    @Override
    public OperationStatus readFolder(FileData fd) {
        try {
            Path base = Paths.get(getHOME());
            Path root = base.resolve(fd.getRelativeFilePath()).normalize();
            if (!Files.exists(root))
                return OperationStatus.PATH_ERROR;
            Files.walk(root)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            String content = Files.readString(file);
                            getBUFFER().append("FILE: ").append(file.toString()).append("\n");
                            getBUFFER().append(content).append("\n\n");
                        } catch (IOException ignore) {
                        }
                    });
            return OperationStatus.DONE;
        } catch (IOException e) {
            return OperationStatus.FAIL;
        }
    }

    @Override
    public OperationStatus write(FileData fd) {
        try {
            Path root = Paths.get(getHOME());
            Path file = root.resolve(fd.getRelativeFilePath()).resolve(fd.getFileName()).normalize();
            Files.createDirectories(file.getParent());
            Files.writeString(
                file,
                fd.getText(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
            return OperationStatus.DONE;
        } catch (IOException e) {
            e.getLocalizedMessage();
            return OperationStatus.FAIL;
        }
    }

    @Override
    public OperationStatus remove(String path) {
        try {
            Path root = Paths.get(getHOME());
            Path target = root.resolve(path).normalize();
            Files.walkFileTree(target, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc != null) throw exc;
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            return OperationStatus.DONE;
        } catch (NoSuchFileException e) {
            return OperationStatus.PATH_ERROR;
        } catch (IOException e) {
            return OperationStatus.FAIL;
        }
    }
}
