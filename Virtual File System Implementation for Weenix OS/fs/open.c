/*
 *  FILE: open.c
 *  AUTH: mcc | jal
 *  DESC:
 *  DATE: Mon Apr  6 19:27:49 1998
 */

#include "globals.h"
#include "errno.h"
#include "fs/fcntl.h"
#include "util/string.h"
#include "util/printf.h"
#include "fs/vfs.h"
#include "fs/vnode.h"
#include "fs/file.h"
#include "fs/vfs_syscall.h"
#include "fs/open.h"
#include "fs/stat.h"
#include "util/debug.h"

/* find empty index in p->p_files[] */
int
get_empty_fd(proc_t *p)
{
        int fd;

        for (fd = 0; fd < NFILES; fd++) {
                if (!p->p_files[fd])
                        return fd;
        }

        dbg(DBG_ERROR | DBG_VFS, "ERROR: get_empty_fd: out of file descriptors "
            "for pid %d\n", curproc->p_pid);
        return -EMFILE;
}

/*
 * There a number of steps to opening a file:
 *      1. Get the next empty file descriptor.
 *      2. Call fget to get a fresh file_t.
 *      3. Save the file_t in curproc's file descriptor table.
 *      4. Set file_t->f_mode to OR of FMODE_(READ|WRITE|APPEND) based on
 *         oflags, which can be O_RDONLY, O_WRONLY or O_RDWR, possibly OR'd with
 *         O_APPEND.
 *      5. Use open_namev() to get the vnode for the file_t.
 *      6. Fill in the fields of the file_t.
 *      7. Return new fd.
 *
 * If anything goes wrong at any point (specifically if the call to open_namev
 * fails), be sure to remove the fd from curproc, fput the file_t and return an
 * error.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EINVAL
 *        oflags is not valid.
 *      o EMFILE
 *        The process already has the maximum number of files open.
 *      o ENOMEM
 *        Insufficient kernel memory was available.
 *      o ENAMETOOLONG
 *        A component of filename was too long.
 *      o ENOENT
 *        O_CREAT is not set and the named file does not exist.  Or, a
 *        directory component in pathname does not exist.
 *      o EISDIR
 *        pathname refers to a directory and the access requested involved
 *        writing (that is, O_WRONLY or O_RDWR is set).
 *      o ENXIO
 *        pathname refers to a device special file and no corresponding device
 *        exists.
 */

int
do_open(const char *filename, int oflags)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_open");*/
			int flag=oflags&0x003;
	        if(flag!=0 && flag!=1 && flag!=2)
	        	return -EINVAL;

	        int flen=strlen(filename);
	        if(flen > MAXPATHLEN)
	        	return -ENAMETOOLONG;

	        int fd=get_empty_fd(curproc);
	        if(fd<0)
	        	return -EMFILE;

	        file_t *f=fget(-1);
	        curproc->p_files[fd]=f;
	        if(curproc->p_files[fd]==NULL)
	        	return -ENOMEM;

	        int flag1=oflags&0x700;
	        vnode_t *res_vnode;
	        int rc=open_namev(filename,flag1,&res_vnode,NULL);
	        if(rc < 0)
	        {
	             fput(curproc->p_files[fd]);
	             curproc->p_files[fd]=NULL;
	             return rc;
	        }

	        if(S_ISDIR(res_vnode->vn_mode))
	        {
	        	if(flag==O_RDWR || flag==O_WRONLY)
	        	{
	        		fput(curproc->p_files[fd]);
	                vput(res_vnode);
	                curproc->p_files[fd]=NULL;
	                return -EISDIR;
	        	}
	        }

	        if((S_ISCHR(res_vnode->vn_mode) && !(res_vnode->vn_cdev=bytedev_lookup(res_vnode->vn_devid))) || (S_ISBLK(res_vnode->vn_mode) && !(res_vnode->vn_bdev = blockdev_lookup(res_vnode->vn_devid))))
	        {
	        	fput(curproc->p_files[fd]);
	        	vput(res_vnode);
	        	curproc->p_files[fd]=NULL;
	        	return -ENXIO;
	        }

	        if(flag1==O_APPEND)
	        	curproc->p_files[fd]->f_mode=FMODE_APPEND;
	        else if(flag1==(O_APPEND | O_CREAT))
	        	curproc->p_files[fd]->f_mode=FMODE_APPEND;
	        else
	        	curproc->p_files[fd]->f_mode=0;
	        if(flag==O_RDONLY)
	        	curproc->p_files[fd]->f_mode=curproc->p_files[fd]->f_mode | FMODE_READ;
	        else if(flag==O_WRONLY)
	        	curproc->p_files[fd]->f_mode=curproc->p_files[fd]->f_mode | FMODE_WRITE;
	        else if(flag==O_RDWR)
	        	curproc->p_files[fd]->f_mode=curproc->p_files[fd]->f_mode | FMODE_READ | FMODE_WRITE;

	        curproc->p_files[fd]->f_vnode=res_vnode;
	        curproc->p_files[fd]->f_pos=0;
	        return fd;
}
