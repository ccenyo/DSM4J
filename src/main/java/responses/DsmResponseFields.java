package responses;

import java.util.List;

public class DsmResponseFields {

    private DsmResponseFields() { }

    public static class Owner {
        /**
         * User name of file owner.
         */
        private String user;
        /**
         * Group name of file group.
         */
        private String group;
        /**
         * File UID.
         */
        private Integer uid;
        /**
         * File GID.
         */
        private Integer gid;

        public String getUser() {
            return user;
        }

        public String getGroup() {
            return group;
        }

        public Integer getUid() {
            return uid;
        }

        public Integer getGid() {
            return gid;
        }
    }

    public static class VolumeStatus {
        /**
         * Byte size of free space of a volume where a shared
         * folder is located.
         */
        private Long freespace;
        /**
         * Byte size of total space of a volume where a shared
         * folder is located.
         */
        private Long totalspace;
        /**
         * “true”: A volume where a shared folder is located is
         * read-only; “false”: It’s writable.
         */
        private boolean readonly;

        public Long getFreespace() {
            return freespace;
        }

        public Long getTotalspace() {
            return totalspace;
        }

        public boolean isReadonly() {
            return readonly;
        }
    }

    public static class Acl {
        /**
         * If a logged-in user has a privilege to append data or
         * create folders within this folder or not.
         */
        boolean append;
        /**
         * If a logged-in user has a privilege to delete a file/a
         * folder within this folder or not.
         */
        boolean del;
        /**
         * If a logged-in user has a privilege to execute
         * files/traverse folders within this folder or not.
         */
        boolean exec;
        /**
         * If a logged-in user has a privilege to read data or list
         * folder within this folder or not.
         */
        boolean read;
        /**
         * If a logged-in user has a privilege to write data or
         * create files within this folder or not.
         */
        boolean write;

        public boolean isAppend() {
            return append;
        }

        public boolean isDel() {
            return del;
        }

        public boolean isExec() {
            return exec;
        }

        public boolean isRead() {
            return read;
        }

        public boolean isWrite() {
            return write;
        }
    }

    public static class AdvanceRight {
        /**
         * If a non-administrator user can download files in this
         * shared folder through SYNO.FileStation.Download
         * API or not.
         */
        boolean disable_down_load;
        /**
         * If a non-administrator user can enumerate files in
         * this shared folder though SYNO.FileStation.List API
         * with list method or not.
         */
        boolean disable_list;
        /**
         * If a non-administrator user can modify or overwrite
         * files in this shared folder or not.
         */
        boolean disable_modify;

        public boolean isDisable_down_load() {
            return disable_down_load;
        }

        public boolean isDisable_list() {
            return disable_list;
        }

        public boolean isDisable_modify() {
            return disable_modify;
        }
    }

    public static class Perm {
        /**
         * “RW: The shared folder is writable; “RO”: the shared
         * folder is read-only.
         */
        private String share_right;
        /**
         * POSIX file permission, For example, 777 means
         * owner, group or other has all permission; 764 means
         * owner has all permission, group has read/write
         * permission, other has read permission.
         */
        private Integer posix;
        /**
         * Specail privelge of the shared folder
         */
        private AdvanceRight adv_right;
        /**
         * If the configure of Windows ACL privilege of the
         * shared folder is enabled or not
         */
        boolean acl_enable;
        /**
         * “true”: The privilege of the shared folder is set to be
         * ACL-mode. “false”: The privilege of the shared folder
         * is set to be POSIX-mode.
         */
        boolean is_acl_mode;
        /**
         * Windows ACL privilege. If a shared folder is set to be
         * POSIX-mode, these values of Windows ACL
         * privileges are derived from the POSIX privilege.
         */
        private Acl acl;

        public String getShare_right() {
            return share_right;
        }

        public Integer getPosix() {
            return posix;
        }

        public AdvanceRight getAdv_right() {
            return adv_right;
        }

        public boolean isAcl_enable() {
            return acl_enable;
        }

        public boolean isIs_acl_mode() {
            return is_acl_mode;
        }

        public Acl getAcl() {
            return acl;
        }
    }

    public static class Time {
        /**
         * Linux timestamp of last access in second
         */
        private Long atime;
        /**
         * Linux timestamp of last modification in second.
         */
        private Long mtime;
        /**
         * Linux timestamp of last change in second.
         */
        private Long ctime;
        /**
         * Linux timestamp of create time in second.
         */
        private Long crtime;

        public Long getAtime() {
            return atime;
        }

        public Long getMtime() {
            return mtime;
        }

        public Long getCtime() {
            return ctime;
        }

        public Long getCrtime() {
            return crtime;
        }
    }

    public  static class AdditionalListShare {
        /**
         * Real path of a shared folder in a volume space
         */
        String real_path;

        private Owner owner;
        /**
         * Time information of file including last access time,
         * last modified time, last change time, and creation
         * time.
         */
        private Time time;
        /**
         * File permission information
         */
        private Perm perm;
        /**
         * Type of a virtual file system of a mount point.
         */
        private String mount_point_type;
        /**
         * Volume status including free space, total space and
         * read-only status.
         */
        private DsmResponseFields.VolumeStatus volume_status;

        public String getReal_path() {
            return real_path;
        }

        public Owner getOwner() {
            return owner;
        }

        public Time getTime() {
            return time;
        }

        public Perm getPerm() {
            return perm;
        }

        public String getMount_point_type() {
            return mount_point_type;
        }

        public VolumeStatus getVolume_status() {
            return volume_status;
        }
    }

    public static class Share {
        /**
         * Path of a shared folder.
         */
        private String path;
        /**
         * Name of a shared folder.
         */
        private String name;

        private AdditionalListShare additional;

        public String getPath() {
            return path;
        }

        public String getName() {
            return name;
        }

        public AdditionalListShare getAdditional() {
            return additional;
        }
    }

    public static class AdditionalListFolder {
        /**
         * Real path started with a volume path.
         */
        private String real_path;
        /**
         * File size.
         */
        private Long size;
        /**
         * File owner information including user name, group name, UID and GID.
         */
        private DsmResponseFields.Owner owner;
        /**
         * Time information of file including last access time,
         * last modified time, last change time and create time.
         */
        private DsmResponseFields.Time time;
        /**
         * File permission information.
         */
        private DsmResponseFields.Perm perm;
        /**
         * A type of a virtual file system of a mount point.
         */
        private String mount_point_type;
        /**
         * File extension.
         */
        private String type;

        public String getReal_path() {
            return real_path;
        }

        public Long getSize() {
            return size;
        }

        public Owner getOwner() {
            return owner;
        }

        public Time getTime() {
            return time;
        }

        public Perm getPerm() {
            return perm;
        }

        public String getMount_point_type() {
            return mount_point_type;
        }

        public String getType() {
            return type;
        }
    }

    public static class Children {
        /**
         * Total number of files.
         */
        private Integer total;
        /**
         * Requested offset
         */
        private Integer offset;
        /**
         * Array of <file> objects.
         */
        private Files files;

        public Integer getTotal() {
            return total;
        }

        public Integer getOffset() {
            return offset;
        }

        public Files getFiles() {
            return files;
        }
    }

    public static class Files {
        /**
         * Folder/file path started with a shared folder.
         */
        private String path;
        /**
         * File name.
         */
        private String name;
        /**
         * If this file is folder or not.
         */
        private boolean isdir;
        /**
         * File list within a folder which is described by a <file>
         * object. The value is returned, only if goto_path
         * parameter is given.
         */
        private List<DsmResponseFields.Children> children;
        /**
         * File additional object.
         */
        private DsmResponseFields.AdditionalListFolder additional;

        public String getPath() {
            return path;
        }

        public String getName() {
            return name;
        }

        public boolean isIsdir() {
            return isdir;
        }

        public List<Children> getChildren() {
            return children;
        }

        public DsmResponseFields.AdditionalListFolder getAdditional() {
            return additional;
        }
    }
}
