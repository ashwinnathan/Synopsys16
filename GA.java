package travelingSalesman;

import java.util.ArrayList;


public class GA
{

    /* GA parameters */
    private static final boolean elitism = true;

    private static final double mutationRate = 0.7;


    // Evolves a population over one generation
    public static Population evolvePopulation( Population pop, int mutationNum, boolean localMinimum )
    {
        Population newPopulation = new Population( pop.populationSize(), false );
        // Keep our best individual if elitism is enabled
        int elitismOffset = 0;
        if ( elitism )
        {
            newPopulation.saveTour( 0, pop.getFittest() );
            elitismOffset = 1;
        }

        // Crossover population
        // Loop over the new population's size and create individuals from
        // Current population
        for ( int i = elitismOffset; i < newPopulation.populationSize(); i++ )
        {
            // Select parents
            Tour parent1;
            Tour parent2;

            parent1 = tournamentSelection( pop, 15 );
            parent2 = tournamentSelection( pop, 15 );

            // Crossover parents
            Tour child = crossover( parent1, parent2 );
            // Add child to new population
            newPopulation.saveTour( i, child );
        }

        ArrayList<Integer> indices = null;
        // Mutate the new population a bit to add some new genetic material
        for ( int i = elitismOffset; i < newPopulation.populationSize(); i++ )
        {
            Tour current = newPopulation.getTour( i );
            Tour model = null;
            if ( mutationNum == 1 && localMinimum == false )
            {
                if ( i < newPopulation.populationSize() * .05 )
                {
                    model = pop.getFittest();
                }
                 else if ( i < newPopulation.populationSize() * .08 )
                 {
                 model = pop.getNextFittest( pop.getFittest() );
                 }
                // else if ( i < (int)( ( newPopulation.populationSize() * 3 ) /
                // 10.0 ) )
                // {
                // model = pop.getNextFittest( pop.getNextFittest(
                // pop.getFittest() ) );
                // }
                if ( model != null )
                {
                    indices = new ArrayList<Integer>();
                    if ( current.getCity( 0 ) != model.getCity( 0 ) )
                    {
                        indices.add( 0 );
                    }
                    for ( int index = 1; index < newPopulation.getTour( i ).tourSize(); index++ )
                    {
                        if ( current.getCity( index ) != model.getCity( index )
                            || current.getCity( index - 1 ) != model.getCity( index - 1 ) )
                        {
                            indices.add( index );
                        }

                    }
                    intelligentSwap( current, indices );
                }
                else
                {
                    translocation( current );
                }
            }
            else
            {
                translocation( current );
            }

        }

        return newPopulation;

    }


    // Applies crossover to a set of parents and creates offspring
    public static Tour crossover( Tour parent1, Tour parent2 )
    {
        // Create new child tour
        Tour child = new Tour();

        // Get start and end sub tour positions for parent1's tour
        int startPos = (int)( Math.random() * parent1.tourSize() );
        int endPos = (int)( Math.random() * parent1.tourSize() );

        // Loop and add the sub tour from parent1 to our child
        for ( int i = 0; i < child.tourSize(); i++ )
        {
            // If our start position is less than the end position
            if ( startPos < endPos && i > startPos && i < endPos )
            {
                child.setCity( i, parent1.getCity( i ) );
            } // If our start position is larger
            else if ( startPos > endPos )
            {
                if ( !( i < startPos && i > endPos ) )
                {
                    child.setCity( i, parent1.getCity( i ) );
                }
            }
        }

        // Loop through parent2's city tour
        for ( int i = 0; i < parent2.tourSize(); i++ )
        {
            // If child doesn't have the city add it
            if ( !child.containsCity( parent2.getCity( i ) ) )
            {
                // Loop to find a spare position in the child's tour
                for ( int ii = 0; ii < child.tourSize(); ii++ )
                {
                    // Spare position found, add city
                    if ( child.getCity( ii ) == null )
                    {
                        child.setCity( ii, parent2.getCity( i ) );
                        break;
                    }
                }
            }
        }
        return child;
    }


    // Mutate a tour using swap mutation
    private static void swap( Tour tour )
    {
        if ( Math.random() < mutationRate )
        {
            // Loop through tour cities

            // Apply mutation rate
            // Get a second random position in the tour
            int tourPos1 = (int)( tour.tourSize() * Math.random() );
            int tourPos2 = (int)( tour.tourSize() * Math.random() );

            // Get the cities at target position in tour
            City city1 = tour.getCity( tourPos1 );
            City city2 = tour.getCity( tourPos2 );

            // Swap them around
            tour.setCity( tourPos2, city1 );
            tour.setCity( tourPos1, city2 );
        }

    }


    private static void intelligentSwap( Tour tour, ArrayList<Integer> indices )
    {

        
            // Loop through tour cities
            if ( !indices.isEmpty() )
            {
                // Apply mutation rate
                // Get a second random position in the tour
                int arrayPos1 = (int)( ( indices.size() - 1 ) * Math.random() );
                int arrayPos2 = (int)( ( indices.size() - 1 ) * Math.random() );

                // Get the cities at target position in tour
                City city1 = tour.getCity( indices.get( arrayPos1 ) );
                City city2 = tour.getCity( indices.get( arrayPos2 ) );

                // Swap them around
                tour.setCity( indices.get( arrayPos2 ), city1 );
                tour.setCity( indices.get( arrayPos1 ), city2 );
            }
            else
            {
                swap( tour );
            }
        

    }


    // Use same intelligent swapping method for other types of mutations as well
    public static void inversion( Tour tour )
    {
        if ( Math.random() < mutationRate )
        {
            // Get start and end positions
            int tourPosS = (int)( tour.tourSize() * Math.random() );
            int tourPosE = (int)( tour.tourSize() * Math.random() );
            if ( tourPosS > tourPosE )
            {
                int temp = tourPosS;
                tourPosS = tourPosE;
                tourPosE = temp;
            }
            while ( tourPosS < tourPosE && tourPosS != tourPosE )
            {
                City tempCity = tour.getCity( tourPosS );
                tour.setCity( tourPosS, tour.getCity( tourPosE ) );
                tour.setCity( tourPosE, tempCity );
                tourPosE--;
                tourPosS++;
            }
        }

    }


    public static void translocation( Tour tour )
    {
        if ( Math.random() < mutationRate )
        {

            int start1 = (int)( ( tour.tourSize() - 1 ) * Math.random() );
            int end1 = (int)( ( tour.tourSize() - 1 ) * Math.random() );
            int mutateLength = Math.abs( start1 - end1 );
            int start2 = (int)( ( tour.tourSize() - mutateLength ) * Math.random() );
            if ( end1 < start1 )
            {
                int temp = start1;
                start1 = end1;
                end1 = temp;
            }

            for ( int i = 0; i < mutateLength; i++ )
            {
                City city1 = tour.getCity( start1 );
                City city2 = tour.getCity( start2 );

                // Swap them around
                tour.setCity( start2, city1 );
                tour.setCity( start1, city2 );
                start1++;
                start2++;
            }
        }

    }


    // Selects candidate tour for crossover
    private static Tour tournamentSelection( Population pop, int tournamentSize )
    {
        // Create a tournament population
        Population tournament = new Population( tournamentSize, false );
        // For each place in the tournament get a random candidate tour and
        // add it
        for ( int i = 0; i < tournamentSize; i++ )
        {
            int randomId = (int)( Math.random() * pop.populationSize() );
            tournament.saveTour( i, pop.getTour( randomId ) );
        }
        // Get the fittest tour
        Tour fittest = tournament.getFittest();
        return fittest;
    }

}
