/*****************************************************************
         HotKnot: A heuristic algorithm for RNA secondary 
            structure prediction including pseudoknots 
    Date        : Oct. 16, 2004
    copyright   : (C) 2004 by Jihong Ren, Baharak Rastegari  
    email       : jihong@cs.ubc.ca, baharak@cs.ubc.ca       
******************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <ctype.h>
#include <unistd.h>
#include <string.h>

#include "goodStem.h"
#include "simfold.h"
#include "hotspot.h"
#include "utils.h"

#define INF 10000;


struct Hotspot* hotspots;
int noPS=0;
int TRACE=0;
int *sp;
float T=400.0;//threshold for sub-sequence
float T_RATIO = 0.8; 
/*Consider only subsequent 2ndary structures that have energy lower than 
  T_RATIO*energy of the best non-pseudoknotted structure */
int MaxSubOpt = 20;
struct Node* listOfNodes[50]; 
int count;//number of nodes
int numRnaStruct;  //total number of different Rna structures

extern void PlotRna(char* seqName, char *sequence, short *structure, char *filename,float score);


#define PRIVATE static

static char  scale1[] = "....,....1....,....2....,....3....,....4";
static char  scale2[] = "....,....5....,....6....,....7....,....8";

PRIVATE void usage(void);

/*--------------------------------------------------------------------------*/

int main(int argc, char *argv[])
{
  char *string; 
  char line[5000];
  char *structure=NULL, *cstruc=NULL;
  char  fname[13], ffname[20], gfname[20];

  char  *ns_bases=NULL, *c;
  int   i, length, l, sym, r;
  double energy, min_en;
  double kT, sfact=1.07;
  int   pf=0, istty;
  int noconv=0;
  int InputFile = 1;
  int StrCon = 0;    
  int endFlag;
  char outPSFile[100], bpseqFile[100], seqName[100], inFile[100], ctFile[100];
  struct Node *rootNode;
  char tempFile[10]="hello";
  FILE* input_file;
  int MaxHotspots = 200;
  int first = 0;

  string=NULL;
   
  for (i=1; i<argc; i++) {
    if (argv[i][0]=='-') 
      switch ( argv[i][1] )
	{
	case 'n':
	  if ( strcmp(argv[i], "-noPS")==0) noPS = 1;
	  if ( strcmp(argv[i], "-noGU")==0) noGU = 1;
	  break;
	case 'I':
	  if (i==argc-1) usage();
          strcpy(seqName, argv[++i]);
	  break;
	case 'c':
	  StrCon = 1;
	  break;
	case 't':
	  TRACE = 1;
	  break;	
	case 'b':
	  first = 1;
	  break;
	default: usage();
	} 

  }

   char config_file[200] = "params/pairfold.conf";
   int dna_or_rna = RNA;
   double temperature = 37;
   init_data (config_file, dna_or_rna, temperature);
   
   strcpy(inFile, seqName);
   strcat(inFile,".seq");
   if ((input_file = fopen(inFile, "r")) == NULL) {
           fprintf(stderr, "Cannot open %s\n", inFile);
           fprintf(stderr, "please only provide the sequence name\n");
           usage();
	   return 0;
   }
   fscanf(input_file,"%s",&line);
   string = (char *) malloc((strlen(line)+1)*sizeof(char));
   strcpy(string, line);
   length = (int) strlen(string);
   structure = (char *) malloc((length+1)*sizeof(char));

   for (l = 0; l < length; l++) 
     structure[l] = '.';
   structure[length] = 0;

   if (StrCon == 1) {
     fscanf(input_file, "%s", &line);
     
     int sclen; 
     sclen = strlen(line);
     if (strlen(line) > length) {
       fprintf(stderr, "--------------------WARNING-----------------------\n");
       fprintf(stderr, "The length of the structure constraint is %d\n", sclen);
       fprintf(stderr, "The length of the sequence is %d\n", length);
       fprintf(stderr, "Extra constraints are ignored!\n");
       line[length] = 0;
       strcpy(structure, line);
     }
     else if (strlen(line) < length) {
       fprintf(stderr, "--------------------WARNING----------------------\n");
       fprintf(stderr, "The length of the structure constraint is %d\n", sclen);
       fprintf(stderr, "The length of the sequence is %d\n", length);
       fprintf(stderr, "The rest of the sequence is assumed to be unconstrained!\n");
       strcpy(structure, line);
       structure[sclen] = '.';
     }
     else
       strcpy(structure, line);
     printf("\nThe following bases are forced to be single stranded (the first base has index 1): \n");
     for (l = 0; l < length; l++){ 
       if (structure[l] == 'x') printf("%d   %c\n", l+1, string[l]); 
     }
   }

   for (l = 0; l < length; l++) {
      string[l] = toupper(string[l]);
      if (string[l] == 'T') string[l] = 'U';
      if (structure[l] != '.' && structure[l] != 'x') {
	fprintf(stderr, "-------------------WARNING----------------------\n");
	fprintf(stderr, "There are letters other than . and x in the structure constraint, treated as .\n");
	structure[l] = '.';
      }
   }
      
    //-----initialization of rootNode
    rootNode=(struct Node *)malloc(sizeof(struct Node));
    rootNode->secStructure=(short *)malloc((length)*sizeof(short));
    rootNode->fixedPairs=(short *)malloc((length)*sizeof(short));
    rootNode->constraint=(char *)malloc((length+1)*sizeof(char));
    rootNode->constraint[length] = 0;
    rootNode->numChild = 0;
    rootNode->length = length;
    rootNode->score = 0;
    for(i=0;i<length;i++)
      {
	rootNode->secStructure[i]=0;
	rootNode->fixedPairs[i]=0;
	//rootNode->constraint[i]='.';
	
      }
    strcpy(rootNode->constraint, structure);
    //===end of initialization    
 
    printf("LENGTH OF THE RNA IS %d.\n",length);
    printf("%s\n", string);    
    
    numRnaStruct = 0;
    InitHotspots(MaxHotspots,length);
    GenerateStemList(length, string, structure);
    
    endFlag=secondaryStruct(string,length,rootNode,rootNode, MaxHotspots);
    ClearHotspots(MaxHotspots);
    min_en=endFlag;
    printf("In total, %d nodes created. \n", count);
    printf(" total number of Rna structures: %d \n", numRnaStruct);
    sprintf(ctFile, "%s%s", seqName, ".ct");
    FILE *cfile = fopen(ctFile, "w");
    if (cfile == NULL) {
      printf("can't open %s \n", ctFile);
      exit(1);
    }

      for (i=0; i < numRnaStruct; i++) {
	sprintf(outPSFile, "%s%d%s", seqName,i,".ps");
        sprintf(bpseqFile, "%s%d%s", seqName, i, ".bpseq");
	printf(" sub optimal energy  %f kal/mol\n", -listOfNodes[i]->score /1000.0);
	printRnaStruct(listOfNodes[i]->secStructure,length);
	printf("\n");
	if ((first == 1 && i==0) || (first == 0)) {
	  if (noPS == 0) PlotRna(seqName, string, listOfNodes[i]->secStructure, outPSFile, listOfNodes[i]->score);
                  
          FILE *bfile = fopen(bpseqFile, "w");
	  if (bfile == NULL) {
	    printf("can't open %s \n", bpseqFile);
	    exit(1);
	  }
	  for (int b = 0; b < length; b++) {
	    fprintf(bfile, "%d %c %d\n", b+1, string[b], listOfNodes[i]->secStructure[b]);            
	  }          
	}
        fprintf(cfile, "%5d   ENERGY = %.2f %s suboptimal structure %d\n", length, -listOfNodes[i]->score/1000.0, seqName, i);
        for (int b = 0; b < length; b++) {
         fprintf(cfile, "%5d %c    %4d %4d %4d %4d\n", b+1, string[b], b, b+2, listOfNodes[i]->secStructure[b], b+1);
        }
        
      }
     
        
    
    (void) fflush(stdout);
    free(string);
    free(structure);
    return 0;   //the following code was from the original RNAfold.c, not used here.

}

PRIVATE void usage(void)
{
  nrerror("usage:\n"
	  "HotKnot [-noGU (do not allow GU pair)] [-noPS(no ps, bpseq file output)] [-t (trace)] [-b (output only the ps, bpseq file for the best structure)] -I seqName");
}


