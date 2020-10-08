package requests.filestation;

import responses.filestation.DsmResponseFields;

import java.util.List;

public class DsmRequestParameters {
    public enum Sort {
        /**
         * file name
         */
        NAME,
        /**
         * file owner
         */
        USER,
        /**
         * file group
         */
        GROUP,
        /**
         * last modified time
         */
        MTIME,
        /**
         * last access time
         */
        ATIME,
        /**
         * last change time
         */
        CTIME,
        /**
         * create time
         */
        CRTIME,
        /**
         * POSIX permission
         */
        POSIX,
        /**
         * file extension
         */
        TYPE
    }

    public enum ShareSort {
        /**
         * a unique ID of sharing a
         * file/folder
         */
        id,
        /**
         * file name
         */
        name,
        /**
         * if it’s a folder or not
         */
        isFolder,
        /**
         * file path
         */
        path,
        /**
         *   the expiration
         * date for the sharing link
         */
        date_expired,
        /**
         * the available
         * date for the sharing link start
         * effective
         */
        date_available,
        /**
         * the link accessibility
         * status
         */
        status,
        /**
         * If the sharing
         * link is protected or not
         */
        has_password,
        /**
         * a URL of a sharing link
         */
        url,
        /**
         * the user name of
         * the sharing link owner
         */
        link_owner

    }

    public enum SortDirection {
        /**
         * sort ascending
         */
        ASC,
        /**
         * sort descending
         */
        DESC
    }

    public enum Additional {
        /**
         * return a real path in volume
         */
        REAL_PATH,
        /**
         * return information about file owner including user name, group name, UID and GID
         */
        OWNER,
        /**
         * return information about time including last access time, last modified time, last change time and create time
         */
        TIME,
        /**
         * return information about file permission
         */
        PERM,
        /**
         * return a type of a virtual file system of a mount point
         */
        MOUNT_POINT_TYPE,
        /**
         * return volume statuses including free space, total space and read-only status
         */
        VOLUME_STATUS,
        /**
         *  return a file extension
         */
        TYPE
    }

    public enum FileType {
        ALL,
        FILE,
        DIR
    }

    public enum Mode {
        OPEN,
        DOWNLOAD
    }

    public enum OverwriteBehaviour {
        SKIP(false),
        OVERWRITE(true),
        ERROR(null);

        OverwriteBehaviour(Boolean value) {
            this.value = value;
        }
        private final Boolean value;

        public Boolean getValue() {
            return value;
        }
    }

    public enum  StatusFilter {
        VALID,
        BROKEN,
        ALL
    }
}
