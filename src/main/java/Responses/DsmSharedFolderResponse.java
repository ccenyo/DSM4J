package Responses;

import java.util.List;

public class DsmSharedFolderResponse {
    /**
     * Total number of shared folders.
     */
   Integer total;
    /**
     * Requested offset.
     */
   Integer offset;

   List<Share> shares;

   public static class Share {
       /**
        * Path of a shared folder.
        */
       private String path;
       /**
        * Name of a shared folder.
        */
       private String name;

       private Additional additional;

       public  static class Additional {
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
           private VolumeStatus volume_status;

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
               private Integer freespace;
               /**
                * Byte size of total space of a volume where a shared
                * folder is located.
                */
               private Integer totalspace;
               /**
                * “true”: A volume where a shared folder is located is
                * read-only; “false”: It’s writable.
                */
               private boolean readonly;

               public Integer getFreespace() {
                   return freespace;
               }

               public Integer getTotalspace() {
                   return totalspace;
               }

               public boolean isReadonly() {
                   return readonly;
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

       public String getPath() {
           return path;
       }

       public String getName() {
           return name;
       }

       public Additional getAdditional() {
           return additional;
       }
   }

    public Integer getTotal() {
        return total;
    }

    public Integer getOffset() {
        return offset;
    }

    public List<Share> getShares() {
        return shares;
    }
}
