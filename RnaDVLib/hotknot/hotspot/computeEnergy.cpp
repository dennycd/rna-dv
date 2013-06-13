/*****************************************************************
         HotKnot: A heuristic algorithm for RNA secondary 
            structure prediction including pseudoknots
         File: computeEnergy.cpp
         Description: 
			Given a seq file(contains RNA primary structure) and its bpseq file 
			(base pair file, contains RNA secondary structure, see examples in TestSeq/RealStruct),
			compute its free energy value and plot the arc diagram for its structure.
			
    Date        : Oct. 16, 2004
    copyright   : (C) 2004 by Baharak Rastegari, Jihong Ren  
    email       : baharak@cs.ubc.ca, jihong@cs.ubc.ca        
******************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/


#include "simfold.h"
#include "Stack.h"
#include "Loop.h"
#include "Bands.h"
#include "Input.h"


extern void PlotRna(char* seqName, char *sequence, short *structure, char *filename,float score);
void usage(void);


/******************************************************************
mirela_init: initializing using simfold program (written by Mirela Andronescue)
*******************************************************************/
void mirella_init()
{
    double energy;
    // configuration file
    char config_file[200] = "../hotspot/params/pairfold.conf";

    // what to fold: RNA or DNA
    int dna_or_rna = RNA;

    // temperature: any integer or real number between 0 and 100
    // represents degrees Celsius
    double temperature = 37;

    // initialize the thermodynamic parameters
    // call init_data only once for the same dna_or_rna and same temperature
    // if one of them changes, call init_data again (see below)
    init_data (config_file, dna_or_rna, temperature);

}


/******************************************************************
main: taking input files, call appropriate functions for: storing
essential information in the structures which will be used by the 
program, identifying closed regions, adding the closed regions to 
the tree, computing the free energy of the secondary structure
and drawing the plots.
*******************************************************************/
int main(int argc, char ** argv){

	mirella_init();	

	int mode = 1;

        char fileSeq[100], fileStruct[100];
	char prefix[100];
	FILE *input_file;

	ReadInput * R;
	
	if (argc < 2){	  
	  usage();
          return 0;
        }
       	
       	strcpy(fileStruct, argv[1]);
        char outPSFile[100] = "ArcDiagram.ps";
     	        		     
        if ((input_file = fopen(fileStruct, "r")) == NULL) {
           fprintf(stderr, "Cannot open %s\n", fileStruct);           
           usage();
	   return 0;
	}
        fclose(input_file);
	R = new ReadInput(fileStruct);
	
	Stack * s = new Stack(R);
	Bands * B = new Bands(R, s);

	printf("Seq: %s \n", R->CSequence);
	printf("size: %d \n", R->Size);
        for (int i = 1; i <= R->Size; i++) {
	  printf("%d ", R->Sequence[i]);
	}
	printf("-------------------------------\n Making the Loop Tree\n");
	Loop * L = new Loop(0, MaxN+1, R, B, s);

	int a, b; //will store the borders of a closed regoin
	for (int i = 1; i <= R->Size; i++){
		if (R->BasePair(i)>= 0){
		  if (s->Add(i, a, b)){
		  //If a closed region is identifed add it to the tree by calling addLoop
		    L->addLoop(a,b);
		  };
		};
	};

	L->Print(-1);
	printf("-------------------------------\n ");


	
	short* secstructure = new short[R->Size+1];
	char *sequence = new char[R->Size+2];
        sequence[R->Size+1] = '\0';
	for(int i = 0; i < R->Size+1; i++) {
	  secstructure[i] = (short)(R->Sequence[i]);
	  if (secstructure[i] == -1) secstructure[i] = 0;
	  sequence[i] = R->CSequence[i];
          printf("%d %c %d \n", i, sequence[i], secstructure[i]); 
	}
	PlotRna(prefix, &sequence[1], &secstructure[1], outPSFile, L->Energy());
	printf("The total free energy is %f\n", -L->Energy()/1000.0);
	printf("Arc Diagram of the given structure is now in ArcDiagram.ps\n");
}


/******************************************************************
*******************************************************************/
void usage(void)
{
  printf("usage: computeEnergy bpseqFile\n");
  exit(1);
}
