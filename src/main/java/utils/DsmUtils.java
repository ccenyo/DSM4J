package utils;

public class DsmUtils {

    private DsmUtils(){}

    public static String manageErrorMessage(Integer code) {

        switch (code) {

            case 101:
                return "No parameter of API, method or version";
            case 102:
                return "The requested API does not exist";
            case 103:
                return "The requested method does not exist";
            case 104:
                return "The requested version does not support the functionality";
            case 105:
                return "The logged in session does not have permission";
            case 106:
                return "Session timeout";
            case 107:
                return "Session interrupted by duplicate login";
            case 400:
                return "Invalid parameter of file operation";
            case 401:
                return "Unknown error of file operation";
            case 402:
                return "System is too busy";
            case 403:
                return "Invalid user does this file operation";
            case 404:
                return "Invalid group does this file operation";
            case 405:
                return "Invalid user and group does this file operation";
            case 406:
                return "Can’t get user/group information from the account server";
            case 407:
                return "Operation not permitted";
            case 408:
                return "No such file or directory";
            case 409:
                return "Non-supported file system";
            case 410:
                return "Failed to connect internet-based file system (ex: CIFS)";
            case 411:
                return "Read-only file system";
            case 412:
                return "Filename too long in the non-encrypted file system";
            case 413:
                return "Filename too long in the encrypted file system";
            case 414:
                return "File already exists";
            case 415:
                return "Disk quota exceeded";
            case 416:
                return "No space left on device";
            case 417:
                return "Input/output error";
            case 418:
                return "Illegal name or path";
            case 419:
                return "Illegal file name";
            case 420:
                return "Illegal file name on FAT file system";
            case 421:
                return "Device or resource busy";
            case 599:
                return "No such task of the file operation";
            case 100:
            default:
                return "Unknown error";
        }
    }
}
