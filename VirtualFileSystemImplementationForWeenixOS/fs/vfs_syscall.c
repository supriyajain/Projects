/*
 *  FILE: vfs_syscall.c
 *  AUTH: mcc | jal
 *  DESC:
 *  DATE: Wed Apr  8 02:46:19 1998
 *  $Id: vfs_syscall.c,v 1.1 2012/10/10 20:06:46 william Exp $
 */

#include "kernel.h"
#include "errno.h"
#include "globals.h"
#include "fs/vfs.h"
#include "fs/file.h"
#include "fs/vnode.h"
#include "fs/vfs_syscall.h"
#include "fs/open.h"
#include "fs/fcntl.h"
#include "fs/lseek.h"
#include "mm/kmalloc.h"
#include "util/string.h"
#include "util/printf.h"
#include "fs/stat.h"
#include "util/debug.h"

/* To read a file:
 *      o fget(fd)
 *      o call its virtual read f_op
 *      o update f_pos
 *      o fput() it
 *      o return the number of bytes read, or an error
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EBADF
 *        fd is not a valid file descriptor or is not open for reading.
 *      o EISDIR
 *        fd refers to a directory.
 *
 * In all cases, be sure you do not leak file refcounts by returning before
 * you fput() a file that you fget()'ed.
 */
int
do_read(int fd, void *buf, size_t nbytes)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_read");*/
		int op=0;
		if(fd<0)
			return -EBADF;
		if(fd>=NFILES)
			return -EBADF;
		file_t *f=fget(fd);
	    if(f!=NULL)
	    {
	       if(S_ISDIR(f->f_vnode->vn_mode))
	       {
	    	   fput(f);
	    	   return -EISDIR;
	       }
	       if(f->f_mode & FMODE_READ)
	       {
	    	   op=f->f_vnode->vn_ops->read(f->f_vnode,f->f_pos,buf, nbytes);
	    	   f->f_pos+=op;
	    	   fput(f);
	    	   return op;
	       }
	       else
	       {
	    	   fput(f);
	    	   return -EBADF;
	       }
	    }
	    else
	    	return -EBADF;
	    return op;
}

/* Very similar to do_read.  Check f_mode to be sure the file is writable.  If
 * f_mode & FMODE_APPEND, do_lseek() to the end of the file, call the write
 * f_op, and fput the file.  As always, be mindful of refcount leaks.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EBADF
 *        fd is not a valid file descriptor or is not open for writing.
 */
int
do_write(int fd, const void *buf, size_t nbytes)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_write");*/
		int op=0;
		if(fd<0)
			return -EBADF;
		if(fd>=NFILES)
			return -EBADF;
		file_t *f=fget(fd);
	    if(f!=NULL)
	    {
	    	if(f->f_mode & FMODE_WRITE)
	        {
	    		if(f->f_mode & FMODE_APPEND)
	    		{
	    			int r=do_lseek(fd,0,SEEK_END);
	    			if(r<0)
	    			{
	    				fput(f);
	    				return r;
	    			}
	    			f->f_pos=r;
	    		}
	        	op=f->f_vnode->vn_ops->write(f->f_vnode,f->f_pos,buf, nbytes);
	        	if(op<0)
	        	{
	        		fput(f);
	        		return op;
	        	}
	        	f->f_pos+=op;
	        	KASSERT((S_ISCHR(f->f_vnode->vn_mode)) || (S_ISBLK(f->f_vnode->vn_mode)) || ((S_ISREG(f->f_vnode->vn_mode)) && (f->f_pos <= f->f_vnode->vn_len)));
	        	dbg(DBG_PRINT, "(GRADING2A 3.a) vnode is one of the ISCHR,ISBLK or ISREG and f_pos didn't exceed file's size !\n");
	        	fput(f);
	        	return op;
	        }
	        else
	        {
	        	fput(f);
	        	return -EBADF;
	        }
	    }
	    else
	    	return -EBADF;
	    return op;
}

/*
 * Zero curproc->p_files[fd], and fput() the file. Return 0 on success
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EBADF
 *        fd isn't a valid open file descriptor.
 */
int
do_close(int fd)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_close");*/
		if(fd<0)
			return -EBADF;
		if(fd>=NFILES)
			return -EBADF;

		/*file_t *f=fget(fd);
		if(f!=NULL)
		{
			curproc->p_files[fd]=0;
			fput(f);
			fput(f);
			return 0;
		}*/
		if(curproc->p_files[fd]!=NULL)
		{
			fput(curproc->p_files[fd]);
			curproc->p_files[fd]=0;
			return 0;
		}
		else
			return -EBADF;
}

/* To dup a file:
 *      o fget(fd) to up fd's refcount
 *      o get_empty_fd()
 *      o point the new fd to the same file_t* as the given fd
 *      o return the new file descriptor
 *
 * Don't fput() the fd unless something goes wrong.  Since we are creating
 * another reference to the file_t*, we want to up the refcount.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EBADF
 *        fd isn't an open file descriptor.
 *      o EMFILE
 *        The process already has the maximum number of file descriptors open
 *        and tried to open a new one.
 */
int
do_dup(int fd)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_dup");*/
		if(fd<0)
			return -EBADF;
		if(fd>=NFILES)
			return -EBADF;
		file_t *f=fget(fd);
		if(f!=NULL)
		{
			int newfd=get_empty_fd(curproc);
			if(newfd<0)
			{
				fput(f);
				return newfd;
			}
			curproc->p_files[newfd]=f;
			return newfd;
		}
		else
			return -EBADF;
}

/* Same as do_dup, but insted of using get_empty_fd() to get the new fd,
 * they give it to us in 'nfd'.  If nfd is in use (and not the same as ofd)
 * do_close() it first.  Then return the new file descriptor.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EBADF
 *        ofd isn't an open file descriptor, or nfd is out of the allowed
 *        range for file descriptors.
 */
int
do_dup2(int ofd, int nfd)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_dup2");*/
		if(ofd<0)
			return -EBADF;
		if(ofd>=NFILES)
			return -EBADF;
		if(nfd<0)
			return -EBADF;
		if(nfd>=NFILES)
			return -EBADF;

		file_t *f=fget(ofd);
		if(f!=NULL)
		{
			if(ofd == nfd) {
				fput(f);
				return nfd;
			}

			if(curproc->p_files[nfd] && ofd!=nfd)
			{
				int success=do_close(nfd);
				if(success!=0)
				{
					dbg(DBG_PRINT,"inside close failure....!!!!!");
					fput(f);
					return -EBADF;
				}
			}
			curproc->p_files[nfd]=f;
			return nfd;
		}
		else
			return -EBADF;
}

/*
 * This routine creates a special file of the type specified by 'mode' at
 * the location specified by 'path'. 'mode' should be one of S_IFCHR or
 * S_IFBLK (you might note that mknod(2) normally allows one to create
 * regular files as well-- for simplicity this is not the case in Weenix).
 * 'devid', as you might expect, is the device identifier of the device
 * that the new special file should represent.
 *
 * You might use a combination of dir_namev, lookup, and the fs-specific
 * mknod (that is, the containing directory's 'mknod' vnode operation).
 * Return the result of the fs-specific mknod, or an error.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EINVAL
 *        mode requested creation of something other than a device special
 *        file.
 *      o EEXIST
 *        path already exists.
 *      o ENOENT
 *        A directory component in path does not exist.
 *      o ENOTDIR
 *        A component used as a directory in path is not, in fact, a directory.
 *      o ENAMETOOLONG
 *        A component of path was too long.
 */
int
do_mknod(const char *path, int mode, unsigned devid)
{
    /*NOT_YET_IMPLEMENTED("VFS: do_mknod");*/
	if(mode!=S_IFCHR)
		return -EINVAL;

	int plen=strlen(path);

	if(plen < 1)
		return -EINVAL;
	if(plen > MAXPATHLEN)
		return -ENAMETOOLONG;

	vnode_t *newnode;
	size_t l;
	const char *name;

	int dir=dir_namev(path,&l,&name,NULL,&newnode);
	if(dir<0)
		return dir;
	vput(newnode);

	int a=lookup(newnode,name,l,&newnode);
	if (a==0)
	{
		vput(newnode);
		return -EEXIST;
	}
	else if(a==-ENOENT)
	{
		KASSERT(NULL != newnode->vn_ops->mknod);
		dbg(DBG_PRINT, "(GRADING2A 3.b) vnode's mknod method is not null\n");
		int r=0;
		r=newnode->vn_ops->mknod(newnode,name,l,mode,devid);
		return r;
	}

	return a;
}

/* Use dir_namev() to find the vnode of the dir we want to make the new
 * directory in.  Then use lookup() to make sure it doesn't already exist.
 * Finally call the dir's mkdir vn_ops. Return what it returns.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EEXIST
 *        path already exists.
 *      o ENOENT
 *        A directory component in path does not exist.
 *      o ENOTDIR
 *        A component used as a directory in path is not, in fact, a directory.
 *      o ENAMETOOLONG
 *        A component of path was too long.
 */
int
do_mkdir(const char *path)
{
    /*NOT_YET_IMPLEMENTED("VFS: do_mkdir");*/

	vnode_t *newnode;
	size_t l;
	const char *name;

	int plen=strlen(path);

	if(plen < 1)
		return -EINVAL;
	if(plen > MAXPATHLEN)
		return -ENAMETOOLONG;

	int dir=dir_namev(path,&l,&name,NULL,&newnode);
	if(dir<0)
		return dir;
	vput(newnode);
	int a=lookup(newnode,name,l,&newnode);
	if (a==0)
	{
		vput(newnode);
		return -EEXIST;
	}
	else if(a==-ENOENT)
	{
		KASSERT(NULL != newnode->vn_ops->mkdir);
		dbg(DBG_PRINT, "(GRADING2A 3.c) vnode's mkdir method is not null\n");
		int r=0;
		r=newnode->vn_ops->mkdir(newnode,name,l);
		return r;
	}
	return a;
}

/* Use dir_namev() to find the vnode of the directory containing the dir to be
 * removed. Then call the containing dir's rmdir v_op.  The rmdir v_op will
 * return an error if the dir to be removed does not exist or is not empty, so
 * you don't need to worry about that here. Return the value of the v_op,
 * or an error.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EINVAL
 *        path has "." as its final component.
 *      o ENOTEMPTY
 *        path has ".." as its final component.
 *      o ENOENT
 *        A directory component in path does not exist.
 *      o ENOTDIR
 *        A component used as a directory in path is not, in fact, a directory.
 *      o ENAMETOOLONG
 *        A component of path was too long.
 */
int
do_rmdir(const char *path)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_rmdir");*/
		int len=0;
		len=strlen(path);
		if(len < 1)
			return -EINVAL;
		if(len > MAXPATHLEN)
					return -ENAMETOOLONG;

		size_t namelen;
		const char *name;
		vnode_t *newnode;

		int result=dir_namev(path, &namelen, &name, NULL, &newnode);
		if(result < 0)
			return result;

		if(namelen==2)
		{
			if(name[namelen-1]=='.' && name[namelen-2]=='.')
			{
				vput(newnode);
				return -ENOTEMPTY;
			}
		}
		else if(namelen==1 && name[namelen-1]=='.')
		{
			vput(newnode);
			return -EINVAL;
		}

		KASSERT(NULL != newnode->vn_ops->rmdir);
		dbg(DBG_PRINT, "(GRADING2A 3.d) vnode's rmdir method is not null\n");
		int res=0;
		res=newnode->vn_ops->rmdir(newnode, name, namelen);
		vput(newnode);
		if(res<0)
			return res;
		return 0;
}

/*
 * Same as do_rmdir, but for files.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EISDIR
 *        path refers to a directory.
 *      o ENOENT
 *        A component in path does not exist.
 *      o ENOTDIR
 *        A component used as a directory in path is not, in fact, a directory.
 *      o ENAMETOOLONG
 *        A component of path was too long.
 */
int
do_unlink(const char *path)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_unlink");*/
		int len=0;
		len=strlen(path);
		if(len < 1)
			return -EINVAL;

		if(len > MAXPATHLEN)
			return -ENAMETOOLONG;

		size_t namelen;
		const char *name;
		vnode_t *dvnode, *fvnode;

		int result=dir_namev(path, &namelen, &name, NULL, &dvnode);
		if(result < 0)
			return result;

		result=lookup(dvnode, name, namelen, &fvnode);
		if(result < 0)
	    {
			vput(dvnode);
	        return result;
	    }

		if(S_ISDIR(fvnode->vn_mode))
		{
			vput(dvnode);
			vput(fvnode);
			return -EISDIR;
		}

		KASSERT(NULL != dvnode->vn_ops->unlink);
		dbg(DBG_PRINT, "(GRADING2A 3.e) vnode's unlink method is not null\n");
		int res_unlink=dvnode->vn_ops->unlink(dvnode, name, namelen);
		vput(dvnode);
		vput(fvnode);
		return res_unlink;
}

/* To link:
 *      o open_namev(from)
 *      o dir_namev(to)
 *      o call the destination dir's (to) link vn_ops.
 *      o return the result of link, or an error
 *
 * Remember to vput the vnodes returned from open_namev and dir_namev.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EEXIST
 *        to already exists.
 *      o ENOENT
 *        A directory component in from or to does not exist.
 *      o ENOTDIR
 *        A component used as a directory in from or to is not, in fact, a
 *        directory.
 *      o ENAMETOOLONG
 *        A component of from or to was too long.
 */
int
do_link(const char *from, const char *to)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_link");*/
		int len1=0, len2=0;

		len1=strlen(from);
		len2=strlen(to);

		if(len1 <1 || len2 <1)
			return -EINVAL;

		if(len1 > MAXPATHLEN || len2 > MAXPATHLEN)
			return -ENAMETOOLONG;

		vnode_t *newnode1;
		int result=open_namev(from,0,&newnode1,NULL);

		if(result < 0)
			return result;

		const char *name;
		vnode_t *newnode2;
		size_t pathlen;
		result=dir_namev(to,&pathlen,&name,NULL,&newnode2);
		if(result < 0)
		{
			vput(newnode1);
			return result;
		}
		vput(newnode2);
		result=lookup(newnode2,name,pathlen,&newnode2);
		if(result==0)
		{
			vput(newnode1);
			vput(newnode2);
			return -EEXIST;
		}
		else if(result==-ENOENT)
		{
			int i=newnode2->vn_ops->link(newnode1,newnode2,name,pathlen);
			vput(newnode1);
			return i;
		}
		vput(newnode1);
		return result;
}

/*      o link newname to oldname
 *      o unlink oldname
 *      o return the value of unlink, or an error
 *
 * Note that this does not provide the same behavior as the
 * Linux system call (if unlink fails then two links to the
 * file could exist).
 */
int
do_rename(const char *oldname, const char *newname)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_rename");*/
		int len1,len2;
		len1=strlen(oldname);
		len2=strlen(newname);

		if(len1 < 1 || len2 < 1)
			return -EINVAL;
		if(len1 > NAME_LEN || len2 > NAME_LEN)
			return -ENAMETOOLONG;

		do_link(oldname, newname);

		int result=do_unlink(oldname);
		return result;
}

/* Make the named directory the current process's cwd (current working
 * directory).  Don't forget to down the refcount to the old cwd (vput()) and
 * up the refcount to the new cwd (open_namev() or vget()). Return 0 on
 * success.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o ENOENT
 *        path does not exist.
 *      o ENAMETOOLONG
 *        A component of path was too long.
 *      o ENOTDIR
 *        A component of path is not a directory.
 */
int
do_chdir(const char *path)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_chdir");*/
		int len=0;
		vnode_t *newnode;

		len=strlen(path);
		if(len < 1)
			return -EINVAL;

		if(len > MAXPATHLEN)
			return -ENAMETOOLONG;

		int result=open_namev(path,0,&newnode,NULL);
		if(result < 0)
	    	return result;

	    if(!S_ISDIR(newnode->vn_mode))
	    {
	    	vput(newnode);
	    	return -ENOTDIR;
	    }

	    vput(curproc->p_cwd);

	    curproc->p_cwd=newnode;
	    return 0;
}

/* Call the readdir f_op on the given fd, filling in the given dirent_t*.
 * If the readdir f_op is successful, it will return a positive value which
 * is the number of bytes copied to the dirent_t.  You need to increment the
 * file_t's f_pos by this amount.  As always, be aware of refcounts, check
 * the return value of the fget and the virtual function, and be sure the
 * virtual function exists (is not null) before calling it.
 *
 * Return either 0 or sizeof(dirent_t), or -errno.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EBADF
 *        Invalid file descriptor fd.
 *      o ENOTDIR
 *        File descriptor does not refer to a directory.
 */
int
do_getdent(int fd, struct dirent *dirp)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_getdent");*/
		if(fd<0)
			return -EBADF;
		if(fd>=NFILES)
			return -EBADF;
		file_t *f=fget(fd);
		if(f!=NULL)
		{
			if(S_ISDIR(f->f_vnode->vn_mode))
			{
				KASSERT(NULL != f->f_vnode->vn_ops->readdir);
				dbg(DBG_PRINT, "vnode's getdent method is not null\n");
				int val=f->f_vnode->vn_ops->readdir(f->f_vnode,f->f_pos,dirp);
				f->f_pos+=val;
				fput(f);
				if(val!=0)
					return sizeof(*dirp);
				else
					return 0;
			}
			else
			{
				fput(f);
				return -ENOTDIR;
			}
		}
		else
			return -EBADF;
}

/*
 * Modify f_pos according to offset and whence.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o EBADF
 *        fd is not an open file descriptor.
 *      o EINVAL
 *        whence is not one of SEEK_SET, SEEK_CUR, SEEK_END; or the resulting
 *        file offset would be negative.
 */
int
do_lseek(int fd, int offset, int whence)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_lseek");*/
		if(whence != SEEK_SET && whence != SEEK_CUR && whence != SEEK_END)
			return -EINVAL;
		if(fd<0)
			return -EBADF;
		if(fd>=NFILES)
			return -EBADF;
		file_t *f=fget(fd);
		if(f!=NULL)
		{


				int val=0;
				if(whence==SEEK_SET)
				{
					val=offset;
				}
				else if(whence==SEEK_CUR)
				{
					val=f->f_pos+offset;
				}
				else if(whence==SEEK_END)
				{
					val=f->f_vnode->vn_len+offset;
				}
				f->f_pos=val;

				if(val<0)
				{
					f->f_pos=0;
					fput(f);
					return -EINVAL;
				}
				else
				{
					fput(f);
					return val;
				}

		}
		else
		{
			return -EBADF;
		}
}

/*
 * Find the vnode associated with the path, and call the stat() vnode operation.
 *
 * Error cases you must handle for this function at the VFS level:
 *      o ENOENT
 *        A component of path does not exist.
 *      o ENOTDIR
 *        A component of the path prefix of path is not a directory.
 *      o ENAMETOOLONG
 *        A component of path was too long.
 */
int
do_stat(const char *path, struct stat *buf)
{
        /*NOT_YET_IMPLEMENTED("VFS: do_stat");*/
		int len=0;
		len=strlen(path);
		if(len < 1)
			return -EINVAL;
		if(len > MAXPATHLEN)
			return -ENAMETOOLONG;

		vnode_t *newnode;
		size_t pathlen;
		const char *name;
		int result=dir_namev(path,&pathlen,&name,NULL,&newnode);
		if(result < 0)
			return result;

		vput(newnode);

		int a=lookup(newnode,name,pathlen,&newnode);
		if(a < 0)
			return a;

		KASSERT(NULL != newnode->vn_ops->stat);
		dbg(DBG_PRINT, "(GRADING2A 3.f) vnode's stat method is not null\n");
		int c=newnode->vn_ops->stat(newnode,buf);
		vput(newnode);
		return c;
}

#ifdef __MOUNTING__
/*
 * Implementing this function is not required and strongly discouraged unless
 * you are absolutely sure your Weenix is perfect.
 *
 * This is the syscall entry point into vfs for mounting. You will need to
 * create the fs_t struct and populate its fs_dev and fs_type fields before
 * calling vfs's mountfunc(). mountfunc() will use the fields you populated
 * in order to determine which underlying filesystem's mount function should
 * be run, then it will finish setting up the fs_t struct. At this point you
 * have a fully functioning file system, however it is not mounted on the
 * virtual file system, you will need to call vfs_mount to do this.
 *
 * There are lots of things which can go wrong here. Make sure you have good
 * error handling. Remember the fs_dev and fs_type buffers have limited size
 * so you should not write arbitrary length strings to them.
 */
int
do_mount(const char *source, const char *target, const char *type)
{
        NOT_YET_IMPLEMENTED("MOUNTING: do_mount");
        return -EINVAL;
}

/*
 * Implementing this function is not required and strongly discouraged unless
 * you are absolutley sure your Weenix is perfect.
 *
 * This function delegates all of the real work to vfs_umount. You should not worry
 * about freeing the fs_t struct here, that is done in vfs_umount. All this function
 * does is figure out which file system to pass to vfs_umount and do good error
 * checking.
 */
int
do_umount(const char *target)
{
        NOT_YET_IMPLEMENTED("MOUNTING: do_umount");
        return -EINVAL;
}
#endif
