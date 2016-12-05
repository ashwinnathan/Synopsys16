package travelingSalesman;

public class Population
{

    // Holds population of tours
    Tour[] tours;


    // Construct a population
    public Population( int populationSize, boolean initialise )
    {
        tours = new Tour[populationSize];
        // If we need to initialise a population of tours do so
        if ( initialise )
        {
            // Loop and create individuals
            for ( int i = 0; i < populationSize(); i++ )
            {
                Tour newTour = new Tour();
                newTour.generateIndividual();
                saveTour( i, newTour );
            }
        }
    }


    // Saves a tour
    public void saveTour( int index, Tour tour )
    {
        tours[index] = tour;
    }


    // Gets a tour from population
    public Tour getTour( int index )
    {
        return tours[index];
    }


    // Gets the best tour in the population
    public Tour getFittest()
    {
        Tour fittest = tours[0];
        // Loop through individuals to find fittest
        for ( int i = 1; i < populationSize(); i++ )
        {
            if ( fittest.getFitness() <= getTour( i ).getFitness() )
            {
                fittest = getTour( i );
            }
        }
        return fittest;
    }
    
    public int getIndexOfFittest()
    {
        Tour fittest = tours[0];
        int index = 0;
        // Loop through individuals to find fittest
        for ( int i = 1; i < populationSize(); i++ )
        {
            if ( fittest.getFitness() <= getTour( i ).getFitness() )
            {
                fittest = getTour( i );
                index = i;
            }
        }
        return index;
    }


    public Tour getNextFittest( Tour mostFit )
    {
        Tour fittest;
        if ( tours[0].getFitness() == mostFit.getFitness() )
        {
            fittest = tours[1];
        }
        else
        {
            fittest = tours[0];
        }
        // Loop through individuals to find fittest
        for ( int i = 1; i < populationSize(); i++ )
        {
            if ( fittest.getFitness() <= getTour( i ).getFitness()
                && fittest.getFitness() > mostFit.getFitness() )
            {
                fittest = getTour( i );
            }
        }
        return fittest;
    }


    public double getDiversity()
    {
        double averageFitness = 0;
        for ( int i = 0; i < populationSize(); i++ )
        {
            Tour tour = getTour( i );
            averageFitness += tour.getFitness();
        }
        averageFitness = averageFitness / populationSize();
        double variance = 0;
        for ( int i = 0; i < populationSize(); i++ )
        {
            Tour tour = getTour( i );
            variance += ( tour.getFitness() - averageFitness ) * ( tour.getFitness() - averageFitness );
        }
        variance *= Math.pow( 10, 10 );
        return Math.sqrt( variance / populationSize() );
    }


    public double getAvgDiversity()
    {
        double averageFitness = 0;
        for ( int i = 0; i < populationSize(); i++ )
        {
            Tour tour = getTour( i );
            averageFitness += tour.getFitness();
        }
        return averageFitness / populationSize();

    }


    // Gets population size
    public int populationSize()
    {
        return tours.length;
    }
}