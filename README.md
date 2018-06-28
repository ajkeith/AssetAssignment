# Asset Assignment

## Introduction

The sensor assignment problem can include many sensor assets and targets of various types and effectiveness. Two different heuristics are used to develop a prioritized target list as an input to the assignment problem: rank order centroid weighting and pairwise ratio weighting. The rank order centroid weghting is much easier to implement in practice, but is also lower fidelity than pairwise ratio weighting. 

To compare the performance of these heuristics in a simulated annealing search algorithm, we use a bounded
temperature schedule and execute a screening design across three parameter settings and ten environmental factors. The rank order centroid weighting can produce a total expected value within 5% of the pairwise ratio comparison weighting method for this domain. The highest performing settings for the bounded temperature schedule for the rank order centroid method are high initial temperature (100), high maximum steps (1,000,000), and high decay rate (8) for problem instances up to 100 by 100 units. These settings and results
are robust to changes in environment such as target value distribution, clustering, density, and type of target. 

## Abbreviated Documentation

### Main 

main

Input: Edit RUN OPTIONS at header 

Output: .csv file containing results, command line progress indicator 


runSearch 

Input: searchType, searchParameters, heuristic, initialState, numSearchSteps 

Output: finalAssignment

Description: All input parameters as described above, produces final state from search. 


### SimAnnealingSearch 

findBestMove 

Input: initialState, heuristic, numSearchSteps, initialTemperature, decayRate 

Output: finalAssignment 

Description: Implements Simulated Annealing as described in Russell and Norvig, 2010, page 126. 


tempSchedule 

Input: currentStep (𝑘), numSearchSteps (𝐾), initialTemperature (𝑇0), decayRate (𝛼) 

Output: currentTemperature (𝑇) 

Description: 𝑇 = 𝑇0(1 − 𝑘/𝐾)^𝛼 from Press, et. al. 2007. 


### HillSearch 

findBestMove 

Input: initialState, heuristic 

Output: nextState 

Description: Implements hill climbing as described in Russell and Norvig, 2010, page 122. Note that the output is nextState, so the algorithm must be run in a loop until final state or max iterations reached. 


### Assignment 

Assignment 

Input: width, height, targetParameters, assetParameters 

Output: None 

Description: Constructor. Creates a randomized initial assignment of targets and assets on top of a grid environment, all of which are specified by the input parameters. 


assignNew 

Input: assetIndex, gridIndex, type 

Output: None 

Description: Assigns an asset of a given type to a grid square for initial state, used by constructor. 


assign 

Input: assetIndex, gridIndex 

Output: None

Description: Assigns an asset to a grid location. If another asset is currently at that grid location, that is we start with (Asset A at Location A) and (Asset B at Location B), then the final configuration is (Asset B at Location A) and (Asset A at Location B). 


value 

Input: asset, grid, heuristic 

Output: expectedValue 

Description: Calculates the expected value of a single asset-target combination based on likelihood of detection and target value. The heuristic affects the effective target value. 


totalValue 

Input: heuristic 

Output: expectedValueTotal 

Description: Calculates the total expected value of all asset-target combinations in an assignment, given a heuristic. 


### Grid 

Grid 

Input: xLocation, yLocation, index 

Output: None 

Description: Constructor. Defaults to an empty grid (no target present). Target parameters can be set using setters. 


### Asset 

Asset 

Input: grid (optional) 

Output: None 

Description: Constructor. Creates an asset with no assignment (optional: assigned to input grid). 


### TransferCSV 

importCSV 

Input: filename, numRuns, numCategoricalVariables, numNumericalVariables 

Output: None 

Description: Imports .csv input file as described earlier in the guide. 


exportCSV 

Input: filename, numRuns, numResponses, results 

Output: None 

Description: Writes .csv output file as described earlier in the guide. 


### MersenneTwiser 

Description: MersenneTwister Version 22 pseudo random number generator from https://cs.gmu.edu/~sean/research/. 
