#include "globals.h"
#include "errno.h"

#include "main/interrupt.h"

#include "proc/sched.h"
#include "proc/kthread.h"

#include "util/init.h"
#include "util/debug.h"

static ktqueue_t kt_runq;

static __attribute__((unused)) void
sched_init(void)
{
        sched_queue_init(&kt_runq);
}
init_func(sched_init);



/*** PRIVATE KTQUEUE MANIPULATION FUNCTIONS ***/
/**
 * Enqueues a thread onto a queue.
 *
 * @param q the queue to enqueue the thread onto
 * @param thr the thread to enqueue onto the queue
 */
static void
ktqueue_enqueue(ktqueue_t *q, kthread_t *thr)
{
        list_insert_head(&q->tq_list, &thr->kt_qlink);
        thr->kt_wchan = q;
        q->tq_size++;
}

/**
 * Dequeues a thread from the queue.
 *
 * @param q the queue to dequeue a thread from
 * @return the thread dequeued from the queue
 */
static kthread_t *
ktqueue_dequeue(ktqueue_t *q)
{
        kthread_t *thr;
        list_link_t *link;

        if (list_empty(&q->tq_list))
                return NULL;

        link = q->tq_list.l_prev;
        thr = list_item(link, kthread_t, kt_qlink);
        list_remove(link);
        thr->kt_wchan = NULL;

        q->tq_size--;

        return thr;
}

/**
 * Removes a given thread from a queue.
 *
 * @param q the queue to remove the thread from
 * @param thr the thread to remove from the queue
 */
static void
ktqueue_remove(ktqueue_t *q, kthread_t *thr)
{
        KASSERT(thr->kt_qlink.l_next && thr->kt_qlink.l_prev);
        list_remove(&thr->kt_qlink);
        thr->kt_wchan = NULL;
        q->tq_size--;
}

/*** PUBLIC KTQUEUE MANIPULATION FUNCTIONS ***/
void
sched_queue_init(ktqueue_t *q)
{
        list_init(&q->tq_list);
        q->tq_size = 0;
}

int
sched_queue_empty(ktqueue_t *q)
{
        return list_empty(&q->tq_list);
}

/*
 * Updates the thread's state and enqueues it on the given
 * queue. Returns when the thread has been woken up with wakeup_on or
 * broadcast_on.
 *
 * Use the private queue manipulation functions above.
 */
void
sched_sleep_on(ktqueue_t *q)
{
        /* NOT_YET_IMPLEMENTED("PROCS: sched_sleep_on");*/
		curthr->kt_state=KT_SLEEP;
		ktqueue_enqueue(q,curthr);
		sched_switch();
		/*while(curthr->kt_state!=KT_RUN);*/
}


/*
 * Similar to sleep on, but the sleep can be cancelled.
 *
 * Don't forget to check the kt_cancelled flag at the correct times.
 *
 * Use the private queue manipulation functions above.
 */
int
sched_cancellable_sleep_on(ktqueue_t *q)
{
        /* NOT_YET_IMPLEMENTED("PROCS: sched_cancellable_sleep_on");*/
		if(curthr->kt_cancelled!=1)
		{
			curthr->kt_state=KT_SLEEP_CANCELLABLE;
			ktqueue_enqueue(q,curthr);
			sched_switch();

			if (curthr->kt_cancelled==1)
	                return -EINTR;
	        return 0;
		}
		else
			return -EINTR;
}

kthread_t *
sched_wakeup_on(ktqueue_t *q)
{
        /* NOT_YET_IMPLEMENTED("PROCS: sched_wakeup_on");*/
		if(sched_queue_empty(q))
			return NULL;
		else
		{
			kthread_t *t=ktqueue_dequeue(q);
			if(t!=NULL)
			{
				KASSERT(t->kt_state==KT_SLEEP || t->kt_state==KT_SLEEP_CANCELLABLE);
				dbgq(DBG_CORE, "(GRADING1 4.a) Removed thread's status is either sleep or cancellable sleep right now\n");
				sched_make_runnable(t);
			}
			return t;
		}
}

void
sched_broadcast_on(ktqueue_t *q)
{
        /* NOT_YET_IMPLEMENTED("PROCS: sched_broadcast_on"); */
		while(sched_wakeup_on(q));
}

/*
 * If the thread's sleep is cancellable, we set the kt_cancelled
 * flag and remove it from the queue. Otherwise, we just set the
 * kt_cancelled flag and leave the thread on the queue.
 *
 * Remember, unless the thread is in the KT_NO_STATE or KT_EXITED
 * state, it should be on some queue. Otherwise, it will never be run
 * again.
 */
void
sched_cancel(struct kthread *kthr)
{
        /* NOT_YET_IMPLEMENTED("PROCS: sched_cancel");*/
		if(kthr->kt_state!=KT_NO_STATE && kthr->kt_state!=KT_EXITED)
		{
			kthr->kt_cancelled=1;
			if(kthr->kt_state==KT_SLEEP_CANCELLABLE)
			{
				ktqueue_t *q=kthr->kt_wchan;
				ktqueue_remove(q,kthr);
				sched_make_runnable(kthr);
			}
		}
}

/*
 * In this function, you will be modifying the run queue, which can
 * also be modified from an interrupt context. In order for thread
 * contexts and interrupt contexts to play nicely, you need to mask
 * all interrupts before reading or modifying the run queue and
 * re-enable interrupts when you are done. This is analagous to
 * locking a mutex before modifying a data structure shared between
 * threads. Masking interrupts is accomplished by setting the IPL to
 * high.
 *
 * Once you have masked interrupts, you need to remove a thread from
 * the run queue and switch into its context from the currently
 * executing context.
 *
 * If there are no threads on the run queue (assuming you do not have
 * any bugs), then all kernel threads are waiting for an interrupt
 * (for example, when reading from a block device, a kernel thread
 * will wait while the block device seeks). You will need to re-enable
 * interrupts and wait for one to occur in the hopes that a thread
 * gets put on the run queue from the interrupt context.
 *
 * The proper way to do this is with the intr_wait call. See
 * interrupt.h for more details on intr_wait.
 *
 * Note: When waiting for an interrupt, don't forget to modify the
 * IPL. If the IPL of the currently executing thread masks the
 * interrupt you are waiting for, the interrupt will never happen, and
 * your run queue will remain empty. This is very subtle, but
 * _EXTREMELY_ important.
 *
 * Note: Don't forget to set curproc and curthr. When sched_switch
 * returns, a different thread should be executing than the thread
 * which was executing when sched_switch was called.
 *
 * Note: The IPL is process specific.
 */
void
sched_switch(void)
{
        /* NOT_YET_IMPLEMENTED("PROCS: sched_switch");*/
	    uint8_t oipl = intr_getipl();
	    intr_setipl(IPL_HIGH);
	    kthread_t *ot = curthr;
	    kthread_t *t=ktqueue_dequeue(&kt_runq);
        if(t==NULL)
	    {
	    	while (t==NULL)
	        {
	    		intr_setipl(IPL_LOW);
	    		intr_wait();
	    		intr_setipl(IPL_HIGH);
	            t=ktqueue_dequeue(&kt_runq);
	        }
	    }
        curthr=t;
	    curproc=t->kt_proc;
	    intr_setipl(oipl);
	    context_switch(&ot->kt_ctx, &curthr->kt_ctx);
}

/*
 * Since we are modifying the run queue, we _MUST_ set the IPL to high
 * so that no interrupts happen at an inopportune moment.

 * Remember to restore the original IPL before you return from this
 * function. Otherwise, we will not get any interrupts after returning
 * from this function.
 *
 * Using intr_disable/intr_enable would be equally as effective as
 * modifying the IPL in this case. However, in some cases, we may want
 * more fine grained control, making modifying the IPL more
 * suitable. We modify the IPL here for consistency.
 */
void
sched_make_runnable(kthread_t *thr)
{
		/* NOT_YET_IMPLEMENTED("PROCS: sched_make_runnable");*/
		uint8_t oipl=intr_getipl();
		intr_setipl(IPL_HIGH);
		KASSERT(&kt_runq != thr->kt_wchan);
		dbgq(DBG_CORE, "(GRADING1 4.b) Thread is not blocked on run queue\n");
		thr->kt_state=KT_RUN;
		ktqueue_enqueue(&kt_runq,thr);
		intr_setipl(oipl);
}