import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaMeasurements {

    public static void main(String[] args) {

        int numberOfAllocations = 1000;
        long totalTimeAlloc = 0, totalTimeMemAccStatic = 0, totalTimeMemAccDynamic = 0, totalTimeThreadCr = 0;
        long totalTimeThreadCS = 0, totalTimeAddList = 0, totalTimeRemoveList = 0, totalTimeReverse = 0;
        int nrTests = 1000; // array dimension

        for (int i = 0; i < numberOfAllocations; i++) {
            long elapsedTimeMemAlloc = measureMemoryAllocationTime(nrTests);
            long elapsedTimeMemAccStatic = measureStaticMemoryAccess(nrTests);
            long elapsedTimeMemAccDynamic = measureDynamicMemoryAccess(nrTests);
            long elapsedTimeThreadCr = measureThreadCreation();
            long elapsedTimeThreadCS = measureThreadContextSwitch();
            long elapsedTimeAddList = measureElementAdditionToList();
            long elapsedTimeRemoveList = measureElementRemovalFromList();
            long elapsedTimeReverse = measureListReversal();


            totalTimeMemAccDynamic += elapsedTimeMemAccDynamic;
            totalTimeAlloc += elapsedTimeMemAlloc;
            totalTimeMemAccStatic += elapsedTimeMemAccStatic;
            totalTimeThreadCr += elapsedTimeThreadCr;
            totalTimeThreadCS += elapsedTimeThreadCS;
            totalTimeAddList += elapsedTimeAddList;
            totalTimeRemoveList += elapsedTimeRemoveList;
            totalTimeReverse += elapsedTimeReverse;

        }

        // Calculate average elapsed time
        long averageTimeMemAlloc = totalTimeAlloc / numberOfAllocations;
        long averageTimeMemAccStatic = totalTimeMemAccStatic / numberOfAllocations;
        long averageTimeMemAccDynamic = totalTimeMemAccDynamic / numberOfAllocations;
        long averageTimeThreadCr = totalTimeThreadCr / numberOfAllocations;
        long averageTimeThreadCS = totalTimeThreadCS / numberOfAllocations;
        long averageTimeAddList = totalTimeAddList / numberOfAllocations;
        long averageTimeRemove = totalTimeRemoveList / numberOfAllocations;
        long averageTimeReverse = totalTimeReverse / numberOfAllocations;


        // Print and write results to a file
        writeResultToFile("java_results.txt", averageTimeMemAlloc, averageTimeMemAccStatic, averageTimeMemAccDynamic, averageTimeThreadCr, averageTimeThreadCS, averageTimeAddList, averageTimeRemove, averageTimeReverse, numberOfAllocations);
    }

    private static long measureMemoryAllocationTime(int size) {
        long startTime = System.nanoTime();
        int[] dynamicArray = new int[size];
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static long measureStaticMemoryAccess(int size) {
        int[] staticArray = new int[size];
        long startTime = System.nanoTime();
        for (int i = 0; i < size; i++) {
            int element = staticArray[i];
        }
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static long measureDynamicMemoryAccess(int size) {
        int[] dynamicArray = new int[size];
        long startTime = System.nanoTime();
        for (int i = 0; i < size; i++) {
            dynamicArray[i] = i;
            int element = dynamicArray[i];
        }
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static long measureThreadCreation() {
        long startTime = System.nanoTime();
        Thread thread = new Thread(() -> {
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static long measureThreadContextSwitch() {
        final Object lock = new Object();
        final long[] elapsedTime = new long[1];

        Thread thread = new Thread(() -> {
            long startTime = System.nanoTime();
            synchronized (lock) {
                lock.notify();
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            long endTime = System.nanoTime();
            elapsedTime[0] = endTime - startTime;
        });

        synchronized (lock) {
            thread.start();
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized (lock) {
            lock.notify();
        }

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return elapsedTime[0];
    }

    private static long measureElementAdditionToList() {
        long startTime = System.nanoTime();

        List<Integer> myList = new ArrayList<>();
        myList.add(42);  // Add an element to the list

        long endTime = System.nanoTime();

        return endTime - startTime;
    }

    private static long measureElementRemovalFromList() {
        List<Integer> myList = new ArrayList<>();
        myList.add(42);  // Add an element to the list

        long startTime = System.nanoTime();

        // Remove the element from the list
        myList.remove(0);


        long endTime = System.nanoTime();

        return endTime - startTime;
    }

    private static long measureListReversal() {
        List<Integer> myList = new ArrayList<>();

        // Add elements to the list
        for (int i = 0; i < 1000; i++) {
            myList.add(i);
        }

        long startTime = System.nanoTime();

        // Reverse the list
        Collections.reverse(myList);

        long endTime = System.nanoTime();

        return endTime - startTime;
    }

    private static void writeResultToFile(String fileName, long elapsedTimeMemAlloc, long elapsedTimeMemAccStatic, long elapsedTimeMemAccDynamic, long elapsedTimeThreadCr, long elapsedTimeThreadCS, long averageTimeAddList, long averageTimeRemove, long averageTimeReverse, long numberOfAllocations) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            writer.write(elapsedTimeMemAlloc + "\n"); //Average memory allocation
            writer.write(elapsedTimeMemAccStatic + "\n");//Average memory access dynamic
            writer.write(elapsedTimeMemAccDynamic + "\n");//Average memory access dynamic
            writer.write(elapsedTimeThreadCr + "\n");//Average thread creation
            writer.write(elapsedTimeThreadCS + "\n");//Average thread context switch
            writer.write(averageTimeAddList + "\n");//Average add
            writer.write(averageTimeRemove + "\n");//Average remove
            writer.write(averageTimeReverse + "\n");//Average reverse

            System.out.println(elapsedTimeMemAlloc); //Average memory allocation
            System.out.println(elapsedTimeMemAccStatic);//Average memory access dynamic
            System.out.println(elapsedTimeMemAccDynamic);//Average memory access dynamic
            System.out.println(elapsedTimeThreadCr);//Average thread creation
            System.out.println(elapsedTimeThreadCS);//Average thread context switch
            System.out.println(averageTimeAddList);//Average add
            System.out.println(averageTimeRemove);//Average remove
            System.out.println(averageTimeReverse);//Average reverse

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
