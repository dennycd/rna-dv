/*****************************************************************
         HotKnot: A heuristic algorithm for RNA secondary 
            structure prediction including pseudoknots
         File: score.cpp
         Description: 
               Interface between folding functions and LEModel library.
 
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



#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#include "score.h"

#include "Stack.h"
#include "Loop.h"
#include "Input.h"
#include "Defines.h"
#include "Bands.h"
#include "LoopList.h"


int Score::sPair( int len, int* sequence, int i, int j)
{
        i = i - 1; 
        j = j - 1;

	return tstackh[sequence[i]]
                  [sequence[j]]
                  [sequence[i+1]]
                  [sequence[j-1]];
}

int Score::F1( int len,  char* csequence, int* sequence, int i, int j){

  return LEhairpin_loop_energy(i-1, j-1, sequence, csequence);

}

int Score:: F2(  int len,  int* sequence,  int i, int k,
	 int l, int j){

	if ( (k== i+1) && (j== l+1))
		return LEstacked_pair_energy (i-1, j-1, sequence);
	else
    	return LEinternal_loop_energy (i-1, j-1, k-1, l-1, sequence);


}
int Score::intloop( int len,  int* s,
   int i, int k, int l, int j
){

  return F2(len, s, i, k, l, j);
}


char ccc(int i){
	switch(i){
		case 0:
			return 'A';
		case 1:
			return 'C';
		case 2:
			return 'G';
		case 3:
			return 'T';
	};

};


bool Amazing(int i, int j){
	int x;
	if (i > j){
		x = i;
		i = j;
		j = x;
	};

	if ( (i == 2) && (j == 3))
		return 0;


	if ( (i == 0) && (j != 3))
		return 1;

	if ( (i == 1) && (j != 2))
		return 1;

	return 0;



}


long Score::score(
   int len, char* s, short* p, int TRACE)
{   
 
    ReadInput* input = new ReadInput(len, s, p);
    Stack* st = new Stack(input);
    Bands* ban = new Bands(input, st);
    Loop* tree = new Loop(0, len + 1, input, ban, st);
    int a, b;

    for (int i = 1; i <= input->Size; i++){
      if (input->BasePair(i) > 0) {
        if(st->Add(i, a, b)){
	  tree->addLoop(a, b);
        }
	  }
    }

	int x = 3;
	float  f = tree->Energy();
	delete input;
        delete st;
        delete ban;
        delete tree;
        return f;
  
}

Score::~Score(){}
Score::Score(){
  sc = 0;
}
