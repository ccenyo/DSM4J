package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DsmUtils {

    private static final Map<Integer, String> codeErrors = new HashMap<>();
    static {
        codeErrors.put(101, "No parameter of API, method or version");
        codeErrors.put(102, "The requested API does not exist");
        codeErrors.put(103, "The requested method does not exist");
        codeErrors.put(104, "The requested version does not support the functionality");
        codeErrors.put(105, "The logged in session does not have permission");
        codeErrors.put(106, "Session timeout");
        codeErrors.put(107, "Session interrupted by duplicate login");
        codeErrors.put(400, "Invalid parameter of file operation");
        codeErrors.put(401, "Unknown error of file operation");
        codeErrors.put(402, "System is too busy");
        codeErrors.put(403, "Invalid user does this file operation");
        codeErrors.put(404, "Invalid group does this file operation");
        codeErrors.put(405, "Invalid user and group does this file operation");
        codeErrors.put(406, "Can’t get user/group information from the account server");
        codeErrors.put(407, "Operation not permitted");
        codeErrors.put(408, "No such file or directory");
        codeErrors.put(409, "Non-supported file system");
        codeErrors.put(410, "Failed to connect internet-based file system (ex: CIFS)");
        codeErrors.put(411, "Read-only file system");
        codeErrors.put(412, "Filename too long in the non-encrypted file system");
        codeErrors.put(413, "Filename too long in the encrypted file system");
        codeErrors.put(414, "File already exists");
        codeErrors.put(415, "Disk quota exceeded");
        codeErrors.put(416, "No space left on device");
        codeErrors.put(417, "Input/output error");
        codeErrors.put(418, "Illegal name or path");
        codeErrors.put(419, "Illegal file name");
        codeErrors.put(420, "Illegal file name on FAT file system");
        codeErrors.put(421, "Device or resource busy");
        codeErrors.put(500, "No such task of the file operation");
        codeErrors.put(100, "Unknown error");
    }

    private DsmUtils(){}

    public static String manageErrorMessage(Integer code) {
        return Optional.ofNullable(codeErrors.get(code)).orElse("Unknown error");
    }
}
