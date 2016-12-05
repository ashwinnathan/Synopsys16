package travelingSalesman;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class TSP_GA
{

    public static void main( String[] args )
    {
        // initialDistance - the distance of the original population's most fit
        // solution
        // prevDistance - variable the keeps track of the previous generation's
        // best distance in order to keep track of repetitions
        // currentDistance - current population's best distance
        // genCount - keeps track of current generation
        // streakCount - keeps track of any repetitions in the most fit solution
        int initialDistance = 0, prevDistance = 0, currentDistance = 0, genCount = 0, streakCount = 0;
        // Whether or not the most fit solution is at a local minima
        boolean localMin = false;
        ArrayList<Integer> streaks;

        // Initializes the Berlin52 problem
        try
        {
            FileReader fileReader = new FileReader( "Berlin52.txt" );
            BufferedReader br = new BufferedReader( fileReader );
            String x = "";
            while ( ( x = br.readLine() ) != null )
            {
                TourManager
                    .addCity( new City( (int)Double.parseDouble( x ), (int)Double.parseDouble( br.readLine() ) ) );
            }

            br.close();
        }
        catch ( FileNotFoundException ex )
        {
            ex.printStackTrace();
        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        }
        
        // Initialize a new population with 50 tours and sets the evolve to the
        // original population that needs to be evolved
        Population pop = new Population( 50, true ), evolve = pop;
        // Find the most fit solution is in new population
        initialDistance = pop.getFittest().getDistance();
        prevDistance = evolve.getFittest().getDistance();

        // Print starting distance and type of mutation
        System.out.println( "Initial distance: " + initialDistance );
        System.out.println( "Swap" );

        // Initialize array list which will keep track of solution recurrences
        streaks = new ArrayList<Integer>();

        // Runs the algorithm for the alloted number of generations and evolves
        // the population in each run
        while ( genCount < 50000 )
        {

            evolve = GA.evolvePopulation( evolve, 0, localMin );
            currentDistance = evolve.getFittest().getDistance();
            if ( prevDistance == currentDistance )
            {
                streakCount++;
            }
            else
            {
                streaks.add( streakCount );
                streakCount = 0;
            }
            genCount++;
            prevDistance = currentDistance;
            if ( genCount % 100 == 0 )
            {
                System.out.println( evolve.getFittest().getDistance() );
            }

        }
        // Trial 2 which uses the intelligent swap mutation to converge to the
        // optima faster

        // Reinitialize variables
        genCount = 0;
        evolve = pop;
        currentDistance = initialDistance;
        localMin = false;
        streakCount = 0;
        System.out.println( "Intelligent Swap" );

        while ( genCount < 50000 )
        {

            evolve = GA.evolvePopulation( evolve, 1, localMin );
            currentDistance = evolve.getFittest().getDistance();
            if ( prevDistance == currentDistance )
            {
                streakCount++;
            }
            else
            {
                streakCount = 0;
            }
            if ( streakCount > mode(streaks) )
            {
                localMin = true;
            }
            else
            {
                localMin = false;
            }
            genCount++;
            prevDistance = currentDistance;
            if ( genCount % 100 == 0 )
            {
                System.out.println( evolve.getFittest().getDistance() );
            }

        }

    }


    public static int mode( ArrayList<Integer> streaks )
    {
        HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
        int mostFrequent = streaks.get( 0 );
        for ( int i = 0; i < streaks.size(); i++ )
        {
            int key = streaks.get( i ).hashCode();
            if ( counts.containsKey( key ) )
            {
                int value = counts.get( key ) + 1;
                counts.put( key, value );
                if ( value > key )
                {
                    key = value;
                    mostFrequent = key;

                }
            }
            else
            {
                counts.put( key, 1 );
            }
        }

        return mostFrequent;
    }

}
