#include "kernel.h"
#include "globals.h"
#include "types.h"
#include "errno.h"

#include "util/string.h"
#include "util/printf.h"
#include "util/debug.h"

#include "fs/dirent.h"
#include "fs/fcntl.h"
#include "fs/stat.h"
#include "fs/vfs.h"
#include "fs/vnode.h"

/* This takes a base 'dir', a 'name', its 'len', and a result vnode.
 * Most of the work should be done by the vnode's implementation
 * specific lookup() function, but you may want to special case
 * "." and/or ".." here depnding on your implementation.
 *
 * If dir has no lookup(), return -ENOTDIR.
 *
 * Note: returns with the vnode refcount on *result incremented.
 */
int
lookup(vnode_t *dir, const char *name, size_t len, vnode_t **result)
{
        /*NOT_YET_IMPLEMENTED("VFS: lookup");*/
		KASSERT(NULL != dir);
		dbg(DBG_PRINT, "(GRADING2A 2.a) dir is not null\n");
		KASSERT(NULL != name);
		dbg(DBG_PRINT, "(GRADING2A 2.a) name is not null\n");
		KASSERT(NULL != result);
		dbg(DBG_PRINT, "(GRADING2A 2.a) result is not null\n");
		if(dir->vn_ops->lookup==NULL)
		    return -ENOTDIR;
		if(len > NAME_LEN)
			return -ENAMETOOLONG;
		if(len==0)
		{
			*result=vget(dir->vn_fs,dir->vn_vno);
			return 0;
		}
        return dir->vn_ops->lookup(dir,name,len,result);
}


/* When successful this function returns data in the following "out"-arguments:
 *  o res_vnode: the vnode of the parent directory of "name"
 *  o name: the `basename' (the element of the pathname)
 *  o namelen: the length of the basename
 *
 * For example: dir_namev("/s5fs/bin/ls", &namelen, &name, NULL,
 * &res_vnode) would put 2 in namelen, "ls" in name, and a pointer to the
 * vnode corresponding to "/s5fs/bin" in res_vnode.
 *
 * The "base" argument defines where we start resolving the path from:
 * A base value of NULL means to use the process's current working directory,
 * curproc->p_cwd.  If pathname[0] == '/', ignore base and start with
 * vfs_root_vn.  dir_namev() should call lookup() to take care of resolving each
 * piece of the pathname.
 *
 * Note: A successful call to this causes vnode refcount on *res_vnode to
 * be incremented.
 */
int
dir_namev(const char *pathname, size_t *namelen, const char **name,
          vnode_t *base, vnode_t **res_vnode)
{
        /*NOT_YET_IMPLEMENTED("VFS: dir_namev");*/
			KASSERT(NULL != pathname);
			dbg(DBG_PRINT, "(GRADING2A 2.b) pathname is not null\n");
			KASSERT(NULL != res_vnode);
			dbg(DBG_PRINT, "(GRADING2A 2.b) res_vnode is not null\n");
			KASSERT(NULL != namelen);
			dbg(DBG_PRINT, "(GRADING2A 2.b) namelen is not null\n");
			KASSERT(NULL != name);
			dbg(DBG_PRINT, "(GRADING2A 2.b) name is not null\n");

			int index=0;
			if(base==NULL)
				base=curproc->p_cwd;
			if(pathname[0]=='/')
			{
				base=vfs_root_vn;
				index=1;
			}
			int i=0,rc=0,j=0,count=0,k=0;
			for(j=0;pathname[j]!='\0';j++)
			{
				if(pathname[j]=='/')
					count++;
			}
			for(k=0;k<=count;k++)
			{
				for(i=0;pathname[index+i]!='/';i++)
				{
					if(pathname[index+i]=='\0')
					{
						if(i>NAME_LEN)
							return -ENAMETOOLONG;
						*res_vnode=vget(base->vn_fs,base->vn_vno);
						KASSERT(NULL != *res_vnode);
						dbg(DBG_PRINT, "(GRADING2A 2.b) res_vnode is not null\n");
						if(S_ISDIR((*res_vnode)->vn_mode))
						{
							*name=&pathname[index];
							*namelen=i;
							return 0;
						}
						else
						{
							vput(*res_vnode);
							return -ENOTDIR;
						}
					}
				}
				rc=lookup(base,&pathname[index],i,res_vnode);
				if(rc<0)
					return rc;

				vput(*res_vnode);
				base=*res_vnode;
				index=index+i+1;
			}
			return 0;
}

/* This returns in res_vnode the vnode requested by the other parameters.
 * It makes use of dir_namev and lookup to find the specified vnode (if it
 * exists).  flag is right out of the parameters to open(2); see
 * <weenix/fnctl.h>.  If the O_CREAT flag is specified, and the file does
 * not exist call create() in the parent directory vnode.
 *
 * Note: Increments vnode refcount on *res_vnode.
 */
int
open_namev(const char *pathname, int flag, vnode_t **res_vnode, vnode_t *base)
{
        /*NOT_YET_IMPLEMENTED("VFS: open_namev");*/
		const char *name;
		size_t namelen;
		int rc=0;
		rc=dir_namev(pathname,&namelen,&name,base,res_vnode);
		if(rc<0)
			return rc;
		vput(*res_vnode);
		rc=lookup(*res_vnode,name,namelen,res_vnode);
		if(rc==-ENOENT)
		{
			if((flag & O_CREAT)==O_CREAT)
			{
				KASSERT(NULL != (*res_vnode)->vn_ops->create);
				dbg(DBG_PRINT, "(GRADING2A 2.c) vnode's create method is not null\n");
				rc=(*res_vnode)->vn_ops->create(*res_vnode,name,namelen,res_vnode);
			}
		}
		return rc;
}

#ifdef __GETCWD__
/* Finds the name of 'entry' in the directory 'dir'. The name is writen
 * to the given buffer. On success 0 is returned. If 'dir' does not
 * contain 'entry' then -ENOENT is returned. If the given buffer cannot
 * hold the result then it is filled with as many characters as possible
 * and a null terminator, -ERANGE is returned.
 *
 * Files can be uniquely identified within a file system by their
 * inode numbers. */
int
lookup_name(vnode_t *dir, vnode_t *entry, char *buf, size_t size)
{
        NOT_YET_IMPLEMENTED("GETCWD: lookup_name");
        return -ENOENT;
}


/* Used to find the absolute path of the directory 'dir'. Since
 * directories cannot have more than one link there is always
 * a unique solution. The path is writen to the given buffer.
 * On success 0 is returned. On error this function returns a
 * negative error code. See the man page for getcwd(3) for
 * possible errors. Even if an error code is returned the buffer
 * will be filled with a valid string which has some partial
 * information about the wanted path. */
ssize_t
lookup_dirpath(vnode_t *dir, char *buf, size_t osize)
{
        NOT_YET_IMPLEMENTED("GETCWD: lookup_dirpath");

        return -ENOENT;
}
#endif /* __GETCWD__ */
