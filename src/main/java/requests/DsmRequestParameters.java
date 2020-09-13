package requests;

public class DsmRequestParameters {
    public enum Sort {
        /**
         * file name
         */
        name,
        /**
         * file owner
         */
        user,
        /**
         * file group
         */
        group,
        /**
         * last modified time
         */
        mtime,
        /**
         * last access time
         */
        atime,
        /**
         * last change time
         */
        ctime,
        /**
         * create time
         */
        crtime,
        /**
         * POSIX permission
         */
        posix,
        /**
         * file extension
         */
        type
    }

    public enum SortDirection {
        /**
         * sort ascending
         */
        asc,
        /**
         * sort descending
         */
        desc
    }

    public enum Additional {
        /**
         * return a real path in volume
         */
        real_path,
        /**
         * return information about file owner including user name, group name, UID and GID
         */
        owner,
        /**
         * return information about time including last access time, last modified time, last change time and create time
         */
        time,
        /**
         * return information about file permission
         */
        perm,
        /**
         * return a type of a virtual file system of a mount point
         */
        mount_point_type,
        /**
         * return volume statuses including free space, total space and read-only status
         */
        volume_status,
        /**
         *  return a file extension
         */
        type
    }

    public enum FileType {
        all,
        file,
        dir
    }

}
