#include <stdio.h>
#include <string.h>
#include <stdlib.h>

short *make_pair_table(char *structure)
{
    /* returns array representation of structure.
       table[i] is 0 if unpaired or j if (i.j) pair.  */
   int i,j,hx;
   int length;
   short *stack;
   short *table;
   
   length = strlen(structure);
   stack = (short *) malloc(sizeof(short)*(length+1));
   table = (short *) malloc(sizeof(short)*(length+2));
   table[0] = length;
   
   for (hx=0, i=1; i<=length; i++) {
      switch (structure[i-1]) {
       case '(': 
         stack[hx++]=i;
         break;
       case ')':
         j = stack[--hx];
         if (hx<0) {
            fprintf(stderr, "unbalanced brackets in %s\n", structure);
	    free(stack); free(table); return NULL;
         }
         table[i]=j;
         table[j]=i;
         break;
       default:   /* unpaired base, usually '.' */
         table[i]= 0;
         break;
      }
   }
   free(stack);
   if (hx!=0) {
      fprintf(stderr, "unbalanced brackets %s\n", structure);
      free(table);
      return NULL;
   }
   return(table);
}

void nrerror(char *message)       /* output message upon error */
{
    fprintf(stderr, "\n%s\n", message);
    exit(0);
}

void printRnaStruct(short* structure, int length) 
//print out secondary struct in the form: 1-4;13-16, 6-10; 30-34...
{
  int i,j;
  int st, stp;

  i = 0;
  while(i < length) {
    while(i < length && (structure[i] == 0 || structure[i] < i)) i++;
    st = i;    
    while(i < length && (structure[i]-1 == structure[i+1])) i++; 
    stp = i;
    if (i >= length) break;
    printf("%d-%d; %d-%d,  ",st+1, stp+1, structure[stp],structure[st]);
    i++;
  }
}
