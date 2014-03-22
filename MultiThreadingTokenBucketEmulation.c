#define _POSIX_PTHREAD_SEMANTICS

#include<stdio.h>
#include<sys/types.h>
#include<sys/time.h>
#include<pthread.h>
#include<signal.h>
#include<math.h>
#include<string.h>
#include<stdlib.h>
#include "my402list.h"

double getTime()
{
	struct timeval tv;	
	gettimeofday(&tv,NULL);
	long s= ((long) tv.tv_sec * 1000000) + tv.tv_usec;
	double ms=s*1.0/1000.0;
	return ms;
}

int TokenCount=0;
My402List Q1,Q2,datalist;
My402ListElem* myelem; 
double EmuStartT,EmuEndT;

typedef struct tagPacket
{
	int ID;
	int no_of_tokens;
	float service_time;
	double arrival_time;
	double Q1_arrival_time;
	double Q2_arrival_time;
	double TimeInQ1;
	double TimeInQ2;
}packet;

typedef struct filedata
{
	double lam;
	double servTime;
	int nToken;
}data;

pthread_t threads[4];
pthread_mutex_t mutex=PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t serv=PTHREAD_COND_INITIALIZER;	
sigset_t new;

double lambda=0.5;
double mu=0.35;
double r=1.5;
int B=10;
int p=3;     
int num=20;   

int Queue2=0;
int Pdropped=0;
int Tdropped=0;
int PID=0;
int TID=0;
int Pserved=0;
int flag=0;
int fileflag=0;
int sleepflag=0;

double avgIntarrTime=0.0;
double avgPckQ1=0.0;
double avgPckQ2=0.0;
double avgPckServ=0.0;
double avgPckSys=0.0;
double avgStd=0.0;

void *arrival();
void *token_bucket();
void *server();
void *handler();//, interrupt();
void PrintStat();
int ReadFile(FILE*);
//int CheckValid(char*);

//struct sigaction act;

int main(int argc,char *argv[])
{
	FILE *f;
	if(argc>1)
	{
		int i;
		for(i=1;i<argc;i++)
		{
			if(strcmp(argv[i],"-t")==0)
			{
				f=fopen(argv[++i],"r");	
				if(f==NULL)
				{
					fprintf(stderr,"\nError while opening the file !");
					exit(1);
				}
				if(ReadFile(f)==1)
					fileflag=1;
			}
			if(fileflag==0)
			{
				if(strcmp(argv[i],"-lambda")==0)
				{
					double l=atof(argv[++i]);
					if(l>0.0)
						lambda=l;
					else
					{
						fprintf(stderr,"\ninappropriate value for lambda");
						exit(1);
					}
				}
				if(strcmp(argv[i],"-mu")==0)	
				{
					double m=atof(argv[++i]);
					if(m>0.0)
						mu=m;	
					else
					{
						fprintf(stderr,"\ninappropriate value for mu");
						exit(1);
					}			
				}
				if(strcmp(argv[i],"-P")==0)	
				{
					double pn=atoi(argv[++i]);
					if(pn>0 && pn<2147483647)
						p=pn;
					else
					{
						fprintf(stderr,"\ninappropriate value for P");
						exit(1);
					}			
				}
				if(strcmp(argv[i],"-n")==0)	
				{
					double n=atoi(argv[++i]);
					if(n>0 && n<2147483647)
						num=n;
					else
					{
						fprintf(stderr,"\ninappropriate value for n");
						exit(1);
					}			
				}
			}
			if(strcmp(argv[i],"-r")==0)	
			{
				double rt=atof(argv[++i]);
				if(rt>0.0)
				{
					if(rt>10.0)
						r=10.0;
					else
						r=rt;
				}
				else
				{
					fprintf(stderr,"\ninappropriate value for r");
					exit(1);
				}			
			}
			if(strcmp(argv[i],"-B")==0)	
			{
				double bn=atoi(argv[++i]);
				if(bn>0 && bn<2147483647)
					B=bn;				
				else
				{
					fprintf(stderr,"\ninappropriate value for B");
					exit(1);
				}			

			}
		}
	}

	EmuStartT=getTime();
	printf("\n00000000.000ms: emulation begins");		
	
	sigemptyset(&new);
	sigaddset(&new,SIGINT);
	pthread_sigmask(SIG_BLOCK, &new, NULL);
	pthread_create(&threads[0],0,handler,0);
	
	pthread_create(&threads[1],0,arrival,0);
	pthread_create(&threads[2],0,token_bucket,0);
	pthread_create(&threads[3],0,server,0);

	int i;
	for(i=0;i<4;i++)
		pthread_join(threads[i],0);	
	return 0;
}

void *arrival()
{
	int j;
	double time,Q1LT;
	double lastTime=EmuStartT;

	if(fileflag==1 && My402ListLength(&datalist)>0)
	{		
		myelem=My402ListFirst(&datalist);
	}
	for(j=0;j<num;j++)
	{
		if(fileflag==1)
		{
			struct filedata *dt=(struct filedata*)myelem->obj;
			lambda=dt->lam;
			p=dt->nToken;
			mu=dt->servTime;
			myelem=My402ListNext(&datalist,myelem);
		}
		if(flag!=1)
		{
			struct timeval tv1;
			double sleeptimeinsec=1/lambda;
			tv1.tv_sec=(int)sleeptimeinsec;
			tv1.tv_usec=(sleeptimeinsec-tv1.tv_sec)*1000000;
			select(0,NULL,NULL,NULL,&tv1);
			time=getTime();
			PID++;

			if(p>=B)
			{
				Pdropped++;
				printf("\n%012.3fms: P%d arrives, needs %d tokens, dropped",(time-EmuStartT),PID,p);
				avgIntarrTime=avgIntarrTime+(time-lastTime);
				lastTime=time;
				continue;
			}

			packet *pack=(packet *)malloc(sizeof(packet));
			if(pack!=NULL)
			{
				pack->ID=PID;
				pack->no_of_tokens=p;
				pack->service_time=mu;
	
				printf("\n%012.3fms: P%d arrives, needs %d tokens, inter-arrival time = %.3fms",(time-EmuStartT),PID,p,(time-lastTime));
				avgIntarrTime=avgIntarrTime+(time-lastTime);
				lastTime=time;
				pack->arrival_time=time;

				pthread_mutex_lock(&mutex);
				My402ListAppend(&Q1,pack);
				pack->Q1_arrival_time=getTime();
				printf("\n%012.3fms: P%d enters Q1",(pack->Q1_arrival_time-EmuStartT),PID);
				if(My402ListLength(&Q1)==1)
				{
					if(TokenCount>=pack->no_of_tokens)
					{
						My402ListElem *m=My402ListLast(&Q1);
						My402ListUnlink(&Q1,m);
						TokenCount=TokenCount-pack->no_of_tokens;
						Q1LT=getTime();
						printf("\n%012.3fms: P%d leaves Q1, time in Q1 = %.3fms, token bucket has now %d tokens",(Q1LT-EmuStartT),pack->ID,(Q1LT-pack->Q1_arrival_time),TokenCount);
						pack->TimeInQ1=Q1LT-pack->Q1_arrival_time;

						My402ListAppend(&Q2,pack);
						pack->Q2_arrival_time=getTime();
						printf("\n%012.3fms: P%d enters Q2",(pack->Q2_arrival_time-EmuStartT),pack->ID);
					
						Queue2++;
						if(Queue2==1)
							pthread_cond_signal(&serv);
					}
				}		
				pthread_mutex_unlock(&mutex);
			}
		}
	}
	return 0;	
}

void *token_bucket()
{
	int c;
	double TokenT,Q1LT;
	while(1)
	{
		if(flag!=1)
		{
			struct timeval tv1;
			double sleeptimeinsec=1/r;
			tv1.tv_sec=(int)sleeptimeinsec;
			tv1.tv_usec=(sleeptimeinsec-tv1.tv_sec)*1000000;
			select(0,NULL,NULL,NULL,&tv1);

			pthread_mutex_lock(&mutex);
			if(TokenCount<B)
			{
				TID++;
				TokenCount++;
				TokenT=getTime();
				printf("\n%012.3fms: token t%d arrives, token bucket now has %d tokens",(TokenT-EmuStartT),TID,TokenCount);
				if(!My402ListEmpty(&Q1))
				{
					struct tagPacket* pc=(struct tagPacket*)My402ListFirst(&Q1)->obj;
					c=pc->no_of_tokens;
					if(TokenCount>=c)
					{
						My402ListElem *m=My402ListFirst(&Q1);
						My402ListUnlink(&Q1,m);
						TokenCount=TokenCount-c;
						Q1LT=getTime();
						printf("\n%012.3fms: P%d leaves Q1, time in Q1 = %.3fms, token bucket has now %d tokens",(Q1LT-EmuStartT),pc->ID,(Q1LT-pc->Q1_arrival_time),TokenCount);
						pc->TimeInQ1=Q1LT-pc->Q1_arrival_time;
						
						My402ListAppend(&Q2,pc);
						pc->Q2_arrival_time=getTime();
						printf("\n%012.3fms: P%d enters Q2",(pc->Q2_arrival_time-EmuStartT),pc->ID);

						Queue2++;
						if(Queue2==1)
							pthread_cond_signal(&serv);
					}
				}
			}
			else
			{
				TID++;
				Tdropped++;
				TokenT=getTime();
				printf("\n%012.3fms: token t%d arrives, dropped",(TokenT-EmuStartT),TID);

			}
			pthread_mutex_unlock(&mutex);
			if(My402ListLength(&Q1)<1 && PID==num)
			{
				if(My402ListLength(&Q2)<1)
				{
					EmuEndT=getTime();
					PrintStat();
					pthread_cancel(threads[3]);
					pthread_cancel(threads[0]);
				}	
				pthread_exit((void*)1);
			}
		}
	}
	return 0;
}

void *server()
{
	int x;
	double ServAT,ServLT;
	for(x=0;x<num;x++)
	{
		if(flag!=1)
		{
			pthread_mutex_lock(&mutex);
			while(Queue2==0)
				pthread_cond_wait(&serv,&mutex);
				
			//if(My402ListLength(&Q1)<1 && PID==num)
				//pthread_cancel(threads[2]);
			struct tagPacket* pc=(struct tagPacket*)My402ListFirst(&Q2)->obj;
			My402ListElem *m=My402ListFirst(&Q2);
			My402ListUnlink(&Q2,m);
			Queue2--;
			pthread_mutex_unlock(&mutex);
			ServAT=getTime();
			printf("\n%012.3fms: P%d begin service at S, time in Q2 = %.3fms",(ServAT-EmuStartT),pc->ID,(ServAT-pc->Q2_arrival_time));
				pc->TimeInQ2=ServAT-pc->Q2_arrival_time;
	
			struct timeval tv1;
			double sleeptimeinsec=1/pc->service_time;
			tv1.tv_sec=(int)sleeptimeinsec;
			tv1.tv_usec=(sleeptimeinsec-tv1.tv_sec)*1000000;
			sleepflag=1;
			select(0,NULL,NULL,NULL,&tv1);
			
			ServLT=getTime();
			sleepflag=0;
			printf("\n%012.3fms: p%d departs from S, service time = %.3fms, time in system = %.3fms",(ServLT-EmuStartT),pc->ID,(ServLT-ServAT),(ServLT-pc->arrival_time));
			Pserved++;
			avgPckQ1=avgPckQ1+pc->TimeInQ1;
			avgPckQ2=avgPckQ2+pc->TimeInQ2;
			avgPckServ=avgPckServ+(ServLT-ServAT);
			avgPckSys=avgPckSys+(ServLT-pc->arrival_time);
			avgStd=avgStd+((ServLT-pc->arrival_time)*(ServLT-pc->arrival_time));
			//free(&pc);
		}
		else
			break;
	}
	EmuEndT=getTime();
	PrintStat();
	pthread_cancel(threads[0]);
	return 0;
}

void PrintStat()
{
	double EmuTime=EmuEndT-EmuStartT;
	double var;

	printf("\n\nStatistics:");
	if(PID>0)
		printf("\n\n\taverage packet inter-arrival time = %.6g",(avgIntarrTime/PID)*0.001);
	else
		printf("\n\n\taverage packet inter-arrival time = N/A");
	if(Pserved>0)
		printf("\n\taverage packet service time = %.6g",(avgPckServ/Pserved)*0.001);
	else
		printf("\n\taverage packet service time = N/A");
	if(Pserved>0)
	{
		printf("\n\n\taverage number of packets in Q1 = %.6g",(avgPckQ1/EmuTime));	
		printf("\n\taverage number of packets in Q2 = %.6g",(avgPckQ2/EmuTime));	
		printf("\n\taverage number of packets at S = %.6g",(avgPckServ/EmuTime));	
		printf("\n\n\taverage time a packet spent in system = %.6g",(avgPckSys/Pserved)*0.001);
			
		var=(avgStd/Pserved)-((avgPckSys/Pserved)*(avgPckSys/Pserved));
		printf("\n\tstandard deviation for time spent in system = %.6g",sqrt(var)*0.001);
	}
	else
	{
		printf("\n\n\taverage number of packets in Q1 = N/A");	
		printf("\n\taverage number of packets in Q2 = N/A");	
		printf("\n\taverage number of packets at S = N/A");	
		printf("\n\n\taverage time a packet spent in system = N/A");
		printf("\n\tstandard deviation for time spent in system = N/A");	
	}
	if(TID>0)
	{
		double tem=(double)Tdropped/TID;
		printf("\n\n\ttoken drop probability = %.6g",tem);
	}
	else
		printf("\n\n\ttoken drop probability = N/A");
	if(PID>0)
	{
		double pem=(double)Pdropped/PID;
		printf("\n\tpacket drop probability = %.6g\n",pem);
	}
	else
		printf("\n\tpacket drop probability = N/A\n");
}

void *handler()
{
	int sig;
	sigwait(&new, &sig);
	flag=1;
	pthread_cancel(threads[1]);
	pthread_cancel(threads[2]);
	if(My402ListLength(&Q1)>0)
		My402ListUnlinkAll(&Q1);
	if(My402ListLength(&Q2)>0)
		My402ListUnlinkAll(&Q2);
	if(Queue2==0 || sleepflag==1)
	{
		EmuEndT=getTime();
		PrintStat();
		pthread_cancel(threads[3]);
	}
	pthread_exit((void*) 1);
	return 0;
}

/*void *handler(char argv1[])
{
	act.sa_handler=interrupt;
	sigaction(SIGINT, &act, NULL);
	pthread_sigmask(SIG_UNBLOCK, &new, NULL);
	sleep(5000000);
	return 0;
}

void interrupt(int sig)
{
	//printf("\nthread caught signal\n");
	flag=1;
	//pthread_kill(threads[2],sig);
	//pthread_kill(threads[1],sig);
	pthread_cancel(threads[1]);
	pthread_cancel(threads[2]);
	if(My402ListLength(&Q1)>0)
		My402ListUnlinkAll(&Q1);
	printf("\nQ1 is now empty..!!! %d",My402ListLength(&Q1));
	if(My402ListLength(&Q2)>0)
		My402ListUnlinkAll(&Q2);
	printf("\nQ2 is now empty...!!! %d\n\n\n",My402ListLength(&Q2));
	if(Queue2==0)
		pthread_cond_signal(&serv);	
}

int ReadFile(FILE *f)
{
	char numb[11];
	if(fgets(numb,sizeof(numb),f)!=NULL)
	{
		int number=atoi(numb);
		if(number>0 && number<2147483647)
		{
			num=number;
			//printf("\nnumber= %d num=%d",number,num);
			while(!feof(f))
			{
				char buf[1026];
				if(fgets(buf,1026,f)!=NULL)
				{
					data *md=(data*)malloc(sizeof(data));
					char *start_ptr=buf;
					char *tab_ptr=strchr(start_ptr,'\t');
					if(tab_ptr!=NULL)
						*tab_ptr++='\0';
					else
					{
						//printf("\nChecking for 1st tab");
						fprintf(stderr,"\nData in the file is inappropriate 1st tab!");
						return 0;
					}
					if(CheckValid(start_ptr)==1)
					{
						double t=atof(start_ptr);
						if(t>0.0)
							md->lam=t;
						//printf("\nlambda setted=%f",md->lam);
					}					
				
					start_ptr=tab_ptr;
	                                tab_ptr=strchr(start_ptr,'\t');
        	                        if(tab_ptr!=NULL)
	                	                *tab_ptr++='\0';
	                                else
        	                        {
                	                        //printf("\nchecking for 2nd tab");
						fprintf(stderr,"\nData in the file is inappropriate 2nd tab!");
                        	                return 0;
                               		 }
	                                if(CheckValid(start_ptr)==1)
        	                        {
	                                	int t=atoi(start_ptr);
						if(t>0 && t<2147483647)
						{
				        		md->nToken=t;
							reqtoken=reqtoken+t;
							//printf("\nnToken setted=%d",md->nToken);
						}
					}

					start_ptr=tab_ptr;
	                                if(CheckValid(start_ptr)==1)
        	                        {
	                                	double t=atof(start_ptr);
						if(t>0.0)
					        	md->servTime=t;
						//printf("\nservTime setted=%f",md->servTime);
					}
					
					My402ListAppend(&datalist,md);
               			}
			}
         	}
		else
		{
			//printf("\nnumber is wrong, numb=%s number=%d",numb,number);
			fprintf(stderr,"\nData in file is inappropriate ! number");
			return 0;
		}
	}
	return 1;
}*/

int ReadFile(FILE *f)
{
	char numb[11];
	if(fgets(numb,sizeof(numb),f)!=NULL)
	{
		int number=atoi(numb);
		if(number>0 && number<2147483647)
		{
			num=number;
			while(!feof(f))
			{
				char buf[1026];
				if(fgets(buf,1026,f)!=NULL)
				{
					data *md=(data*)malloc(sizeof(data));
					char *start_ptr=buf;
					char* s[3];
					int h=0;
					while(*start_ptr!='\n' || *start_ptr!='\0')
					{
						if(h>2)
						{
								//printf("\nh=%d char=%c||",h,*start_ptr);
								//fprintf(stderr,"\nInvalid Character in file 1!");
								//return 0;
							break;
						}
						while(*start_ptr==' '|| *start_ptr=='\t')
							*start_ptr++;
						if(*start_ptr>='0' && *start_ptr<='9')
						{
							s[h]=start_ptr;
							while(*start_ptr>='0' && *start_ptr<='9')
								*start_ptr++;
							*start_ptr++='\0';
							h++;
						}
						else
						{
							fprintf(stderr,"\nInvalid Character in file !");
							exit(1);
						}
					}

					if(strlen(s[0])>0)
					{
						double t=atof(s[0]);
						if(t>0.0)
							md->lam=t;
						else
						{
							fprintf(stderr,"\ninappropriate value for lambda in file");
							exit(1);
						}			
					}
					
					if(strlen(s[1])>0)
					{
						int t=atoi(s[1]);
						if(t>0 && t<2147483647)
				        		md->nToken=t;
						else
						{
							fprintf(stderr,"\ninappropriate value for P in file");
							exit(1);
						}			
					}
					
					if(strlen(s[2])>0)
					{
						double t=atof(s[2]);
						if(t>0.0)
					        	md->servTime=t;
						else
						{
							fprintf(stderr,"\ninappropriate value for mu in file");
							exit(1);
						}			
					}
					
					My402ListAppend(&datalist,md);
               			}
			}
         	}
		else
		{
			fprintf(stderr,"\nData in file is inappropriate !");
			exit(1);
		}
	}
	return 1;
}

/*int CheckValid(char *start_ptr)
{
	char *temp=start_ptr;
	while(*temp!='\0')
	{
		if(*temp>='0' && *temp<='9')
			*temp++;
		else if(*temp=='\n')
			break;
		else
		{
			fprintf(stderr,"\nData in file is inaprropriate ! invalid=%c||",*temp);
			return 0;
		}
	}
	return 1;
}*/
